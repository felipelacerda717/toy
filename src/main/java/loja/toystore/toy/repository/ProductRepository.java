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

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Buscar produtos por categoria
    List<Product> findByCategory(String category);

    // Buscar produtos por nome (contendo a string de busca, ignorando maiúsculas/minúsculas)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Buscar produtos por faixa de preço
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Buscar produtos por categoria e ordenar por preço
    List<Product> findByCategoryOrderByPriceAsc(String category);

    // Você pode adicionar mais métodos de consulta personalizados conforme necessário
}