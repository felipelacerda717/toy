package loja.toystore.toy.controller;

import loja.toystore.toy.model.Product;
import loja.toystore.toy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
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

    // Exibe detalhes de um produto específico
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "products/view";
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

    @GetMapping
public String listProducts(@RequestParam(required = false) String category,
                         @RequestParam(required = false) BigDecimal minPrice,
                         @RequestParam(required = false) BigDecimal maxPrice,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "name,asc") String sort,
                         Model model) {
    
    // Obter direção e propriedade de ordenação
    String[] sortParams = sort.split(",");
    String sortProperty = sortParams[0];
    String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";
    Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    
    // Criar Pageable com ordenação
    PageRequest pageable = PageRequest.of(page, 9, Sort.by(direction, sortProperty));
    
    // Buscar produtos com filtros
    Page<Product> productPage;
    if (category != null && !category.isEmpty()) {
        productPage = productService.getProductsByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);
    } else if (minPrice != null || maxPrice != null) {
        productPage = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
    } else {
        productPage = productService.getAllProducts(pageable);
    }

    // Adicionar atributos ao modelo
    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("category", category);
    model.addAttribute("categories", productService.getAllCategories());

    return "products/list";
}

    // Pesquisa produtos pelo nome
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<Product> searchResults = productService.searchProducts(query, PageRequest.of(page, size));
        model.addAttribute("products", searchResults.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResults.getTotalPages());
        model.addAttribute("totalItems", searchResults.getTotalElements());
        model.addAttribute("searchQuery", query);
        return "products/search-results";
    }

    
}
