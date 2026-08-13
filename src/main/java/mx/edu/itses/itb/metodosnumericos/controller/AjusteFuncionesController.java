package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.service.AjusteFuncionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/ajuste-funciones")
public class AjusteFuncionesController {

    @Autowired
    private AjusteFuncionesService service;

    @GetMapping
    public String index() {
        return "ajuste-funciones";
    }

    @PostMapping("/calcular")
    public String calcular(
            @RequestParam("metodo") String metodo,
            @RequestParam("xVals") String xVals,
            @RequestParam("yVals") String yVals,
            @RequestParam(value = "xEval", defaultValue = "0") double xEval,
            Model model) {

        String[] xArrStr = xVals.split(",");
        String[] yArrStr = yVals.split(",");
        double[] x = new double[xArrStr.length];
        double[] y = new double[yArrStr.length];

        for (int i = 0; i < xArrStr.length; i++) {
            x[i] = Double.parseDouble(xArrStr[i].trim());
            y[i] = Double.parseDouble(yArrStr[i].trim());
        }

        if (metodo.equals("newton")) {
            Map<String, Object> res = service.interpolaciónNewton(x, y, xEval);
            model.addAttribute("resultado", "Polinomio de Newton: " + res.get("polinomio") + "\nEvaluado en " + xEval + " = " + res.get("valorEvaluado"));
        } else if (metodo.equals("lagrange")) {
            Map<String, Object> res = service.interpolaciónLagrange(x, y, xEval);
            model.addAttribute("resultado", "Polinomio de Lagrange: " + res.get("polinomio") + "\nEvaluado en " + xEval + " = " + res.get("valorEvaluado"));
        } else if (metodo.equals("regresion")) {
            Map<String, Object> res = service.regresionLineal(x, y);
            model.addAttribute("resultado", "Ecuación de Regresión: " + res.get("ecuacion"));
        }

        return "ajuste-funciones";
    }
}