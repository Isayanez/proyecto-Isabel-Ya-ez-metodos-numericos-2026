package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.service.SistemasEcuacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SistemasAvanzadosController {

    @Autowired
    private SistemasEcuacionesService sistemasService;

    @GetMapping("/sistemas-avanzados")
    public String mostrarVista() {
        return "sistemas-avanzados";
    }

    @PostMapping("/sistemas-avanzados/calcular")
    public String calcularSistema(
            @RequestParam("metodo") String metodo,
            @RequestParam("a11") double a11, @RequestParam("a12") double a12, @RequestParam("b1") double b1,
            @RequestParam("a21") double a21, @RequestParam("a22") double a22, @RequestParam("b2") double b2,
            Model model) {

        double[][] A = {{a11, a12}, {a21, a22}};
        double[] B = {b1, b2};

        try {
            switch (metodo) {
                case "determinantes":
                    double[] resDet = sistemasService.resolverDeterminantes(A, B);
                    model.addAttribute("resultado", "x1 = " + String.format("%.4f", resDet[0]) + ", x2 = " + String.format("%.4f", resDet[1]));
                    break;
                case "gauss":
                    double[] resGauss = sistemasService.resolverEliminacionGaussiana(A, B);
                    model.addAttribute("resultado", "x1 = " + String.format("%.4f", resGauss[0]) + ", x2 = " + String.format("%.4f", resGauss[1]));
                    break;
                case "gauss-jordan":
                    double[] resGJ = sistemasService.resolverGaussJordan(A, B);
                    model.addAttribute("resultado", "x1 = " + String.format("%.4f", resGJ[0]) + ", x2 = " + String.format("%.4f", resGJ[1]));
                    break;
                case "jacobi":
                    List<String> histJacobi = sistemasService.resolverJacobi(A, B, 20, 0.0001);
                    model.addAttribute("historial", histJacobi);
                    break;
                case "gauss-seidel":
                    List<String> histGS = sistemasService.resolverGaussSeidel(A, B, 20, 0.0001);
                    model.addAttribute("historial", histGS);
                    break;
                default:
                    model.addAttribute("error", "Método no seleccionado.");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "sistemas-avanzados";
    }
}