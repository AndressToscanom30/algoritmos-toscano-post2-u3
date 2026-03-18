package com.u3.lab2.nqueens;

/**
 * Problema N-Queens resuelto con dos variantes de Backtracking:
 * naive (verificación iterativa) y bitmask (verificación O(1) con bits).
 *
 * <p>El Backtracking explora el árbol de estados colocando una reina
 * por fila. La poda elimina ramas donde ya existe conflicto antes de
 * continuar la recursión, reduciendo drásticamente el espacio de búsqueda.</p>
 */
public class NQueens {

    // =========================================================
    // VARIANTE NAIVE: verifica conflictos iterando por filas previas
    // =========================================================

    /**
     * Cuenta todas las soluciones al problema N-Queens usando backtracking naive.
     *
     * <p>Complejidad temporal: O(n!) en el peor caso (sin poda efectiva).
     * Complejidad espacial: O(n) para el arreglo cols y el stack de recursión.</p>
     *
     * @param n tamaño del tablero (n >= 1)
     * @pre  n >= 1
     * @post retorna el número exacto de soluciones válidas para el tablero n×n
     * @return número de soluciones
     */
    public static long solveNaive(int n) {
        return naiveHelper(n, 0, new int[n]);
    }

    /**
     * Función recursiva del backtracking naive.
     * Coloca una reina en cada columna de la fila actual y verifica conflictos
     * iterando por todas las reinas ya colocadas en filas anteriores.
     *
     * @param n    tamaño del tablero
     * @param row  fila actual que se está procesando
     * @param cols cols[r] = columna donde está la reina en la fila r
     * @return número de soluciones encontradas desde este estado
     */
    private static long naiveHelper(int n, int row, int[] cols) {
        if (row == n) return 1; // caso base: todas las reinas colocadas
        long count = 0;
        for (int col = 0; col < n; col++) {
            if (isValid(cols, row, col)) {
                cols[row] = col;
                count += naiveHelper(n, row + 1, cols);
            }
        }
        return count;
    }

    /**
     * Verifica si colocar una reina en (row, col) es válido.
     * Itera por todas las filas anteriores para detectar conflictos
     * de columna o diagonal.
     *
     * <p>Complejidad: O(row) por llamada.</p>
     *
     * @param cols arreglo de columnas de reinas ya colocadas
     * @param row  fila donde se quiere colocar la nueva reina
     * @param col  columna propuesta
     * @return true si la posición es válida (sin conflictos)
     */
    private static boolean isValid(int[] cols, int row, int col) {
        for (int r = 0; r < row; r++) {
            // Conflicto de columna: misma columna
            if (cols[r] == col) return false;
            // Conflicto de diagonal: diferencia absoluta de columnas == diferencia de filas
            if (Math.abs(cols[r] - col) == row - r) return false;
        }
        return true;
    }

    // =========================================================
    // VARIANTE BITMASK: verificación O(1) con operaciones de bits
    // =========================================================

    /**
     * Cuenta todas las soluciones al problema N-Queens usando bitmask.
     *
     * <p>Cada bit en los enteros cols, diag1 y diag2 representa si una
     * columna o diagonal está ocupada. La operación AND y NOT permite
     * obtener en O(1) las columnas disponibles para la fila actual,
     * eliminando la iteración de verificación de la variante naive.</p>
     *
     * <p>Complejidad temporal: O(n!) en el peor caso, pero con constante
     * mucho menor que naive por la verificación O(1).
     * Complejidad espacial: O(n) para el stack de recursión.</p>
     *
     * @param n tamaño del tablero (1 <= n <= 30)
     * @pre  1 <= n <= 30 (límite de int para operaciones de bits)
     * @post retorna el número exacto de soluciones válidas para el tablero n×n
     * @return número de soluciones
     */
    public static long solveBitmask(int n) {
        return bitmaskHelper(n, 0, 0, 0, 0);
    }

    /**
     * Función recursiva del backtracking con bitmask.
     *
     * <p>Cómo funcionan los bitmasks:</p>
     * <ul>
     *   <li>cols: bit i activado = columna i está ocupada por alguna reina anterior</li>
     *   <li>diag1: diagonal descendente-izquierda ocupada (se desplaza << 1 al bajar fila)</li>
     *   <li>diag2: diagonal descendente-derecha ocupada (se desplaza >> 1 al bajar fila)</li>
     *   <li>available: columnas libres en la fila actual (AND con máscara de n bits)</li>
     *   <li>bit = available & (-available): extrae el bit menos significativo (una columna libre)</li>
     * </ul>
     *
     * @param n     tamaño del tablero
     * @param row   fila actual
     * @param cols  bitmask de columnas ocupadas
     * @param diag1 bitmask de diagonales principales ocupadas (↘)
     * @param diag2 bitmask de diagonales secundarias ocupadas (↙)
     * @return número de soluciones encontradas desde este estado
     */
    private static long bitmaskHelper(int n, int row,
                                       int cols, int diag1, int diag2) {
        if (row == n) return 1; // caso base: todas las filas tienen reina

        long count = 0;
        // Máscara de n bits: columnas válidas del tablero
        // OR de los tres bitmasks: columnas bloqueadas por columna o diagonal
        // NOT (~) y AND con máscara: columnas disponibles
        int available = ((1 << n) - 1) & ~(cols | diag1 | diag2);

        while (available != 0) {
            int bit = available & (-available); // extrae el bit menos significativo
            available -= bit;                   // elimina ese bit de las opciones

            count += bitmaskHelper(n, row + 1,
                    cols  | bit,           // columna ocupada
                    (diag1 | bit) << 1,    // diagonal ↘ se desplaza una fila abajo
                    (diag2 | bit) >> 1);   // diagonal ↙ se desplaza una fila abajo
        }
        return count;
    }
}