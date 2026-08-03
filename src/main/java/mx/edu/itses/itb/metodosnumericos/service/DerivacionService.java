package mx.edu.itses.itb.metodosnumericos.service;

import mx.edu.itses.itb.metodosnumericos.dto.request.DerivacionRequestDTO;
import java.util.Map;

public interface DerivacionService {
    Map<String, Object> calcularDerivada(DerivacionRequestDTO request);
}