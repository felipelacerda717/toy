package loja.toystore.toy.repository;

import loja.toystore.toy.model.Order;
import loja.toystore.toy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
}