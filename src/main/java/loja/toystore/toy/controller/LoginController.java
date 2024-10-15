package loja.toystore.toy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model; // Add this import statement


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String checkout, Model model) {
        if (checkout != null) {
            model.addAttribute("checkout", true);
        }
        return "login";
    }

    @GetMapping("/login-success")
    public String loginSuccess(@RequestParam(required = false) String checkout) {
        return checkout != null ? "redirect:/checkout" : "redirect:/";
    }
}