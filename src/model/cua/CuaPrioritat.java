package model.cua;

import model.huffman.NodeHuffman;

/**
 * Interfície que defineix el comportament bàsic d'una cua de prioritat.
 * Utilitzada per construir l'arbre de Huffman amb diferents estratègies
 * d'ordre.
 *
 * @author tonitorres
 */
public interface CuaPrioritat {

    /**
     * Afegeix un nou node a la cua.
     *
     * @param node node Huffman a afegir
     */
    void afegir(NodeHuffman node);

    /**
     * Extreu el node amb la prioritat més alta (menor freqüència).
     *
     * @return node Huffman extret
     */
    NodeHuffman extreure();

    /**
     * Retorna el nombre de nodes actuals dins la cua.
     *
     * @return mida de la cua
     */
    int mida();

    /**
     * Comprova si la cua està buida.
     *
     * @return true si no hi ha cap element, false en cas contrari
     */
    boolean esBuida();
}
