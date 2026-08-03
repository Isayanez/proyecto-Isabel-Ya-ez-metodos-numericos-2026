package mx.edu.itses.itb.metodosnumericos.dto.request;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AjusteRequestDTO {

    private String tipoAjuste;
    private Integer gradoPolinomio = 1;
    private String puntosX;
    private String puntosY;

    public AjusteRequestDTO() {
    }

    public AjusteRequestDTO(String tipoAjuste, Integer gradoPolinomio, String puntosX, String puntosY) {
        this.tipoAjuste = tipoAjuste;
        this.gradoPolinomio = gradoPolinomio;
        this.puntosX = puntosX;
        this.puntosY = puntosY;
    }

    public String getTipoAjuste() {
        return tipoAjuste;
    }

    public void setTipoAjuste(String tipoAjuste) {
        this.tipoAjuste = tipoAjuste;
    }

    public Integer getGradoPolinomio() {
        return gradoPolinomio != null ? gradoPolinomio : 1;
    }

    public void setGradoPolinomio(Integer gradoPolinomio) {
        this.gradoPolinomio = gradoPolinomio;
    }

    public String getPuntosX() {
        return puntosX;
    }

    public void setPuntosX(String puntosX) {
        this.puntosX = puntosX;
    }

    public String getPuntosY() {
        return puntosY;
    }

    public void setPuntosY(String puntosY) {
        this.puntosY = puntosY;
    }

    // --- Métodos de utilidad para convertir las cadenas de texto a List<Double> ---

    public List<Double> getPuntosXLista() {
        if (puntosX == null || puntosX.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(puntosX.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    public List<Double> getPuntosYLista() {
        if (puntosY == null || puntosY.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(puntosY.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }
}