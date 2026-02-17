package com.abhijot.jobintel.auth;

import com.abhijot.jobintel.user.Role;
import com.abhijot.jobintel.user.User;
import com.abhijot.jobintel.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            // ✅ 409 instead of 500
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered"
            );
        }

        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPasswordHash(passwordEncoder.encode(req.password()));

        // ✅ default role if null (or if you already force STUDENT, keep it)
        Role role = (req.role() == null) ? Role.STUDENT : req.role();

        // ✅ block creating ADMIN from public register
        if (role == Role.ADMIN) role = Role.STUDENT;

        u.setRole(role);

        User saved = userRepository.save(u);

        return new AuthResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
