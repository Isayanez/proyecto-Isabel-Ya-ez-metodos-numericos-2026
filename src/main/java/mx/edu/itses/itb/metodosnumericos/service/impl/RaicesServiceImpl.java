package mx.edu.itses.itb.metodosnumericos.service.impl;

import mx.edu.itses.itb.metodosnumericos.dto.request.RaizRequestDTO;
import mx.edu.itses.itb.metodosnumericos.dto.response.RaizResponseDTO;
import mx.edu.itses.itb.metodosnumericos.service.RaicesService;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.stereotype.Service;

@Service
public class RaicesServiceImpl implements RaicesService {

    // Reutilizar el evaluador evita crear overhead de memoria en cada iteración
    private final ExprEvaluator evaluator = new ExprEvaluator();

    private String normalizarFuncion(String funcion) {
        if (funcion == null || funcion.isBlank()) return "0";
        return funcion.replaceAll("(?i)\\bsin\\b", "Sin")
                      .replaceAll("(?i)\\bcos\\b", "Cos")
                      .replaceAll("(?i)\\btan\\b", "Tan")
                      .replaceAll("(?i)\\bexp\\b", "Exp")
                      .replaceAll("(?i)\\bln\\b", "Log");
    }

    private double evaluarFuncion(String funcionStr, double xVal) {
        String fLimpia = normalizarFuncion(funcionStr);
        String expresion = "N(With[{x=" + xVal + "}, " + fLimpia + "])";
        IExpr result = evaluator.eval(expresion);
        return result.evalDouble();
    }

    // Se sustituye la derivada simbólica por diferencias finitas centradas
    private double evaluarDerivada(String funcionStr, double xVal) {
        double h = 1e-5; // Incremento pequeño (0.00001)
        double fPlus = evaluarFuncion(funcionStr, xVal + h);
        double fMinus = evaluarFuncion(funcionStr, xVal - h);
        return (fPlus - fMinus) / (2 * h);
    }

    @Override
    public RaizResponseDTO calcular(RaizRequestDTO request) {
        String metodo = request.getMetodo();
        if (metodo == null) {
            throw new IllegalArgumentException("El método numérico no puede ser nulo.");
        }

        // Normalizar entrada reemplazando espacios por guiones bajos
        String metodoClave = metodo.trim().toUpperCase().replace(" ", "_");

        switch (metodoClave) {
            case "REGLA_FALSA":
                return calcularReglaFalsa(request);
            case "PUNTO_FIJO":
                return calcularPuntoFijo(request);
            case "NEWTON_RAPHSON":
                return calcularNewtonRaphson(request);
            case "SECANTE":
                return calcularSecante(request);
            case "SECANTE_MODIFICADO":
                return calcularSecanteModificado(request);
            default:
                throw new IllegalArgumentException("Método no soportado: " + metodo);
        }
    }

    private RaizResponseDTO calcularReglaFalsa(RaizRequestDTO req) {
        // Se utilizan los helpers que contemplan a/b o x0/x1 indistintamente
        double a = req.getX0Oa();
        double b = req.getX1Ob();
        double tol = req.getTolerancia() != null ? req.getTolerancia() : 0.0001;
        int maxIter = req.getMaxIteraciones() != null ? req.getMaxIteraciones() : 100;
        
        double fa = evaluarFuncion(req.getFuncion(), a);
        double fb = evaluarFuncion(req.getFuncion(), b);

        if (fa * fb >= 0) {
            return RaizResponseDTO.builder()
                    .metodo("Regla Falsa")
                    .convergio(false)
                    .mensaje("Error: f(a) y f(b) deben tener signos opuestos para garantizar una raíz.")
                    .build();
        }

        double xr = a;
        double error = 100.0;
        int iter = 0;

        while (iter < maxIter) {
            iter++;
            double xrAnterior = xr;
            xr = b - (fb * (a - b)) / (fa - fb);
            double fxr = evaluarFuncion(req.getFuncion(), xr);

            if (xr != 0) {
                error = Math.abs((xr - xrAnterior) / xr) * 100.0;
            }

            if (Math.abs(fxr) < tol || (iter > 1 && error < tol)) {
                return RaizResponseDTO.builder()
                        .metodo("Regla Falsa")
                        .raiz(xr)
                        .iteraciones(iter)
                        .errorRelativo(error)
                        .convergio(true)
                        .mensaje("Éxito al calcular la raíz.")
                        .build();
            }

            if (fa * fxr < 0) {
                b = xr;
                fb = fxr;
            } else {
                a = xr;
                fa = fxr;
            }
        }

        return RaizResponseDTO.builder()
                .metodo("Regla Falsa")
                .raiz(xr)
                .iteraciones(iter)
                .errorRelativo(error)
                .convergio(false)
                .mensaje("Se alcanzó el límite máximo de iteraciones sin converger.")
                .build();
    }

    private RaizResponseDTO calcularPuntoFijo(RaizRequestDTO req) {
        double xi = req.getX0Oa();
        double tol = req.getTolerancia() != null ? req.getTolerancia() : 0.0001;
        int maxIter = req.getMaxIteraciones() != null ? req.getMaxIteraciones() : 100;
        
        double error = 100.0;
        int iter = 0;

        while (iter < maxIter) {
            iter++;
            double xNext = evaluarFuncion(req.getFuncion(), xi);
            
            if (xNext != 0) {
                error = Math.abs((xNext - xi) / xNext) * 100.0;
            }

            if (error < tol) {
                return RaizResponseDTO.builder()
                        .metodo("Iteración de Punto Fijo")
                        .raiz(xNext)
                        .iteraciones(iter)
                        .errorRelativo(error)
                        .convergio(true)
                        .mensaje("Éxito al calcular la raíz.")
                        .build();
            }
            xi = xNext;
        }

        return RaizResponseDTO.builder()
                .metodo("Iteración de Punto Fijo")
                .raiz(xi)
                .iteraciones(iter)
                .errorRelativo(error)
                .convergio(false)
                .mensaje("Límite de iteraciones alcanzado sin convergencia.")
                .build();
    }

