package loja.toystore.toy;

import loja.toystore.toy.model.User;
import loja.toystore.toy.model.Product;
import loja.toystore.toy.service.UserService;
import loja.toystore.toy.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class ToyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDatabase(UserService userService, ProductService productService) {
        return args -> {
            // Inicialização do usuário admin
            if (userService.findByUsername("admin").isEmpty()) {
                User adminUser = new User("admin", "adminpassword", "ADMIN");
                userService.createUser(adminUser);
                System.out.println("Admin user has been initialized.");
            } else {
                System.out.println("Admin user already exists.");
            }

            if (userService.findByUsername("cliente").isEmpty()) {
                User clientUser = new User("cliente", "clientepassword", "USER");
                userService.createUser(clientUser);
                System.out.println("Client user has been initialized.");
            } else {
                System.out.println("Client user already exists.");
            }

            // Inicialização de produtos de exemplo
            if (productService.getAllProducts().isEmpty()) {
                productService.saveProduct(new Product("Boneca", "Boneca de pano", new BigDecimal("39.99"), "Brinquedos", "/images/boneca.jpg"));
                productService.saveProduct(new Product("Carrinho", "Carrinho de controle remoto", new BigDecimal("59.99"), "Brinquedos", "/images/carrinho.jpg"));
                productService.saveProduct(new Product("Quebra-cabeça", "Quebra-cabeça de 1000 peças", new BigDecimal("29.99"), "Jogos", "/images/quebra-cabeca.jpg"));
                System.out.println("Sample products have been initialized.");
            } else {
                System.out.println("Products already exist.");
            }
        };
    }
}