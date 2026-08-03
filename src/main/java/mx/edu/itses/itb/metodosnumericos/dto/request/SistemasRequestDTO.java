package mx.edu.itses.itb.metodosnumericos.dto.request;

import java.util.List;

public class SistemasRequestDTO {

    private String metodo;
    private Integer dimension;
    private List<List<Double>> matrizA;
    private List<Double> vectorB;
    private Double tolerancia;
    private Integer maxIteraciones;

    // Constructor vacío
    public SistemasRequestDTO() {
    }

    // Constructor completo
    public SistemasRequestDTO(String metodo, Integer dimension, List<List<Double>> matrizA, 
                              List<Double> vectorB, Double tolerancia, Integer maxIteraciones) {
        this.metodo = metodo;
        this.dimension = dimension;
        this.matrizA = matrizA;
        this.vectorB = vectorB;
        this.tolerancia = tolerancia;
        this.maxIteraciones = maxIteraciones;
    }

    // Getters y Setters
    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public List<List<Double>> getMatrizA() {
        return matrizA;
    }

    public void setMatrizA(List<List<Double>> matrizA) {
        this.matrizA = matrizA;
    }

    public List<Double> getVectorB() {
        return vectorB;
    }

    public void setVectorB(List<Double> vectorB) {
        this.vectorB = vectorB;
    }

    public Double getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(Double tolerancia) {
        this.tolerancia = tolerancia;
    }

    public Integer getMaxIteraciones() {
        return maxIteraciones;
    }

    public void setMaxIteraciones(Integer maxIteraciones) {
        this.maxIteraciones = maxIteraciones;
    }

    // Builder manual
    public static SistemasRequestDTOBuilder builder() {
        return new SistemasRequestDTOBuilder();
    }

    public static class SistemasRequestDTOBuilder {
        private String metodo;
        private Integer dimension;
        private List<List<Double>> matrizA;
        private List<Double> vectorB;
        private Double tolerancia;
        private Integer maxIteraciones;

        public SistemasRequestDTOBuilder metodo(String metodo) {
            this.metodo = metodo;
            return this;
        }

        public SistemasRequestDTOBuilder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public SistemasRequestDTOBuilder matrizA(List<List<Double>> matrizA) {
            this.matrizA = matrizA;
            return this;
        }

        public SistemasRequestDTOBuilder vectorB(List<Double> vectorB) {
            this.vectorB = vectorB;
            return this;
        }

        public SistemasRequestDTOBuilder tolerancia(Double tolerancia) {
            this.tolerancia = tolerancia;
            return this;
        }

        public SistemasRequestDTOBuilder maxIteraciones(Integer maxIteraciones) {
            this.maxIteraciones = maxIteraciones;
            return this;
        }

        public SistemasRequestDTO build() {
            return new SistemasRequestDTO(metodo, dimension, matrizA, vectorB, tolerancia, maxIteraciones);
        }
    }
}