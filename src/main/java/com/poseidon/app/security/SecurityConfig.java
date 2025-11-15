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



@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
    
    @Bean
    SuccesLoginHandler succesLoginHandler(JwtService jwtService,
                                           UserService userService
                                          ) {
        return new SuccesLoginHandler(jwtService, userService);
    }
    
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

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
