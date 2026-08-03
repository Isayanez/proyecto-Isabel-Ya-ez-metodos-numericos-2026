package mx.edu.itses.itb.metodosnumericos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaicesRequestDTO {

    private String metodo;          // "BISECCION", "REGLA_FALSA", "PUNTO_FIJO", "NEWTON_RAPHSON", "SECANTE", etc.
    private String funcion;         // f(x) o g(x)
    
    // Nombres estándar de límites/puntos
    private Double a;
    private Double b;
    private Double x0;              // Punto inicial
    private Double x1;              // Segundo punto inicial
    
    // Compatibilidad con nombres xi / xu si los usas en el HTML
    private Double xi;
    private Double xu;
    
    @Builder.Default
    private Double tolerancia = 0.0001;

    @Builder.Default
    private Integer maxIteraciones = 100;

    @Builder.Default
    private Double delta = 0.01;    // Para Secante Modificado

    // --- Helpers de compatibilidad para evitar NullPointerException ---

    public double getX0Oa() {
        if (x0 != null) return x0;
        if (a != null) return a;
        if (xi != null) return xi;
        return 0.0;
    }

    public double getX1Ob() {
        if (x1 != null) return x1;
        if (b != null) return b;
        if (xu != null) return xu;
        return getX0Oa() + 0.1;
    }

    // Getters manuales de apoyo por si se usan indistintamente xi/a o xu/b
    public Double getA() {
        return a != null ? a : xi;
    }

    public Double getB() {
        return b != null ? b : xu;
    }
}