    private RaizResponseDTO calcularNewtonRaphson(RaizRequestDTO req) {
        double xi = req.getX0Oa();
        double tol = req.getTolerancia() != null ? req.getTolerancia() : 0.0001;
        int maxIter = req.getMaxIteraciones() != null ? req.getMaxIteraciones() : 100;

        double error = 100.0;
        int iter = 0;

        while (iter < maxIter) {
            iter++;
            double fVal = evaluarFuncion(req.getFuncion(), xi);
            double fDeriv = evaluarDerivada(req.getFuncion(), xi);

            if (fDeriv == 0) {
                return RaizResponseDTO.builder()
                        .metodo("Newton Raphson")
                        .convergio(false)
                        .mensaje("Error: La derivada se volvió cero durante las iteraciones.")
                        .build();
            }

            double xNext = xi - (fVal / fDeriv);
            if (xNext != 0) {
                error = Math.abs((xNext - xi) / xNext) * 100.0;
            }

            if (error < tol) {
                return RaizResponseDTO.builder()
                        .metodo("Newton Raphson")
                        .raiz(xNext)
                        .iteraciones(iter)
                        .errorRelativo(error)
                        .convergio(true)
                        .mensaje("Éxito al calcular la raíz.")
                        .build();
            }
            xi = xNext;
        }

        return RaizResponseDTO.builder()
                .metodo("Newton Raphson")
                .raiz(xi)
                .iteraciones(iter)
                .errorRelativo(error)
                .convergio(false)
                .mensaje("Límite de iteraciones alcanzado.")
                .build();
    }

    private RaizResponseDTO calcularSecante(RaizRequestDTO req) {
        double x0 = req.getX0Oa();
        double x1 = req.getX1Ob();
        double tol = req.getTolerancia() != null ? req.getTolerancia() : 0.0001;
        int maxIter = req.getMaxIteraciones() != null ? req.getMaxIteraciones() : 100;

        double error = 100.0;
        int iter = 0;

        while (iter < maxIter) {
            iter++;
            double fx0 = evaluarFuncion(req.getFuncion(), x0);
            double fx1 = evaluarFuncion(req.getFuncion(), x1);

            double denominador = fx1 - fx0;
            if (denominador == 0) {
                return RaizResponseDTO.builder()
                        .metodo("Secante")
                        .convergio(false)
                        .mensaje("Error: División por cero (f(x1) - f(x0) = 0).")
                        .build();
            }

            double x2 = x1 - (fx1 * (x1 - x0)) / denominador;
            if (x2 != 0) {
                error = Math.abs((x2 - x1) / x2) * 100.0;
            }

            if (error < tol) {
                return RaizResponseDTO.builder()
                        .metodo("Secante")
                        .raiz(x2)
                        .iteraciones(iter)
                        .errorRelativo(error)
                        .convergio(true)
                        .mensaje("Éxito al calcular la raíz.")
                        .build();
            }

            x0 = x1;
            x1 = x2;
        }

        return RaizResponseDTO.builder()
                .metodo("Secante")
                .raiz(x1)
                .iteraciones(iter)
                .errorRelativo(error)
                .convergio(false)
                .mensaje("Límite de iteraciones alcanzado.")
                .build();
    }

    private RaizResponseDTO calcularSecanteModificado(RaizRequestDTO req) {
        double xi = req.getX0Oa();
        double delta = 0.01;
        double tol = req.getTolerancia() != null ? req.getTolerancia() : 0.0001;
        int maxIter = req.getMaxIteraciones() != null ? req.getMaxIteraciones() : 100;

        double error = 100.0;
        int iter = 0;

        while (iter < maxIter) {
            iter++;
            double dx = (xi == 0) ? delta : delta * xi;
            
            double fxi = evaluarFuncion(req.getFuncion(), xi);
            double fxiDelta = evaluarFuncion(req.getFuncion(), xi + dx);

            double denominador = fxiDelta - fxi;
            if (denominador == 0) {
                return RaizResponseDTO.builder()
                        .metodo("Secante Modificado")
                        .convergio(false)
                        .mensaje("Error: División por cero al calcular la pendiente.")
                        .build();
            }

            double xNext = xi - (dx * fxi) / denominador;
            if (xNext != 0) {
                error = Math.abs((xNext - xi) / xNext) * 100.0;
            }

            if (error < tol) {
                return RaizResponseDTO.builder()
                        .metodo("Secante Modificado")
                        .raiz(xNext)
                        .iteraciones(iter)
                        .errorRelativo(error)
                        .convergio(true)
                        .mensaje("Éxito al calcular la raíz.")
                        .build();
            }
            xi = xNext;
        }

        return RaizResponseDTO.builder()
                .metodo("Secante Modificado")
                .raiz(xi)
                .iteraciones(iter)
                .errorRelativo(error)
                .convergio(false)
                .mensaje("Límite de iteraciones alcanzado.")
                .build();
    }
}