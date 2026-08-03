package mx.edu.itses.itb.metodosnumericos.util;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class EvaluadorExpresiones {

    private static final ExprEvaluator evaluator = new ExprEvaluator();

    public static double evaluar(String expresion, double valX) {
        synchronized (evaluator) {
            String exprSustituida = "ReplaceAll(" + expresion + ", x -> (" + valX + "))";
            IExpr result = evaluator.eval(exprSustituida);
            return result.evalDouble();
        }
    }

    public static String derivar(String expresion) {
        synchronized (evaluator) {
            IExpr result = evaluator.eval("D(" + expresion + ", x)");
            return result.toString();
        }
    }
}