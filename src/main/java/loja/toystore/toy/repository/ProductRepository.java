package loja.toystore.toy.repository;

import loja.toystore.toy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Busca paginada por categoria
    Page<Product> findByCategory(String category, Pageable pageable);
    
    // Busca por categoria sem paginação
    List<Product> findByCategory(String category);
    
    // Busca por nome contendo string (case insensitive)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Busca por faixa de preço
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    // Busca por preço mínimo
    Page<Product> findByPriceGreaterThanEqual(BigDecimal minPrice, Pageable pageable);
    
    // Busca por preço máximo
    Page<Product> findByPriceLessThanEqual(BigDecimal maxPrice, Pageable pageable);
    
    // Busca por categoria e faixa de preço
    Page<Product> findByCategoryAndPriceBetween(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByCategoryAndPriceGreaterThanEqual(String category, BigDecimal minPrice, Pageable pageable);
    Page<Product> findByCategoryAndPriceLessThanEqual(String category, BigDecimal maxPrice, Pageable pageable);
}