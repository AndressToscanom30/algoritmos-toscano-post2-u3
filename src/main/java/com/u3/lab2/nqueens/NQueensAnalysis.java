package com.u3.lab2.nqueens;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Análisis comparativo de nodos explorados entre la variante naive y bitmask
 * del problema N-Queens. Instrumenta ambas variantes con un contador atómico
 * que se incrementa al inicio de cada llamada recursiva.
 *
 * <p>El objetivo es cuantificar empíricamente la reducción del espacio de
 * búsqueda producida por la poda bitmask frente a la verificación iterativa.</p>
 */
public class NQueensAnalysis {

    /**
     * Resultado del análisis para un valor de n dado.
     *
     * @param n             tamaño del tablero
     * @param solutions     número de soluciones encontradas
     * @param nodesNaive    nodos explorados por la variante naive
     * @param nodesBitmask  nodos explorados por la variante bitmask
     */
    public record Result(int n, long solutions,
                         long nodesNaive, long nodesBitmask) {

        /**
         * Razón de reducción: qué fracción de nodos explora bitmask respecto a naive.
         * Un valor de 0.3 significa que bitmask explora 30% de los nodos que explora naive.
         *
         * @return nodesBitmask / nodesNaive
         */
        public double reductionRatio() {
            return nodesNaive == 0 ? 1.0 : (double) nodesBitmask / nodesNaive;
        }
    }

    /**
     * Ejecuta ambas variantes instrumentadas para el tablero n×n y retorna el resultado.
     *
     * <p>Complejidad: igual que cada variante por separado, con overhead mínimo
     * del AtomicLong (operación atómica en cada nodo).</p>
     *
     * @param n tamaño del tablero (n >= 1)
     * @pre  n >= 1
     * @post retorna un Result con conteos exactos de nodos explorados por cada variante
     * @return Result con n, solutions, nodesNaive y nodesBitmask
     */
    public static Result analyze(int n) {
        AtomicLong nodesN = new AtomicLong(0);
        AtomicLong nodesB = new AtomicLong(0);

        long sols = countNaive(n, 0, new int[n], nodesN);
        countBitmask(n, 0, 0, 0, 0, nodesB);

        return new Result(n, sols, nodesN.get(), nodesB.get());
    }

    /**
     * Variante naive instrumentada: cuenta nodos explorados con AtomicLong.
     * Cada llamada recursiva (nodo del árbol de búsqueda) incrementa el contador.
     */
    private static long countNaive(int n, int row, int[] cols, AtomicLong nodes) {
        nodes.incrementAndGet(); // contar este nodo
        if (row == n) return 1;
        long count = 0;
        for (int col = 0; col < n; col++) {
            if (isValid(cols, row, col)) {
                cols[row] = col;
                count += countNaive(n, row + 1, cols, nodes);
            }
        }
        return count;
    }

    /**
     * Verifica si (row, col) es una posición válida para colocar una reina.
     */
    private static boolean isValid(int[] cols, int row, int col) {
        for (int r = 0; r < row; r++) {
            if (cols[r] == col || Math.abs(cols[r] - col) == row - r) return false;
        }
        return true;
    }

    /**
     * Variante bitmask instrumentada: cuenta nodos explorados con AtomicLong.
     * Cada llamada recursiva incrementa el contador antes de procesar la fila.
     */
    private static long countBitmask(int n, int row, int cols,
                                      int diag1, int diag2, AtomicLong nodes) {
        nodes.incrementAndGet(); // contar este nodo
        if (row == n) return 1;
        long count = 0;
        // Columnas disponibles en esta fila (O(1) con operaciones de bits)
        int available = ((1 << n) - 1) & ~(cols | diag1 | diag2);
        while (available != 0) {
            int bit = available & (-available); // bit menos significativo
            available -= bit;
            count += countBitmask(n, row + 1,
                    cols  | bit,
                    (diag1 | bit) << 1,
                    (diag2 | bit) >> 1,
                    nodes);
        }
        return count;
    }
}