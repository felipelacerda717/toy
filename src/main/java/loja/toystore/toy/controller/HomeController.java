package loja.toystore.toy.controller;

import loja.toystore.toy.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        logger.debug("Entering home method");
        try {
            model.addAttribute("featuredProducts", productService.getFeaturedProducts());
            logger.debug("Featured products added to model");
            return "index";
        } catch (Exception e) {
            logger.error("Error in home method", e);
            throw e;
        }
    }
}