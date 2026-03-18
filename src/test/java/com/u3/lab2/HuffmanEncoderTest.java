package com.u3.lab2;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.u3.lab2.huffman.HuffmanEncoder;
import com.u3.lab2.huffman.HuffmanNode;

/**
 * Tests JUnit 5 para HuffmanEncoder.
 * Verifica: construcción del árbol, propiedad de prefijo y eficiencia de compresión.
 */
public class HuffmanEncoderTest {

    /**
     * Construye el mapa de frecuencias de la cadena "aabbbcccc":
     * a=2, b=3, c=4.
     */
    private Map<Character, Integer> freqsAabbbcccc() {
        return Map.of('a', 2, 'b', 3, 'c', 4);
    }

    @Test
    void testArbolSeConstruye() {
        HuffmanNode root = HuffmanEncoder.buildTree(freqsAabbbcccc());
        assertNotNull(root);
        // La raíz debe tener frecuencia total = 2+3+4 = 9
        assertEquals(9, root.freq());
    }

    @Test
    void testCodigosGenerados() {
        HuffmanNode root = HuffmanEncoder.buildTree(freqsAabbbcccc());
        Map<Character, String> codes = HuffmanEncoder.generateCodes(root);

        // Deben existir códigos para los tres símbolos
        assertTrue(codes.containsKey('a'));
        assertTrue(codes.containsKey('b'));
        assertTrue(codes.containsKey('c'));

        // El símbolo más frecuente (c=4) debe tener el código más corto
        assertTrue(codes.get('c').length() <= codes.get('b').length());
        assertTrue(codes.get('b').length() <= codes.get('a').length());
    }

    @Test
    void testPropiedadDePrefijo() {
        HuffmanNode root = HuffmanEncoder.buildTree(freqsAabbbcccc());
        Map<Character, String> codes = HuffmanEncoder.generateCodes(root);

        // Verificar que ningún código es prefijo de otro
        for (Map.Entry<Character, String> entry1 : codes.entrySet()) {
            for (Map.Entry<Character, String> entry2 : codes.entrySet()) {
                if (!entry1.getKey().equals(entry2.getKey())) {
                    String c1 = entry1.getValue();
                    String c2 = entry2.getValue();
                    assertFalse(c1.startsWith(c2),
                            "Violación de prefijo: " + c1 + " empieza con " + c2);
                    assertFalse(c2.startsWith(c1),
                            "Violación de prefijo: " + c2 + " empieza con " + c1);
                }
            }
        }
    }

    @Test
    void testCompresionMenorQueASCII() {
        String texto = "aabbbcccc"; // 9 caracteres × 8 bits = 72 bits en ASCII
        Map<Character, Integer> freqs = Map.of('a', 2, 'b', 3, 'c', 4);

        HuffmanNode root = HuffmanEncoder.buildTree(freqs);
        Map<Character, String> codes = HuffmanEncoder.generateCodes(root);

        // Calcular longitud total con Huffman
        int totalBits = 0;
        for (char c : texto.toCharArray()) {
            totalBits += codes.get(c).length();
        }

        int asciiBits = texto.length() * 8; // 72 bits
        assertTrue(totalBits < asciiBits,
                "Huffman (" + totalBits + " bits) debe ser menor que ASCII (" + asciiBits + " bits)");
    }

    @Test
    void testArbolUnSoloSimbolo() {
        HuffmanNode root = HuffmanEncoder.buildTree(Map.of('x', 5));
        Map<Character, String> codes = HuffmanEncoder.generateCodes(root);
        assertEquals("0", codes.get('x'));
    }
}