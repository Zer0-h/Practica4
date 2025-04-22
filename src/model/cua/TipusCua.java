package model.cua;

/**
 * Enumeració que defineix els diferents tipus de cues de prioritat
 * disponibles per construir l'arbre de Huffman.
 *
 * Cada tipus proporciona una implementació diferent del comportament
 * de la cua de prioritat (cost computacional, estructura, eficiència).
 *
 * - BINARY_HEAP: heap binari mínim (eficient per inserció i extreure mínim)
 * - FIBONACCI_HEAP: heap de Fibonacci (inserció ràpida i extreure mínim
 * amortitzat)
 * - ORDENADA: llista amb inserció ordenada (extracció òptima)
 * - NO_ORDENADA: inserció ràpida, però extracció lineal
 *
 * @author tonitorres
 */
public enum TipusCua {
    BINARY_HEAP("Heap binari"),
    FIBONACCI_HEAP("Heap fibonacci"),
    ORDENADA("Llista ordenada"),
    NO_ORDENADA("Llista no ordenada");

    private final String descripcio;

    TipusCua(String d) {
        descripcio = d;
    }

    @Override
    public String toString() {
        return descripcio;
    }
}
