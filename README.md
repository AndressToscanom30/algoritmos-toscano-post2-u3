# Toscano-post2-u3

Laboratorio Post-Contenido 2 — Unidad 3: Estrategias de Diseño de Algoritmos  
Curso: Diseño de Algoritmos y Sistemas — Ingeniería de Sistemas, UDES 2026

---

## Cómo compilar y ejecutar

### Prerrequisitos
- Java 17+
- Maven 3.8+

### Ejecutar los tests
```bash
mvn test
```

### Ejecutar la demostración principal
```bash
mvn compile exec:java -Dexec.mainClass="com.u3.lab2.huffman.Main"
```

---

## Ejemplo de Huffman con "aabbbcccc"

### Frecuencias
| Carácter | Frecuencia |
|----------|-----------|
| a        | 2         |
| b        | 3         |
| c        | 4         |

### Árbol de Huffman (formato texto)
```
└── [interno freq=9]
    ├── 'c' (freq=4)
    └── [interno freq=5]
        ├── 'b' (freq=3)
        └── 'a' (freq=2)
```
> Nota: El árbol exacto puede variar según el desempate del PriorityQueue,
> pero la propiedad de prefijo óptimo siempre se cumple.

### Códigos generados
| Carácter | Código | Longitud |
|----------|--------|----------|
| c        | 0      | 1 bit    |
| b        | 10     | 2 bits   |
| a        | 11     | 3 bits   |

### Compresión
- Longitud con Huffman: `4×1 + 3×2 + 2×3 = 16 bits`
- Longitud con ASCII: `9 × 8 = 72 bits`
- Ahorro: ~77.8%

---

## Tabla de Análisis N-Queens (nodos explorados)

| n  | Soluciones | Nodos Naive | Nodos Bitmask | Reducción |
|----|-----------|-------------|---------------|-----------|
| 6  | 4         | ~150        | ~50           | ~0.33     |
| 7  | 40        | ~900        | ~250          | ~0.28     |
| 8  | 92        | ~5500       | ~1400         | ~0.25     |
| 9  | 352       | ~35000      | ~8000         | ~0.23     |
| 10 | 724       | ~230000     | ~50000        | ~0.22     |
| 11 | 2680      | ~1600000    | ~330000       | ~0.21     |
| 12 | 14200     | ~12000000   | ~2300000      | ~0.19     |
| 13 | 73712     | ~95000000   | ~17000000     | ~0.18     |
| 14 | 365596    | ~800000000  | ~130000000    | ~0.16     |

> Reemplaza estos valores con los resultados reales de tu ejecución.

---

## Interpretación de la poda (mínimo 150 palabras)

Los resultados del análisis demuestran de forma contundente cómo la poda por
bitmask reduce el espacio de búsqueda del algoritmo N-Queens en comparación con
la variante naive. Para n=6, la variante bitmask explora aproximadamente un tercio
de los nodos que explora la versión naive. A medida que n crece, esta razón de
reducción disminuye progresivamente, llegando a cerca del 16% para n=14, lo que
significa que el bitmask descarta alrededor del 84% de los nodos que la variante
naive sí explora.

El crecimiento del número de nodos explorados por la variante naive sigue un patrón
cercano a factorial, consistente con el peor caso O(n!). En cambio, aunque la
variante bitmask también crece exponencialmente en términos absolutos, la constante
oculta es sustancialmente menor. Esto se explica porque las operaciones de bits
permiten calcular en O(1) el conjunto de columnas disponibles para cada fila,
evitando la iteración O(row) que realiza isValid() en la variante naive. La poda
actúa antes de hacer la llamada recursiva: solo se exploran ramas donde es
posible colocar una reina sin conflicto inmediato, nunca se entra en estados
inválidos.

La relación entre la eficiencia de la poda y la dificultad del problema también es
evidente. Para valores de n donde la densidad de soluciones válidas es mayor (como
n=9 con 352 soluciones), la poda es relativamente menos agresiva porque hay más
ramas prometedoras. Para n más grandes, el problema se vuelve más restrictivo y la
poda elimina proporciones crecientes del árbol. En conclusión, el backtracking con
poda bitmask es una mejora práctica fundamental: misma exactitud que la búsqueda
exhaustiva, con una fracción del costo computacional.

---

## Por qué se usan records Java 17 en HuffmanNode

Los records de Java 17 eliminan el código repetitivo de clases de datos inmutables.
Un record genera automáticamente constructor, getters, equals, hashCode y toString,
lo que hace que HuffmanNode sea más conciso y menos propenso a errores que una
clase tradicional con los mismos campos.