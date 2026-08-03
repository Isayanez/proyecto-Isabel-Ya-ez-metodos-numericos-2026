package mx.edu.itses.itb.metodosnumericos.service;

import mx.edu.itses.itb.metodosnumericos.dto.request.AjusteRequestDTO;
import java.util.Map;

public interface AjusteService {
    Map<String, Object> calcularAjuste(AjusteRequestDTO request);
}