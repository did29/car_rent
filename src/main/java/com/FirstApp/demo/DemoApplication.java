package com.FirstApp.demo;

import com.FirstApp.demo.model.User;
import com.FirstApp.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserService userService) {
        return args -> {
            System.out.println("=== Инициализация на тестови данни ===");

            User admin = userService.getUserByEmail("admin@carrent.com");
            if (admin == null) {
                admin = new User();
                admin.setName("Администратор");
                admin.setEmail("admin@carrent.com");
                admin.setPassword("admin123");
                admin.setPhone("+359888123456");
                admin.setRole("ADMIN");

                userService.updateUser(admin);
                System.out.println("✅ Администраторски акаунт създаден: admin@carrent.com / admin123");
            }

            User user = userService.getUserByEmail("user@example.com");
            if (user == null) {
                user = new User();
                user.setName("Иван Петров");
                user.setEmail("user@example.com");
                user.setPassword("password123");
                user.setPhone("+359887654321");
                user.setRole("USER");

                userService.updateUser(user);
                System.out.println("✅ Потребителски акаунт създаден: user@example.com / password123");
            }

            System.out.println("=== Приложението е готово ===");
            System.out.println("🌐 URL: http://localhost:8080");
            System.out.println("🔐 Админ вход: admin@carrent.com / admin123");
            System.out.println("👤 Потребител вход: user@example.com / password123");
        };
    }
}