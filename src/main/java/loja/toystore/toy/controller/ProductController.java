package loja.toystore.toy.controller;

import loja.toystore.toy.model.Product;
import loja.toystore.toy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Exibe todos os produtos
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/catalog"; // Atualizei o retorno para "products/catalog"
    }

    // Exibe detalhes de um produto específico
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> model.addAttribute("product", product));
        return "products/view";
    }

    // Exibe o formulário para adicionar um novo produto
    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form"; // Atualizei o retorno para "products/form"
    }

    // Salva ou atualiza um produto
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // Exibe o formulário para editar um produto existente
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> model.addAttribute("product", product));
        return "products/form"; // Atualizei o retorno para "products/form"
    }

    // Deleta um produto pelo ID
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // Exibe os produtos de uma categoria específica
    @GetMapping("/category/{category}")
    public String listProductsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        return "products/category"; // Atualizei o retorno para "products/category"
    }

    // Pesquisa produtos pelo nome
    @GetMapping("/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> products = productService.searchProductsByName(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "products/search"; // Atualizei o retorno para "products/search"
    }
}
