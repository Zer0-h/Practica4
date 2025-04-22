package model.cua;

import java.util.ArrayList;
import model.huffman.NodeHuffman;

/**
 * Implementació d'una cua de prioritats mitjançant una llista no ordenada.
 * L'afegiment de nous elements es fa al final de la llista, i l'extracció
 * selecciona el node amb menor freqüència mitjançant una cerca lineal.
 *
 * Aquesta implementació té un cost:
 * - afegir: O(1)
 * - extreure (mínim): O(n)
 * - mida, esBuida: O(1)
 *
 * @author tonitorres
 */
public class LlistaNoOrdenada implements CuaPrioritat {

    private final ArrayList<NodeHuffman> llista = new ArrayList<>();

    /**
     * Afegeix un node al final de la llista.
     *
     * @param node el node Huffman a afegir
     */
    @Override
    public void afegir(NodeHuffman node) {
        llista.add(node);
    }

    /**
     * Extreu el node amb la freqüència mínima.
     * Es fa una cerca lineal per trobar el mínim.
     *
     * @return el node Huffman amb la menor freqüència, o null si està buida
     */
    @Override
    public NodeHuffman extreure() {
        if (llista.isEmpty()) {
            return null;
        }

        int indexMinim = 0;
        for (int i = 1; i < llista.size(); i++) {
            if (llista.get(i).compareTo(llista.get(indexMinim)) < 0) {
                indexMinim = i;
            }
        }

        return llista.remove(indexMinim);
    }

    /**
     * Retorna la mida actual de la llista.
     */
    @Override
    public int mida() {
        return llista.size();
    }

    /**
     * Indica si la cua és buida.
     */
    @Override
    public boolean esBuida() {
        return llista.isEmpty();
    }
}
