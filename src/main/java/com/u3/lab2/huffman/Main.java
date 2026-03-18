package com.u3.lab2.huffman;

import java.util.LinkedHashMap;
import java.util.Map;

import com.u3.lab2.nqueens.NQueensAnalysis;

/**
 * Clase principal del laboratorio Post-Contenido 2.
 * Demuestra el algoritmo de Huffman y presenta la tabla de análisis de N-Queens.
 */
public class Main {

    public static void main(String[] args) {
        demoHuffman();
        System.out.println();
        demoNQueens();
    }

    // ------------------------------------------------------------------
    // Demostración de Huffman con la cadena "aabbbcccc"
    // ------------------------------------------------------------------
    private static void demoHuffman() {
        System.out.println("=== Algoritmo de Huffman ===");
        System.out.println("Texto: \"aabbbcccc\"");

        // Frecuencias: a=2, b=3, c=4
        Map<Character, Integer> freqs = new LinkedHashMap<>();
        freqs.put('a', 2);
        freqs.put('b', 3);
        freqs.put('c', 4);

        System.out.println("Frecuencias: " + freqs);

        HuffmanNode root = HuffmanEncoder.buildTree(freqs);
        Map<Character, String> codes = HuffmanEncoder.generateCodes(root);

        System.out.println("\nCódigos generados:");
        codes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> a.length() - b.length()))
                .forEach(e -> System.out.printf("  '%c' (freq=%d) -> %s (%d bits)%n",
                        e.getKey(), freqs.get(e.getKey()), e.getValue(), e.getValue().length()));

        // Calcular compresión
        String texto = "aabbbcccc";
        int huffmanBits = texto.chars()
                .map(c -> codes.get((char) c).length())
                .sum();
        int asciiBits = texto.length() * 8;

        System.out.printf("%nLongitud Huffman: %d bits%n", huffmanBits);
        System.out.printf("Longitud ASCII:   %d bits%n", asciiBits);
        System.out.printf("Ahorro:           %.1f%%%n",
                (1.0 - (double) huffmanBits / asciiBits) * 100);

        System.out.println("\nÁrbol de Huffman (formato texto):");
        printTree(root, "", true);
    }

    /**
     * Imprime el árbol de Huffman en formato de árbol de texto.
     */
    private static void printTree(HuffmanNode node, String prefix, boolean isLeft) {
        if (node == null) return;
        String connector = isLeft ? "├── " : "└── ";
        String label = node.isLeaf()
                ? String.format("'%c' (freq=%d)", node.symbol(), node.freq())
                : String.format("[interno freq=%d]", node.freq());
        System.out.println(prefix + connector + label);
        String childPrefix = prefix + (isLeft ? "│   " : "    ");
        if (!node.isLeaf()) {
            printTree(node.left(),  childPrefix, true);
            printTree(node.right(), childPrefix, false);
        }
    }

    // ------------------------------------------------------------------
    // Análisis de N-Queens: tabla de nodos explorados para n = 6..14
    // ------------------------------------------------------------------
    private static void demoNQueens() {
        System.out.println("=== Análisis N-Queens: Nodos Explorados ===");
        System.out.printf("%-4s %-12s %-15s %-15s %-12s%n",
                "n", "Soluciones", "Nodos Naive", "Nodos Bitmask", "Reducción");
        System.out.println("-".repeat(62));

        for (int n = 6; n <= 14; n++) {
            NQueensAnalysis.Result r = NQueensAnalysis.analyze(n);
            System.out.printf("%-4d %-12d %-15d %-15d %.4f%n",
                    r.n(), r.solutions(),
                    r.nodesNaive(), r.nodesBitmask(),
                    r.reductionRatio());
        }
    }
}