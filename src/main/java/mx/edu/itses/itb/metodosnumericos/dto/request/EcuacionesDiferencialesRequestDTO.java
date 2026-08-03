package mx.edu.itses.itb.metodosnumericos.dto.request;

public class EcuacionesDiferencialesRequestDTO {

    private String ecuacion; // dy/dx = f(x, y), ej. "-2*x^3 + 12*x^2 - 20*x + 8.5" o "x + y"
    private Double x0;       // Valor inicial de x
    private Double y0;       // Valor inicial de y (condición inicial)
    private Double xf;       // Valor final de x a evaluar
    private Double h;        // Tamaño del paso (step size)
    private String metodo;   // "EULER", "EULER_MEJORADO", "RK4"

    // Constructor vacío (necesario para la deserialización JSON en Spring Boot)
    public EcuacionesDiferencialesRequestDTO() {
    }

    // Constructor completo
    public EcuacionesDiferencialesRequestDTO(String ecuacion, Double x0, Double y0, Double xf, Double h, String metodo) {
        this.ecuacion = ecuacion;
        this.x0 = x0;
        this.y0 = y0;
        this.xf = xf;
        this.h = h;
        this.metodo = metodo;
    }

    // Getters y Setters
    public String getEcuacion() {
        return ecuacion;
    }

    public void setEcuacion(String ecuacion) {
        this.ecuacion = ecuacion;
    }

    public Double getX0() {
        return x0;
    }

    public void setX0(Double x0) {
        this.x0 = x0;
    }

    public Double getY0() {
        return y0;
    }

    public void setY0(Double y0) {
        this.y0 = y0;
    }

    public Double getXf() {
        return xf;
    }

    public void setXf(Double xf) {
        this.xf = xf;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}