package loja.toystore.toy;

import loja.toystore.toy.model.User;
import loja.toystore.toy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ToyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User adminUser = new User(
                    "admin",
                    passwordEncoder.encode("adminpassword"),
                    "ADMIN"
                );
                userRepository.save(adminUser);
                System.out.println("Admin user has been initialized.");
            }
        };
    }
}