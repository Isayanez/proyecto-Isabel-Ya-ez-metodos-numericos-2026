package mx.edu.itses.itb.metodosnumericos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IteracionDTO {
    private int iteracion;
    private double xi;
    private double xu;
    private double xr; // Raíz estimada
    private double fXi;
    private double fXu;
    private double fXr;
    private Double errorRelativo; // %
}