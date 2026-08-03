package mx.edu.itses.itb.metodosnumericos.controller;

import mx.edu.itses.itb.metodosnumericos.dto.request.EcuacionesDiferencialesRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.EcuacionesDiferencialesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ecuaciones-diferenciales")
public class EcuacionesDiferencialesController {

    @Autowired
    private EcuacionesDiferencialesService service;

    @PostMapping("/calcular")
    public Map<String, Object> calcular(@RequestBody EcuacionesDiferencialesRequestDTO request) {
        return service.resolverEDO(request);
    }
}