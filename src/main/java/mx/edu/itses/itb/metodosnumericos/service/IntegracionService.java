package mx.edu.itses.itb.metodosnumericos.service;

import mx.edu.itses.itb.metodosnumericos.dto.request.IntegracionRequestDTO;
import java.util.Map;

public interface IntegracionService {
    Map<String, Object> calcularIntegracion(IntegracionRequestDTO request);
}