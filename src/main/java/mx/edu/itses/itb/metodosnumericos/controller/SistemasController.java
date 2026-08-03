package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.dto.request.SistemasRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.SistemasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sistemas-lineales") // Cambiado para coincidir con la URL que llama el HTML
@CrossOrigin(origins = "*")
public class SistemasController {

    private final SistemasService sistemasService;

    public SistemasController(SistemasService sistemasService) {
        this.sistemasService = sistemasService;
    }

    @PostMapping("/calcular") // Cambiado de /resolver a /calcular
    public ResponseEntity<Map<String, Object>> resolver(@RequestBody SistemasRequestDTO request) {
        try {
            Map<String, Object> resultado = sistemasService.resolverSistema(request);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "exito", false,
                "mensaje", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "exito", false,
                "mensaje", "Error interno al procesar el sistema de ecuaciones: " + e.getMessage()
            ));
        }
    }
}