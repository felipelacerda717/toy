package loja.toystore.toy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EquipeController {

    @GetMapping("/equipe")
    public String showEquipePage() {
        return "equipe";  // Isso irá procurar por templates/equipe.html
    }
}