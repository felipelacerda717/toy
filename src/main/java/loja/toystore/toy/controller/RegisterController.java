package loja.toystore.toy.controller;

import loja.toystore.toy.model.User;
import loja.toystore.toy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                             @RequestParam String confirmPassword,
                             RedirectAttributes redirectAttributes) {
        try {
            // Validar se as senhas coincidem
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "As senhas não coincidem");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Verificar se o usuário já existe
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Nome de usuário já está em uso");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Definir papel do usuário como USER
            user.setRole("USER");

            // Salvar usuário
            userService.createUser(user);

            // Adicionar mensagem de sucesso
            redirectAttributes.addFlashAttribute("message", "Cadastro realizado com sucesso! Por favor, faça login.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar conta: " + e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
    }
}