package com.u3.lab2.huffman;

/**
 * Nodo del árbol de Huffman implementado como record Java 17.
 *
 * <p>Las hojas representan símbolos concretos del alfabeto.
 * Los nodos internos agrupan frecuencias y tienen symbol = '\0'.</p>
 *
 * <p>Implementa Comparable para ser ordenable por frecuencia en
 * un min-heap (PriorityQueue), lo que permite construir el árbol
 * de Huffman en O(n log n).</p>
 *
 * @param symbol carácter representado (solo significativo en hojas)
 * @param freq   frecuencia acumulada del nodo
 * @param left   hijo izquierdo (null si es hoja)
 * @param right  hijo derecho (null si es hoja)
 */
public record HuffmanNode(char symbol, int freq,
                          HuffmanNode left, HuffmanNode right)
        implements Comparable<HuffmanNode> {

    /**
     * Compara nodos por frecuencia ascendente para el min-heap.
     *
     * <p>Complejidad: O(1).</p>
     *
     * @param other nodo con el que se compara
     * @return valor negativo si this.freq < other.freq, cero si igual,
     *         positivo si mayor
     */
    @Override
    public int compareTo(HuffmanNode other) {
        return Integer.compare(this.freq, other.freq);
    }

    /**
     * Indica si este nodo es una hoja del árbol.
     *
     * <p>Un nodo es hoja si y solo si no tiene hijos, es decir,
     * representa directamente un símbolo del alfabeto.</p>
     *
     * @return true si left == null y right == null
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
}