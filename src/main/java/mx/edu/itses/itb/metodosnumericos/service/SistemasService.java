package mx.edu.itses.itb.metodosnumericos.service;

import mx.edu.itses.itb.metodosnumericos.dto.request.SistemasRequestDTO;
import java.util.Map;

public interface SistemasService {
    Map<String, Object> resolverSistema(SistemasRequestDTO request);
}