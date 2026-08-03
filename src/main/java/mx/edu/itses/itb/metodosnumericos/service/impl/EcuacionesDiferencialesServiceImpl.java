package mx.edu.itses.itb.metodosnumericos.service.impl;

import mx.edu.itses.itb.metodosnumericos.dto.request.EcuacionesDiferencialesRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.EcuacionesDiferencialesService;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EcuacionesDiferencialesServiceImpl implements EcuacionesDiferencialesService {

    @Override
    public Map<String, Object> resolverEDO(EcuacionesDiferencialesRequestDTO request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String exprStr = request.getEcuacion();
            double x0 = request.getX0();
            double y0 = request.getY0();
            double xf = request.getXf();
            double h = request.getH();
            String metodo = request.getMetodo() != null ? request.getMetodo().toUpperCase() : "EULER";

            if (h <= 0) {
                throw new IllegalArgumentException("El tamaño de paso (h) debe ser mayor a 0.");
            }

            List<Map<String, Object>> tabla = new ArrayList<>();
            double x = x0;
            double y = y0;
            int paso = 0;

            Map<String, Object> filaInicial = new HashMap<>();
            filaInicial.put("paso", paso);
            filaInicial.put("x", Math.round(x * 10000.0) / 10000.0);
            filaInicial.put("y", Math.round(y * 100000.0) / 100000.0);
            tabla.add(filaInicial);

            while (x < xf - (h / 10.0)) {
                paso++;
                if (metodo.contains("MEJORADO") || metodo.contains("HEUN")) {
                    double k1 = evaluar(exprStr, x, y);
                    double yPred = y + h * k1;
                    double k2 = evaluar(exprStr, x + h, yPred);
                    y = y + (h / 2.0) * (k1 + k2);
                } else if (metodo.contains("RK4") || metodo.contains("RUNGE")) {
                    double k1 = evaluar(exprStr, x, y);
                    double k2 = evaluar(exprStr, x + h / 2.0, y + (h * k1) / 2.0);
                    double k3 = evaluar(exprStr, x + h / 2.0, y + (h * k2) / 2.0);
                    double k4 = evaluar(exprStr, x + h, y + h * k3);
                    y = y + (h / 6.0) * (k1 + 2 * k2 + 2 * k3 + k4);
                } else {
                    double fxy = evaluar(exprStr, x, y);
                    y = y + h * fxy;
                }

                x = x + h;

                Map<String, Object> fila = new HashMap<>();
                fila.put("paso", paso);
                fila.put("x", Math.round(x * 10000.0) / 10000.0);
                fila.put("y", Math.round(y * 100000.0) / 100000.0);
                tabla.add(fila);
            }

            response.put("exito", true);
            response.put("resultadoFinal", Math.round(y * 100000.0) / 100000.0);
            response.put("metodo", metodo);
            response.put("tabla", tabla);

        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al calcular: " + e.getMessage());
        }

        return response;
    }

    private double evaluar(String funcion, double xVal, double yVal) {
        Argument x = new Argument("x = " + xVal);
        Argument y = new Argument("y = " + yVal);
        Expression expr = new Expression(funcion, x, y);
        return expr.calculate();
    }
}