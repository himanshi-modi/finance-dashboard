package com.finance.dashboard.config;

import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.entity.model.User;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
            return;
        }
        if(userRepository.findByEmail("admin").isEmpty()){
            User admin=new User();
            admin.setEmail("admin@gmail.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setFailedLoginAttempts(0);
            admin.setAccountLockedUntil(null);
            admin.setRefreshToken(null);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setStatus(Status.ACTIVE);
            userRepository.save(admin);

        }
    }
}
