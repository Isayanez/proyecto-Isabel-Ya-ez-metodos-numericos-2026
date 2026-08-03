package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.dto.request.RaizRequestDTO;
import mx.edu.itses.itb.metodosnumericos.dto.response.RaizResponseDTO;
import mx.edu.itses.itb.metodosnumericos.service.RaicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/raices")
public class RaicesController {

    @Autowired
    private RaicesService raicesService;

    @PostMapping("/calcular")
    public ResponseEntity<?> calcular(@RequestBody RaizRequestDTO request) {
        try {
            RaizResponseDTO resultado = raicesService.calcular(request);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "exito", false,
                "mensaje", "Error al calcular la raíz: " + e.getMessage()
            ));
        }
    }
}