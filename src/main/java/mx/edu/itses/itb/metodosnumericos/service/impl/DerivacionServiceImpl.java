package mx.edu.itses.itb.metodosnumericos.service.impl;

import mx.edu.itses.itb.metodosnumericos.dto.request.DerivacionRequestDTO;
import mx.edu.itses.itb.metodosnumericos.service.DerivacionService;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DerivacionServiceImpl implements DerivacionService {

    @Override
    public Map<String, Object> calcularDerivada(DerivacionRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La petición no puede ser nula.");
        }

        String funcion = request.getFuncion();
        Double x = request.getX();
        Double h = request.getH();
        String tipo = request.getTipoDiferencia() != null ? request.getTipoDiferencia().toUpperCase() : "CENTRADA";
        int orden = request.getOrdenDerivada() != null ? request.getOrdenDerivada() : 1;

        if (funcion == null || funcion.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar una función matemática.");
        }
        if (x == null) {
            throw new IllegalArgumentException("Debe proporcionar el punto X a evaluar.");
        }
        if (h == null || h <= 0) {
            throw new IllegalArgumentException("El tamaño de paso h debe ser positivo y mayor a 0.");
        }

        // Evaluar la función en los puntos necesarios
        double fx = evaluar(funcion, x);
        double fx_plus_h = evaluar(funcion, x + h);
        double fx_minus_h = evaluar(funcion, x - h);
        double fx_plus_2h = evaluar(funcion, x + (2 * h));
        double fx_minus_2h = evaluar(funcion, x - (2 * h));

        double resultado = 0.0;
        String formulaUsada = "";

        if (orden == 1) {
            switch (tipo) {
                case "ADELANTE":
                    resultado = (fx_plus_h - fx) / h;
                    formulaUsada = "f'(x) ≈ [f(x + h) - f(x)] / h";
                    break;
                case "ATRAS":
                    resultado = (fx - fx_minus_h) / h;
                    formulaUsada = "f'(x) ≈ [f(x) - f(x - h)] / h";
                    break;
                case "CENTRADA":
                default:
                    resultado = (fx_plus_h - fx_minus_h) / (2 * h);
                    formulaUsada = "f'(x) ≈ [f(x + h) - f(x - h)] / (2h)";
                    break;
            }
        } else if (orden == 2) {
            switch (tipo) {
                case "ADELANTE":
                    resultado = (fx_plus_2h - 2 * fx_plus_h + fx) / (h * h);
                    formulaUsada = "f''(x) ≈ [f(x + 2h) - 2f(x + h) + f(x)] / h²";
                    break;
                case "ATRAS":
                    resultado = (fx - 2 * fx_minus_h + fx_minus_2h) / (h * h);
                    formulaUsada = "f''(x) ≈ [f(x) - 2f(x - h) + f(x - 2h)] / h²";
                    break;
                case "CENTRADA":
                default:
                    resultado = (fx_plus_h - 2 * fx + fx_minus_h) / (h * h);
                    formulaUsada = "f''(x) ≈ [f(x + h) - 2f(x) + f(x - h)] / h²";
                    break;
            }
        } else {
            throw new IllegalArgumentException("Sólo se soporta 1ª y 2ª derivada.");
        }

        double resultadoRedondeado = Math.round(resultado * 1000000.0) / 1000000.0;

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", true);
        respuesta.put("resultado", resultadoRedondeado);
        respuesta.put("formula", formulaUsada);
        respuesta.put("evaluacionFx", Math.round(fx * 100000.0) / 100000.0);

        return respuesta;
    }

    private double evaluar(String expresion, double xVal) {
        Argument arg = new Argument("x = " + xVal);
        Expression expr = new Expression(expresion, arg);
        double res = expr.calculate();

        if (Double.isNaN(res) || Double.isInfinite(res)) {
            throw new IllegalArgumentException("Error al evaluar la función en x = " + xVal + ". Revisa la sintaxis.");
        }
        return res;
    }
}