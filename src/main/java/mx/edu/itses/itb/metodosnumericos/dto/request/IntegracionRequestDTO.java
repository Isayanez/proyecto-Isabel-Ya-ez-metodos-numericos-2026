package mx.edu.itses.itb.metodosnumericos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegracionRequestDTO {

    private String funcion; // Ej. "x^2" o "sin(x)"
    private Double a;       // Límite inferior
    private Double b;       // Límite superior
    private Integer n;      // Número de subintervalos (trapecios/parábolas)
    private String metodo;  // "TRAPECIO", "SIMPSON_13", "SIMPSON_38"

    // Getters y Setters explícitos para asegurar compatibilidad con la compilación
    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Double getA() {
        return a;
    }

    public void setA(Double a) {
        this.a = a;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}