package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.dto.request.AjusteRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.AjusteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ajuste")
public class AjusteController {

    private final AjusteService ajusteService;

    public AjusteController(AjusteService ajusteService) {
        this.ajusteService = ajusteService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<Map<String, Object>> calcular(@RequestBody AjusteRequestDTO request) {
        try {
            Map<String, Object> resultado = ajusteService.calcularAjuste(request);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "exito", false,
                "mensaje", "Error al calcular el ajuste de curvas: " + e.getMessage()
            ));
        }
    }
}