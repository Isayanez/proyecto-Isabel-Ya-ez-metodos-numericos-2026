package mx.edu.itses.itb.metodosnumericos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaicesResponseDTO {
    private Double raizCalculada;
    private boolean convergio;
    private String mensaje;
    private int iteracionesTotales;
    @Builder.Default
    private List<IteracionDTO> iteraciones = new ArrayList<>();
}