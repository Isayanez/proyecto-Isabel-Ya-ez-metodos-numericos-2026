package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.dto.request.IntegracionRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.IntegracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/integracion")
public class IntegracionController {

    private final IntegracionService integracionService;

    public IntegracionController(IntegracionService integracionService) {
        this.integracionService = integracionService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<Map<String, Object>> calcular(@RequestBody IntegracionRequestDTO request) {
        try {
            Map<String, Object> resultado = integracionService.calcularIntegracion(request);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "exito", false,
                "mensaje", "Error interno al calcular la integración: " + e.getMessage()
            ));
        }
    }
}