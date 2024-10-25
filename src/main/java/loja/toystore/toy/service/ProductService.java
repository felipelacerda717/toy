package loja.toystore.toy.service;

import loja.toystore.toy.model.Product;
import loja.toystore.toy.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
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

    public List<Product> getAllProducts() {
        logger.debug("Getting all products");
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long id) {
        logger.debug("Getting product with ID {}", id);
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        logger.debug("Saving product: {}", product);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        logger.debug("Deleting product with ID {}", id);
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        logger.debug("Getting products by category: {}", category);
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProductsByName(String name) {
        logger.debug("Searching products by name containing: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getFeaturedProducts() {
        logger.debug("Getting featured products");
        List<Product> products = productRepository.findAll().stream()
                .limit(3)
                .collect(Collectors.toList());
        logger.debug("Found {} featured products", products.size());
        return products;
    }

    public List<String> getAllCategories() {
        logger.debug("Getting all product categories");
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    public Page<Product> getProductsByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
        } else if (minPrice != null) {
            return productRepository.findByCategoryAndPriceGreaterThanEqual(category, minPrice, pageable);
        } else if (maxPrice != null) {
            return productRepository.findByCategoryAndPriceLessThanEqual(category, maxPrice, pageable);
        } else {
            return productRepository.findByCategory(category, pageable);
        }
    }

    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (minPrice != null) {
            return productRepository.findByPriceGreaterThanEqual(minPrice, pageable);
        } else if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqual(maxPrice, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }
}