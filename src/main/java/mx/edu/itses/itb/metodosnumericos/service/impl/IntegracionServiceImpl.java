package mx.edu.itses.itb.metodosnumericos.service.impl;

import mx.edu.itses.itb.metodosnumericos.dto.request.IntegracionRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.IntegracionService;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IntegracionServiceImpl implements IntegracionService {

    @Override
    public Map<String, Object> calcularIntegracion(IntegracionRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La petición no puede ser nula.");
        }

        String funcion = request.getFuncion();
        Double a = request.getA();
        Double b = request.getB();
        Integer n = request.getN() != null ? request.getN() : 1;
        String metodo = request.getMetodo() != null ? request.getMetodo().toUpperCase() : "TRAPECIO";

        if (funcion == null || funcion.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar una función f(x).");
        }
        if (a == null || b == null) {
            throw new IllegalArgumentException("Debe especificar los límites de integración a y b.");
        }
        if (a >= b) {
            throw new IllegalArgumentException("El límite inferior 'a' debe ser menor que 'b'.");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("El número de subintervalos 'n' debe ser mayor a 0.");
        }

        double h = (b - a) / n;
        double resultado = 0.0;
        String detalleMetodo = "";

        switch (metodo) {
            case "SIMPSON_13":
                if (n % 2 != 0) {
                    throw new IllegalArgumentException("Para la Regla de Simpson 1/3, el número de subintervalos (n) debe ser PAR.");
                }
                resultado = calcularSimpson13(funcion, a, b, n, h);
                detalleMetodo = "Regla de Simpson 1/3 Compuesta (n = " + n + ", h = " + String.format("%.4f", h) + ")";
                break;

            case "SIMPSON_38":
                if (n % 3 != 0) {
                    throw new IllegalArgumentException("Para la Regla de Simpson 3/8, el número de subintervalos (n) debe ser MÚLTIPLO DE 3.");
                }
                resultado = calcularSimpson38(funcion, a, b, n, h);
                detalleMetodo = "Regla de Simpson 3/8 Compuesta (n = " + n + ", h = " + String.format("%.4f", h) + ")";
                break;

            case "TRAPECIO":
            default:
                resultado = calcularTrapecio(funcion, a, b, n, h);
                detalleMetodo = "Regla del Trapecio " + (n == 1 ? "Simple" : "Compuesta (n = " + n + ", h = " + String.format("%.4f", h) + ")");
                break;
        }

        double resultadoRedondeado = Math.round(resultado * 1000000.0) / 1000000.0;

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", true);
        respuesta.put("resultado", resultadoRedondeado);
        respuesta.put("h", Math.round(h * 100000.0) / 100000.0);
        respuesta.put("detalleMetodo", detalleMetodo);

        return respuesta;
    }

    private double calcularTrapecio(String funcion, double a, double b, int n, double h) {
        double suma = evaluar(funcion, a) + evaluar(funcion, b);
        for (int i = 1; i < n; i++) {
            suma += 2 * evaluar(funcion, a + i * h);
        }
        return (h / 2.0) * suma;
    }

    private double calcularSimpson13(String funcion, double a, double b, int n, double h) {
        double suma = evaluar(funcion, a) + evaluar(funcion, b);
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            if (i % 2 == 0) {
                suma += 2 * evaluar(funcion, x);
            } else {
                suma += 4 * evaluar(funcion, x);
            }
        }
        return (h / 3.0) * suma;
    }

    private double calcularSimpson38(String funcion, double a, double b, int n, double h) {
        double suma = evaluar(funcion, a) + evaluar(funcion, b);
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            if (i % 3 == 0) {
                suma += 2 * evaluar(funcion, x);
            } else {
                suma += 3 * evaluar(funcion, x);
            }
        }
        return (3.0 * h / 8.0) * suma;
    }

    private double evaluar(String expresion, double xVal) {
        Argument arg = new Argument("x = " + xVal);
        Expression expr = new Expression(expresion, arg);
        double res = expr.calculate();

        if (Double.isNaN(res) || Double.isInfinite(res)) {
            throw new IllegalArgumentException("Error al evaluar f(" + xVal + "). Revisa la sintaxis.");
        }
        return res;
    }
}