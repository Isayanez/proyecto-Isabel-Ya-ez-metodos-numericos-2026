package mx.edu.itses.itb.metodosnumericos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.edu.itses.itb.metodosnumericos.dto.request.AjusteRequestDTO; 
import mx.edu.itses.itb.metodosnumericos.dto.request.RaizRequestDTO; // Se agrega la importación

@Controller
public class VistasController {

    // Mapeo para la vista HTML de Raíces
    @GetMapping({"/raices", "/calculo-raices"})
    public String raices(Model model) {
        model.addAttribute("moduloActivo", "raices");
        model.addAttribute("raizRequestDTO", new RaizRequestDTO()); // Se agrega el DTO inicial
        return "raices/raices"; 
    }

    @GetMapping({"/sistemas", "/sistemas-lineales"})
    public String sistemasLineales(Model model) {
        model.addAttribute("moduloActivo", "sistemas-lineales");
        return "sistemas/sistemas-lineales";
    }

    @GetMapping({"/ajuste", "/ajuste-curvas"})
    public String ajusteCurvas(Model model) {
        model.addAttribute("moduloActivo", "ajuste-curvas");
        model.addAttribute("ajusteRequestDTO", new AjusteRequestDTO());
        return "ajuste";
    }

    @GetMapping("/derivacion")
    public String derivacion(Model model) {
        model.addAttribute("moduloActivo", "derivacion");
        return "derivacion";
    }

    @GetMapping("/integracion")
    public String integracion(Model model) {
        model.addAttribute("moduloActivo", "integracion");
        return "integracion/integracion";
    }

    @GetMapping({"/ecuaciones", "/ecuaciones-diferenciales"})
    public String ecuacionesDiferenciales(Model model) {
        model.addAttribute("moduloActivo", "ecuaciones-diferenciales");
        return "ecuaciones/ecuaciones-diferenciales";
    }
}