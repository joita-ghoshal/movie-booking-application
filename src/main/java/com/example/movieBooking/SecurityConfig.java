package com.example.movieBooking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow static assets to load
                        .requestMatchers("/*.css", "/*.js", "/login").permitAll()
                        // Require login for root and booking features
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Redirect user to the bundled index.html upon successful login
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                );

        return http.build();
    }
}