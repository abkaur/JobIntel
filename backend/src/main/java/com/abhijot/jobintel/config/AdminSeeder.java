package com.abhijot.jobintel.config;

import com.abhijot.jobintel.user.Role;
import com.abhijot.jobintel.user.User;
import com.abhijot.jobintel.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@jobintel.com";

            if (!repo.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail(adminEmail);
                admin.setPasswordHash(encoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);

                repo.save(admin);
                System.out.println("✅ Admin user created");
            }
        };
    }
}
