package mx.edu.itses.itb.metodosnumericos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaizRequestDTO {

    private String metodo;
    private Double a;
    private Double b;
    private Double x0; // Usado para Newton-Raphson / Punto Fijo / Secante
    private Double x1; // Usado para Secante
    private Double tolerancia;
    private Integer maxIteraciones;
    private String funcion;

    // Helper para obtener la estimación inicial sin sufrir por NullPointerException
    public double getX0Oa() {
        if (x0 != null) return x0;
        if (a != null) return a;
        return 0.0;
    }

    public double getX1Ob() {
        if (x1 != null) return x1;
        if (b != null) return b;
        return getX0Oa() + 0.1;
    }
}