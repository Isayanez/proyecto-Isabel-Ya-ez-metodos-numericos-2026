package mx.edu.itses.itb.metodosnumericos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaizResponseDTO {

    private String metodo;
    private String mensaje;
    private Double raiz;
    private Integer iteraciones;

    // Asegúrate de que se llamen exactamente así para que el Builder coincida:
    private Boolean convergio;      // <--- genera .convergio(boolean)
    private Double errorRelativo;   // <--- genera .errorRelativo(double)
}
