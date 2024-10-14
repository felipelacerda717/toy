package loja.toystore.toy;

import loja.toystore.toy.model.User;
import loja.toystore.toy.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ToyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDatabase(UserService userService) {
        return args -> {
            if (userService.findByUsername("admin").isEmpty()) {
                User adminUser = new User("admin", "adminpassword", "ADMIN");
                userService.createUser(adminUser);
                System.out.println("Admin user has been initialized.");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}