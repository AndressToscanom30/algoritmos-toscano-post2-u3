package com.u3.lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.u3.lab2.nqueens.NQueens;

/**
 * Tests JUnit 5 para NQueens.
 * Verifica que solveNaive y solveBitmask producen resultados idénticos
 * para n = 1..12 usando valores de referencia conocidos.
 */
public class NQueensTest {

    // Valores de referencia para n = 0..12
    // Fuente: OEIS A000170
    private static final long[] SOLUTIONS = {
        0,     // n=0 (no aplica)
        1,     // n=1
        0,     // n=2
        0,     // n=3
        2,     // n=4
        10,    // n=5
        4,     // n=6
        40,    // n=7
        92,    // n=8
        352,   // n=9
        724,   // n=10
        2680,  // n=11
        14200  // n=12
    };

    @Test
    void testNaiveN1() { assertEquals(1L,  NQueens.solveNaive(1)); }

    @Test
    void testNaiveN4() { assertEquals(2L,  NQueens.solveNaive(4)); }

    @Test
    void testNaiveN8() { assertEquals(92L, NQueens.solveNaive(8)); }

    @Test
    void testNaiveN12() { assertEquals(14200L, NQueens.solveNaive(12)); }

    @Test
    void testBitmaskN1() { assertEquals(1L,  NQueens.solveBitmask(1)); }

    @Test
    void testBitmaskN4() { assertEquals(2L,  NQueens.solveBitmask(4)); }

    @Test
    void testBitmaskN8() { assertEquals(92L, NQueens.solveBitmask(8)); }

    @Test
    void testBitmaskN12() { assertEquals(14200L, NQueens.solveBitmask(12)); }

    @Test
    void testAmbosProducenMismoResultadoN1a12() {
        for (int n = 1; n <= 12; n++) {
            long naive    = NQueens.solveNaive(n);
            long bitmask  = NQueens.solveBitmask(n);
            assertEquals(naive, bitmask,
                    "Discrepancia en n=" + n + ": naive=" + naive + ", bitmask=" + bitmask);
            assertEquals(SOLUTIONS[n], naive,
                    "Valor incorrecto en n=" + n);
        }
    }
}