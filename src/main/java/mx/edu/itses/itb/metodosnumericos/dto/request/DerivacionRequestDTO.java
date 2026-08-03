package mx.edu.itses.itb.metodosnumericos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DerivacionRequestDTO {

    private String funcion;       // Ej. "x^2 + 2*x" o "sin(x)"
    private Double x;             // Punto a evaluar
    private Double h;             // Paso de diferenciación (ej. 0.01)
    private String tipoDiferencia; // "ADELANTE", "ATRAS", "CENTRADA"
    private Integer ordenDerivada; // 1 o 2
}