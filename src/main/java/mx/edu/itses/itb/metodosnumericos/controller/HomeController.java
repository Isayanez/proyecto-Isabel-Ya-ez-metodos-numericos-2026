package mx.edu.itses.itb.metodosnumericos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // Ahora carga la plantilla index.html directamente en la raíz
    }
}