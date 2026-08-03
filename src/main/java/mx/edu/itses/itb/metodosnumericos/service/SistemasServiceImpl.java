package mx.edu.itses.itb.metodosnumericos.service.impl;

import mx.edu.itses.itb.metodosnumericos.dto.request.SistemasRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.SistemasService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SistemasServiceImpl implements SistemasService {

    @Override
    public Map<String, Object> resolverSistema(SistemasRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La petición no puede ser nula.");
        }

        Map<String, Object> respuesta = new HashMap<>();
        List<String> pasos = new ArrayList<>();

        int n = request.getDimension();
        List<List<Double>> matrizList = request.getMatrizA();
        List<Double> vectorList = request.getVectorB();

        if (matrizList == null || vectorList == null || matrizList.size() != n || vectorList.size() != n) {
            throw new IllegalArgumentException("La matriz A o el vector B no coinciden con la dimensión especificada.");
        }

        // Conversión de List<List<Double>> -> double[][]
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) {
            List<Double> fila = matrizList.get(i);
            if (fila == null || fila.size() != n) {
                throw new IllegalArgumentException("La fila " + (i + 1) + " de la matriz A es inválida.");
            }
            for (int j = 0; j < n; j++) {
                A[i][j] = fila.get(j) != null ? fila.get(j) : 0.0;
            }
        }

        // Conversión de List<Double> -> double[]
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = vectorList.get(i) != null ? vectorList.get(i) : 0.0;
        }

        // Matriz aumentada [A|b]
        double[][] Ab = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, Ab[i], 0, n);
            Ab[i][n] = b[i];
        }

        // Eliminación Gaussiana con Pivoteo
        for (int i = 0; i < n; i++) {
            // Pivoteo
            if (Math.abs(Ab[i][i]) < 1e-12) {
                for (int k = i + 1; k < n; k++) {
                    if (Math.abs(Ab[k][i]) > Math.abs(Ab[i][i])) {
                        double[] temp = Ab[i];
                        Ab[i] = Ab[k];
                        Ab[k] = temp;
                        pasos.add("Intercambio de fila " + (i + 1) + " con fila " + (k + 1) + " para evitar división por cero.");
                        break;
                    }
                }
            }

            if (Math.abs(Ab[i][i]) < 1e-12) {
                throw new ArithmeticException("El sistema no tiene solución única (pivote nulo en la posición " + (i + 1) + ").");
            }

            for (int k = i + 1; k < n; k++) {
                double factor = Ab[k][i] / Ab[i][i];
                pasos.add("Fila " + (k + 1) + " = Fila " + (k + 1) + " - (" + String.format("%.4f", factor) + ") * Fila " + (i + 1));
                for (int j = i; j <= n; j++) {
                    Ab[k][j] -= factor * Ab[i][j];
                }
            }
        }

        // Sustitución hacia atrás
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double suma = 0;
            for (int j = i + 1; j < n; j++) {
                suma += Ab[i][j] * x[j];
            }
            x[i] = (Ab[i][n] - suma) / Ab[i][i];
        }

        // Formatear solución
        List<Double> solucion = new ArrayList<>();
        for (double val : x) {
            solucion.add(Math.round(val * 10000.0) / 10000.0);
        }

        respuesta.put("exito", true);
        respuesta.put("solucion", solucion);
        respuesta.put("pasos", pasos);

        return respuesta;
    }
}