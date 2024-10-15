package loja.toystore.toy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model; // Add this import statement


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String targetUrl, Model model) {
        if (targetUrl != null && !targetUrl.isEmpty()) {
            model.addAttribute("targetUrl", targetUrl);
        }
        return "login";
    }
}