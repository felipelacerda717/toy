package loja.toystore.toy.controller;

import loja.toystore.toy.model.Product;
import loja.toystore.toy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Exibe todos os produtos no catálogo
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/catalog"; // Retorna a página do catálogo
    }

    // Exibe detalhes de um produto específico
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "products/view"; // Página de detalhes do produto
                })
                .orElse("redirect:/products");
    }

    // Exibe o formulário para adicionar um novo produto (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/admin/form"; // Formulário de novo produto
    }

    // Salva ou atualiza um produto (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                // Gere um nome de arquivo único
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                // Defina o caminho onde a imagem será salva
                Path path = Paths.get("src/main/resources/static/images/" + fileName);
                // Salve o arquivo
                Files.write(path, imageFile.getBytes());
                // Atualize o imageUrl do produto
                product.setImageUrl("/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Trate o erro adequadamente
            }
        }
        productService.saveProduct(product);
        return "redirect:/products/admin";
    }

    // Exibe o formulário para editar um produto existente (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "products/admin/form"; // Formulário de edição do produto
                })
                .orElse("redirect:/products/admin");
    }

    // Deleta um produto pelo ID (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products/admin"; // Redireciona após deletar o produto
    }

    // Exibe a lista de produtos na visão de administração (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/admin/list"; // Página de administração dos produtos
    }

    // Exibe os produtos de uma categoria específica
    @GetMapping("/category/{category}")
    public String listProductsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        return "products/category"; // Página de produtos filtrados por categoria
    }

    // Pesquisa produtos pelo nome
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> searchResults = productService.searchProducts(query);
        model.addAttribute("products", searchResults);
        model.addAttribute("searchQuery", query);
        return "products/search-results";
    }

    
}
