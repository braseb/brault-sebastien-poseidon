package com.poseidon.app.security;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;





//@Configuration
public class NoSecurityConfig {

    @Bean
    InMemoryUserDetailsManager userDetailsService() {
        // On crée un utilisateur "admin" avec ROLE_ADMIN
        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}password") // password inutile car pas de login
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())

                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults());
                
                
        return http.build();
    }

    /*@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Tout est permis
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable());

        // Créer un "fake login" en tant qu'admin
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin", // username
                null,    // pas de password
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) // rôle admin
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        return http.build();
    }*/
    
}
