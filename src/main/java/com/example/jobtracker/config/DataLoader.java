package com.example.jobtracker.config;

import com.example.jobtracker.model.Role;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.EmailService;
import com.example.jobtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public void run(String... args) throws Exception {
        if (!userService.existsByUsername("admin")) {
            User admin = new User("admin", "admin@example.com", "admin123", Role.ADMIN);
            userService.saveUser(admin);
            emailService.sendWelcomeEmail(admin.getEmail(), admin.getUsername());
            System.out.println("Admin user created: username=admin, password=admin123");
        }
    }
}
