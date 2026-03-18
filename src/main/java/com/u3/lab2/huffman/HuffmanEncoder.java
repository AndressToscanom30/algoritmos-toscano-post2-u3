package com.u3.lab2.huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Construcción del árbol de Huffman y generación de códigos binarios de prefijo.
 *
 * <p>El algoritmo de Huffman es Greedy: en cada paso selecciona los dos nodos
 * de menor frecuencia del min-heap y los combina en un nodo interno nuevo.
 * Esta elección localmente óptima produce globalmente el árbol de prefijo
 * de longitud de codificación mínima (demostración por intercambio de argumentos).</p>
 */
public class HuffmanEncoder {

    /**
     * Construye el árbol de Huffman a partir de un mapa de frecuencias.
     *
     * <p>Complejidad temporal: O(n log n), donde n = número de símbolos únicos.
     * Cada inserción y extracción del PriorityQueue cuesta O(log n),
     * y se realizan O(n) operaciones en total.</p>
     *
     * <p>Complejidad espacial: O(n) para el heap y el árbol resultante.</p>
     *
     * @param freqs mapa de carácter a frecuencia (no nulo, no vacío)
     * @pre  freqs != null y freqs.size() >= 1
     * @post retorna la raíz del árbol de Huffman óptimo
     * @return raíz del árbol de Huffman
     */
    public static HuffmanNode buildTree(Map<Character, Integer> freqs) {
        PriorityQueue<HuffmanNode> heap = new PriorityQueue<>();

        // Insertar cada símbolo como hoja en el min-heap
        freqs.forEach((c, f) ->
                heap.add(new HuffmanNode(c, f, null, null)));

        // Greedy: combinar los dos nodos de menor frecuencia hasta quedar uno
        while (heap.size() > 1) {
            HuffmanNode a = heap.poll(); // menor frecuencia
            HuffmanNode b = heap.poll(); // segunda menor frecuencia
            // Nodo interno: símbolo nulo, frecuencia = suma de hijos
            heap.add(new HuffmanNode('\0', a.freq() + b.freq(), a, b));
        }

        return heap.poll();
    }

    /**
     * Genera el mapa de carácter a código binario (cadena de '0' y '1').
     *
     * <p>Realiza un recorrido DFS del árbol: cada vez que se baja a la
     * izquierda se agrega '0' al prefijo, a la derecha se agrega '1'.
     * Al llegar a una hoja se registra el código acumulado.</p>
     *
     * <p>Complejidad temporal: O(n) donde n = número de nodos del árbol.</p>
     * <p>Complejidad espacial: O(n) para el mapa de códigos y el stack de recursión.</p>
     *
     * @param root raíz del árbol de Huffman (no nula)
     * @pre  root != null
     * @post el mapa retornado contiene un código para cada símbolo hoja;
     *       ningún código es prefijo de otro (propiedad de código de prefijo)
     * @return mapa de carácter a código binario
     */
    public static Map<Character, String> generateCodes(HuffmanNode root) {
        Map<Character, String> codes = new HashMap<>();
        // Caso especial: árbol de un solo símbolo
        if (root.isLeaf()) {
            codes.put(root.symbol(), "0");
            return codes;
        }
        traverse(root, "", codes);
        return codes;
    }

    /**
     * Recorre el árbol en DFS acumulando el prefijo binario.
     *
     * @param node   nodo actual del recorrido
     * @param prefix prefijo binario acumulado hasta este nodo
     * @param codes  mapa donde se registran los códigos de las hojas
     */
    private static void traverse(HuffmanNode node, String prefix,
                                  Map<Character, String> codes) {
        if (node.isLeaf()) {
            codes.put(node.symbol(), prefix);
            return;
        }
        traverse(node.left(),  prefix + "0", codes); // rama izquierda → '0'
        traverse(node.right(), prefix + "1", codes); // rama derecha  → '1'
    }
}