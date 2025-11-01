package com.poseidon.app.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.poseidon.app.repositories.UserRepository;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //@Autowired
    //private CustomUserDetailsService userDetailsService;
    
    //@Autowired
    //JwtAuthFilter jwtAuthFilter;
    
    //@Autowired
    //SuccesLoginHandler succesLoginHandler;
    
    //@Autowired
    //SuccesLogoutHandler succesLogoutHandler;
    
    @Bean
    SuccesLoginHandler succesLoginHandler(JwtService jwtService,
                                       UserRepository userRepository,
                                       OAuth2AuthorizedClientService clientService,
                                       PasswordEncoder passwordEncoder) {
        return new SuccesLoginHandler(jwtService, userRepository, clientService, passwordEncoder);
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
                .requestMatchers("/login", "/css/**", "/auth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                        //.loginPage("/app/login")
                        .successHandler(succesLoginHandler)
                        .permitAll())
            .oauth2Login(t -> t.successHandler(succesLoginHandler)
                                .permitAll())
            .logout(log -> log.logoutSuccessHandler(succesLogoutHandler))
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
