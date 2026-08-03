package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.dto.request.DerivacionRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.DerivacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/derivacion")
public class DerivacionController {

    private final DerivacionService derivacionService;

    public DerivacionController(DerivacionService derivacionService) {
        this.derivacionService = derivacionService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<Map<String, Object>> calcular(@RequestBody DerivacionRequestDTO request) {
        try {
            Map<String, Object> resultado = derivacionService.calcularDerivada(request);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "exito", false,
                "mensaje", "Error al procesar la derivación: " + e.getMessage()
            ));
        }
    }
}