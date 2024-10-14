package loja.toystore.toy.service;

import loja.toystore.toy.model.Product;
import loja.toystore.toy.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Obter todos os produtos
    public List<Product> getAllProducts() {
        logger.debug("Getting all products");
        return productRepository.findAll();
    }

    // Obter produto por ID
    public Optional<Product> getProductById(Long id) {
        logger.debug("Getting product with ID {}", id);
        return productRepository.findById(id);
    }

    // Salvar ou atualizar um produto
    public Product saveProduct(Product product) {
        logger.debug("Saving product: {}", product);
        return productRepository.save(product);
    }

    // Deletar produto por ID
    public void deleteProduct(Long id) {
        logger.debug("Deleting product with ID {}", id);
        productRepository.deleteById(id);
    }

    // Obter produtos por categoria
    public List<Product> getProductsByCategory(String category) {
        logger.debug("Getting products by category: {}", category);
        return productRepository.findByCategory(category);
    }

    // Buscar produtos pelo nome (parcialmente)
    public List<Product> searchProductsByName(String name) {
        logger.debug("Searching products by name containing: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Obter produtos em destaque (limitado a 3)
    public List<Product> getFeaturedProducts() {
        logger.debug("Getting featured products");
        List<Product> products = productRepository.findAll().stream().limit(3).collect(Collectors.toList());
        logger.debug("Found {} featured products", products.size());
        return products;
    }

    // Obter todas as categorias únicas
    public List<String> getAllCategories() {
        logger.debug("Getting all product categories");
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}
