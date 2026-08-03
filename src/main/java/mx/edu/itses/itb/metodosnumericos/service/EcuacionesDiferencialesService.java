package mx.edu.itses.itb.metodosnumericos.service;

import mx.edu.itses.itb.metodosnumericos.dto.request.EcuacionesDiferencialesRequestDTO;
import java.util.Map;

public interface EcuacionesDiferencialesService {
    Map<String, Object> resolverEDO(EcuacionesDiferencialesRequestDTO request);
}