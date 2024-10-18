package loja.toystore.toy.controller;

import loja.toystore.toy.model.*;
import loja.toystore.toy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;
    private final PixPaymentService pixPaymentService;

    @Autowired
    public CheckoutController(CartService cartService, UserService userService, OrderService orderService, PixPaymentService pixPaymentService) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
        this.pixPaymentService = pixPaymentService;
    }

    @GetMapping
    public String showCheckoutPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("isAuthenticated", userDetails != null);
        return "checkout/form";
    }

    @PostMapping("/process")
public String processCheckout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    User user = userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));

    List<OrderItem> orderItems = cartService.getItems().stream()
            .map(this::convertCartItemToOrderItem)
            .collect(Collectors.toList());

    Order order = orderService.createOrder(user, orderItems);
    PixPayment pixPayment = pixPaymentService.generatePixPayment(order.getTotalAmount());

    model.addAttribute("order", order);
    model.addAttribute("pixPayment", pixPayment);
    
    cartService.clear(); // Limpa o carrinho após a criação do pedido
    
    return "checkout/confirmation";
}

    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getProduct().getPrice());
        return orderItem;
    }
}