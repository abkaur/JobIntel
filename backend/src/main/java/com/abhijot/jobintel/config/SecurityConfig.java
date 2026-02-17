package com.abhijot.jobintel.config;

import com.abhijot.jobintel.user.User;
import com.abhijot.jobintel.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User u = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            return org.springframework.security.core.userdetails.User
                    .withUsername(u.getEmail())
                    .password(u.getPasswordHash())
                    .roles(u.getRole().name()) // STUDENT / RECRUITER / ADMIN -> becomes ROLE_STUDENT...
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // ✅ IMPORTANT
                .authorizeHttpRequests(auth -> auth

                        // allow CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        // Jobs
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/jobs").hasRole("RECRUITER")

                        // Applications
                        .requestMatchers(HttpMethod.POST, "/api/apply").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/applications").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/api/my-applications").hasRole("STUDENT")

                        // Admin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
