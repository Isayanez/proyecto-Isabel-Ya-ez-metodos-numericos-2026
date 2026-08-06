package mx.edu.itses.itb.metodosnumericos.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SistemasEcuacionesService {

    // 1. MÉTODO DE DETERMINANTES (REGLA DE CRAMER) - Sistemas NxN (máx 4x4)
    public double[] resolverDeterminantes(double[][] A, double[] B) {
        int n = B.length;
        double detA = calcularDeterminante(A, n);
        if (Math.abs(detA) < 1e-9) {
            throw new IllegalArgumentException("El sistema no tiene solución única (Determinante = 0).");
        }
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            double[][] Ai = clonarMatriz(A);
            for (int j = 0; j < n; j++) {
                Ai[j][i] = B[j];
            }
            x[i] = calcularDeterminante(Ai, n) / detA;
        }
        return x;
    }

    private double calcularDeterminante(double[][] matrix, int n) {
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        
        double det = 0;
        for (int p = 0; p < n; p++) {
            double[][] sub = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                int colIndex = 0;
                for (int j = 0; j < n; j++) {
                    if (j == p) continue;
                    sub[i - 1][colIndex] = matrix[i][j];
                    colIndex++;
                }
            }
            det += Math.pow(-1, p) * matrix[0][p] * calcularDeterminante(sub, n - 1);
        }
        return det;
    }

    // 2. ELIMINACIÓN GAUSSIANA
    public double[] resolverEliminacionGaussiana(double[][] A, double[] B) {
        int n = B.length;
        double[][] M = construirMatrizAumentada(A, B);

        for (int i = 0; i < n; i++) {
            // Pivoteo
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(M[k][i]) > Math.abs(M[maxRow][i])) maxRow = k;
            }
            double[] temp = M[i]; M[i] = M[maxRow]; M[maxRow] = temp;

            if (Math.abs(M[i][i]) < 1e-9) throw new IllegalArgumentException("El sistema no tiene solución única.");

            for (int k = i + 1; k < n; k++) {
                double factor = M[k][i] / M[i][i];
                for (int j = i; j <= n; j++) {
                    M[k][j] -= factor * M[i][j];
                }
            }
        }

        // Sustitución hacia atrás
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) sum += M[i][j] * x[j];
            x[i] = (M[i][n] - sum) / M[i][i];
        }
        return x;
    }

    // 3. GAUSS-JORDAN
    public double[] resolverGaussJordan(double[][] A, double[] B) {
        int n = B.length;
        double[][] M = construirMatrizAumentada(A, B);

        for (int i = 0; i < n; i++) {
            double pivot = M[i][i];
            if (Math.abs(pivot) < 1e-9) throw new IllegalArgumentException("Pivote nulo en Gauss-Jordan.");

            for (int j = 0; j <= n; j++) M[i][j] /= pivot;

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = M[k][i];
                    for (int j = 0; j <= n; j++) M[k][j] -= factor * M[i][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = 0; i < n; i++) x[i] = M[i][n];
        return x;
    }

    // 4. MÉTODOS ITERATIVOS (JACOBI Y GAUSS-SEIDEL)
    public List<String> resolverJacobi(double[][] A, double[] B, int maxIter, double tol) {
        int n = B.length;
        double[] x = new double[n];
        double[] xOld = new double[n];
        List<String> historial = new ArrayList<>();

        for (int iter = 1; iter <= maxIter; iter++) {
            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j) sum += A[i][j] * xOld[j];
                }
                x[i] = (B[i] - sum) / A[i][i];
            }

            double error = 0;
            for (int i = 0; i < n; i++) error = Math.max(error, Math.abs(x[i] - xOld[i]));

            historial.add(String.format("Iter %d: x1=%.4f, x2=%.4f, Error=%.6f", iter, x[0], x[1], error));

            if (error < tol) break;
            System.arraycopy(x, 0, xOld, 0, n);
        }
        return historial;
    }

    public List<String> resolverGaussSeidel(double[][] A, double[] B, int maxIter, double tol) {
        int n = B.length;
        double[] x = new double[n];
        List<String> historial = new ArrayList<>();

        for (int iter = 1; iter <= maxIter; iter++) {
            double[] xOld = x.clone();
            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j) sum += A[i][j] * x[j];
                }
                x[i] = (B[i] - sum) / A[i][i];
            }

            double error = 0;
            for (int i = 0; i < n; i++) error = Math.max(error, Math.abs(x[i] - xOld[i]));

            historial.add(String.format("Iter %d: x1=%.4f, x2=%.4f, Error=%.6f", iter, x[0], x[1], error));

            if (error < tol) break;
        }
        return historial;
    }

    // Funciones auxiliares
    private double[][] clonarMatriz(double[][] A) {
        double[][] copy = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) System.arraycopy(A[i], 0, copy[i], 0, A[i].length);
        return copy;
    }

    private double[][] construirMatrizAumentada(double[][] A, double[] B) {
        int n = B.length;
        double[][] M = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = B[i];
        }
        return M;
    }
}