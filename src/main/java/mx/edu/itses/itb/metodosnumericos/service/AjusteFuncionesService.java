package mx.edu.itses.itb.metodosnumericos.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AjusteFuncionesService {

    // 1. INTERPOLACIÓN: DIFERENCIAS DIVIDIDAS DE NEWTON
    public Map<String, Object> interpolaciónNewton(double[] x, double[] y, double xEval) {
        int n = x.length;
        double[][] tabla = new double[n][n];

        for (int i = 0; i < n; i++) {
            tabla[i][0] = y[i];
        }

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                tabla[i][j] = (tabla[i + 1][j - 1] - tabla[i][j - 1]) / (x[i + j] - x[i]);
            }
        }

        double resultado = tabla[0][0];
        double termino = 1.0;
        StringBuilder polinomio = new StringBuilder(String.format("%.4f", tabla[0][0]));

        for (int i = 1; i < n; i++) {
            termino *= (xEval - x[i - 1]);
            resultado += tabla[0][i] * termino;

            if (tabla[0][i] >= 0) polinomio.append(" + ");
            else polinomio.append(" ");
            polinomio.append(String.format("%.4f", tabla[0][i]));
            for (int k = 0; k < i; k++) {
                polinomio.append(String.format("(x - %.2f)", x[k]));
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("polinomio", polinomio.toString());
        res.put("valorEvaluado", resultado);
        return res;
    }

    // 2. INTERPOLACIÓN: LAGRANGE
    public Map<String, Object> interpolaciónLagrange(double[] x, double[] y, double xEval) {
        int n = x.length;
        double resultado = 0.0;
        StringBuilder polinomio = new StringBuilder();

        for (int i = 0; i < n; i++) {
            double Li = 1.0;
            StringBuilder LiStr = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Li *= (xEval - x[j]) / (x[i] - x[j]);
                    LiStr.append(String.format("[(x - %.2f)/(%.2f - %.2f)]", x[j], x[i], x[j]));
                }
            }
            resultado += Li * y[i];
            if (i > 0 && y[i] >= 0) polinomio.append(" + ");
            polinomio.append(String.format("%.4f * %s", y[i], LiStr.toString()));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("polinomio", polinomio.toString());
        res.put("valorEvaluado", resultado);
        return res;
    }

    // 3. REGRESIÓN LINEAL SIMPLE
    public Map<String, Object> regresionLineal(double[] x, double[] y) {
        int n = x.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
        }

        double a1 = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double a0 = (sumY - a1 * sumX) / n;

        Map<String, Object> res = new HashMap<>();
        res.put("ecuacion", String.format("y = %.4f + %.4fx", a0, a1));
        res.put("a0", a0);
        res.put("a1", a1);
        return res;
    }
}