package com.poseidon.app.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.poseidon.app.services.UserService;

/**
 * Configuration class for Spring Security.
 * <p>
 * This class defines security settings for the application, including:
 * <ul>
 *     <li>JWT-based authentication and stateless session management.</li>
 *     <li>Custom login and logout handlers.</li>
 *     <li>Role-based access control for endpoints.</li>
 *     <li>OAuth2 login integration with Google.</li>
 *     <li>CORS configuration to allow requests from localhost.</li>
 *     <li>Password encoding using BCrypt.</li>
 * </ul>
 * <p>
 * The security configuration ensures that:
 * <ul>
 *     <li>Public endpoints such as login, OAuth2 callbacks, and static resources are accessible without authentication.</li>
 *     <li>Administrative endpoints are restricted to users with the ADMIN role.</li>
 *     <li>Application-specific endpoints are accessible to users with either ADMIN or USER roles.</li>
 *     <li>JWT authentication is applied before the standard username/password filter.</li>
 * </ul>
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
    /**
     * Defines a custom success login handler that generates JWT tokens
     * and updates user-related information upon successful authentication.
     *
     * @param jwtService the service responsible for generating JWT tokens
     * @param userService the service handling user data
     * @return a SuccesLoginHandler instance
     */
    @Bean
    SuccesLoginHandler succesLoginHandler(JwtService jwtService,
                                           UserService userService
                                          ) {
        return new SuccesLoginHandler(jwtService, userService);
    }
    
    /**
     * Configures the main security filter chain.
     * <p>
     * This includes disabling CSRF, enabling CORS, enforcing stateless sessions,
     * defining access rules, configuring login and logout handlers, and
     * adding the JWT authentication filter.
     *
     * @param http the HttpSecurity object
     * @param succesLoginHandler the custom login success handler
     * @param succesLogoutHandler the custom logout success handler
     * @param jwtAuthFilter the JWT authentication filter
     * @return a configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    SuccesLoginHandler succesLoginHandler,
                                    SuccesLogoutHandler succesLogoutHandler,
                                    JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(crsf -> crsf.disable())
                .cors(c -> c.configurationSource(configurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/app/login", "/login/oauth2/**", "/css/**", "/images/**", "/auth2/**", "/app/error", "/error").permitAll()
                .requestMatchers("/user/**", "/app/secure/**").hasRole("ADMIN")
                .requestMatchers("/trade/**", "/curvePoint/**", "/rating/**", "/bidList/**", "/ruleName/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                        .loginPage("/app/login")
                        .successHandler(succesLoginHandler)
                        .permitAll())
            .oauth2Login(t -> t.successHandler(succesLoginHandler)
                            .loginPage("/app/login")    
                            .permitAll())
            .logout(log ->  log
                            .logoutUrl("/app/logout")
                            .logoutSuccessHandler(succesLogoutHandler))
            .exceptionHandling(ex -> ex
                    .accessDeniedPage("/app/error") 
                )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            
            
        return http.build();
    }
    
    /**
     * Defines the password encoder bean used for hashing user passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configures CORS settings to allow requests from specific origins and methods.
     *
     * @return a configured UrlBasedCorsConfigurationSource
     */
    
    UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
