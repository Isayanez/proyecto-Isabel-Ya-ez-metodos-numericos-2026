package mx.edu.itses.itb.metodosnumericos.service;

import mx.edu.itses.itb.metodosnumericos.dto.request.RaizRequestDTO;
import mx.edu.itses.itb.metodosnumericos.dto.response.RaizResponseDTO;

public interface RaicesService {
    
    // Cambia "calcularRaiz" por "calcular"
    RaizResponseDTO calcular(RaizRequestDTO request);
    
}