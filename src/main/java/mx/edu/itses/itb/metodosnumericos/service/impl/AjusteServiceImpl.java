package mx.edu.itses.itb.metodosnumericos.service.impl;

import mx.edu.itses.itb.metodosnumericos.dto.request.AjusteRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.AjusteService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AjusteServiceImpl implements AjusteService {

    @Override
    public Map<String, Object> calcularAjuste(AjusteRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La petición no puede ser nula.");
        }

        // Obtener los puntos con soporte flexible para List<Double> o String
        List<Double> xList = extraerListaPuntos(request.getPuntosX());
        List<Double> yList = extraerListaPuntos(request.getPuntosY());

        if (xList.isEmpty() || yList.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar los puntos (X, Y).");
        }

        if (xList.size() != yList.size()) {
            throw new IllegalArgumentException("La cantidad de valores en X debe ser igual a la de Y.");
        }

        int nPuntos = xList.size();
        int grado = (request.getGradoPolinomio() != null && request.getGradoPolinomio() > 0) 
                    ? request.getGradoPolinomio() : 1;

        if ("LINEAL".equalsIgnoreCase(request.getTipoAjuste())) {
            grado = 1;
        }

        if (nPuntos <= grado) {
            throw new IllegalArgumentException("El número de puntos (" + nPuntos + ") debe ser mayor al grado del polinomio (" + grado + ").");
        }

        // Sistema de Mínimos Cuadrados (Ecuaciones Normales)
        // Dimensión de la matriz A es (grado + 1) x (grado + 1)
        int m = grado + 1;
        double[][] A = new double[m][m];
        double[] B = new double[m];

        // Sumatorias de X^(i+j)
        double[] sumX = new double[2 * m];
        for (int k = 0; k < 2 * m; k++) {
            double suma = 0;
            for (int i = 0; i < nPuntos; i++) {
                suma += Math.pow(xList.get(i), k);
            }
            sumX[k] = suma;
        }

        // Llenar Matriz A
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = sumX[i + j];
            }
        }

        // Llenar Vector B (Sumatorias de Y * X^i)
        for (int i = 0; i < m; i++) {
            double suma = 0;
            for (int k = 0; k < nPuntos; k++) {
                suma += yList.get(k) * Math.pow(xList.get(k), i);
            }
            B[i] = suma;
        }

        // Resolver el sistema A * c = B con Eliminación Gaussiana
        double[] coeficientes = resolverGauss(A, B, m);

        // Calcular Coeficiente de Determinación (R²)
        double mediaY = yList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double st = 0.0; // Suma total de cuadrados
        double sr = 0.0; // Suma de residuos al cuadrado

        for (int i = 0; i < nPuntos; i++) {
            double xi = xList.get(i);
            double yi = yList.get(i);

            // Evaluar polinomio obtenido: P(x) = c0 + c1*x + c2*x^2 + ...
            double yPredicho = 0;
            for (int j = 0; j < m; j++) {
                yPredicho += coeficientes[j] * Math.pow(xi, j);
            }

            st += Math.pow(yi - mediaY, 2);
            sr += Math.pow(yi - yPredicho, 2);
        }

        double r2 = (st != 0) ? Math.max(0, 1 - (sr / st)) : 1.0;

        // Formatear Ecuación en String
        StringBuilder ecuacion = new StringBuilder("y = ");
        for (int i = coeficientes.length - 1; i >= 0; i--) {
            double val = Math.round(coeficientes[i] * 10000.0) / 10000.0;
            if (i < coeficientes.length - 1 && val >= 0) {
                ecuacion.append(" + ");
            } else if (val < 0) {
                ecuacion.append(" - ");
                val = Math.abs(val);
            }

            if (i == 0) {
                ecuacion.append(val);
            } else if (i == 1) {
                ecuacion.append(val).append("x");
            } else {
                ecuacion.append(val).append("x^").append(i);
            }
        }

        // Formatear coeficientes para la respuesta
        List<Double> coefList = new ArrayList<>();
        for (double c : coeficientes) {
            coefList.add(Math.round(c * 10000.0) / 10000.0);
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", true);
        respuesta.put("ecuacion", ecuacion.toString());
        respuesta.put("coeficientes", coefList);
        respuesta.put("r2", Math.round(r2 * 10000.0) / 10000.0);

        return respuesta;
    }

    private double[] resolverGauss(double[][] A, double[] b, int n) {
        double[][] Ab = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, Ab[i], 0, n);
            Ab[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            // Pivoteo parcial si el pivote actual es muy pequeño
            if (Math.abs(Ab[i][i]) < 1e-12) {
                for (int k = i + 1; k < n; k++) {
                    if (Math.abs(Ab[k][i]) > Math.abs(Ab[i][i])) {
                        double[] temp = Ab[i];
                        Ab[i] = Ab[k];
                        Ab[k] = temp;
                        break;
                    }
                }
            }

            for (int k = i + 1; k < n; k++) {
                double factor = Ab[k][i] / Ab[i][i];
                for (int j = i; j <= n; j++) {
                    Ab[k][j] -= factor * Ab[i][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double suma = 0;
            for (int j = i + 1; j < n; j++) {
                suma += Ab[i][j] * x[j];
            }
            x[i] = (Ab[i][n] - suma) / Ab[i][i];
        }
        return x;
    }

    /**
     * Extrae de forma segura una lista de números desde un objeto que puede ser List<?> o String
     */
    @SuppressWarnings("unchecked")
    private List<Double> extraerListaPuntos(Object objetoPuntos) {
        if (objetoPuntos == null) {
            return new ArrayList<>();
        }

        if (objetoPuntos instanceof List<?>) {
            List<?> lista = (List<?>) objetoPuntos;
            List<Double> resultado = new ArrayList<>();
            for (Object item : lista) {
                if (item instanceof Number) {
                    resultado.add(((Number) item).doubleValue());
                } else if (item instanceof String) {
                    resultado.add(Double.parseDouble(((String) item).trim()));
                }
            }
            return resultado;
        }

        if (objetoPuntos instanceof String) {
            String str = (String) objetoPuntos;
            if (str.isBlank()) return new ArrayList<>();

            return Arrays.stream(str.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}