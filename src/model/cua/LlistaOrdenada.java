package model.cua;

import java.util.LinkedList;
import model.huffman.NodeHuffman;

/**
 * Implementació d'una cua de prioritats mitjançant una llista ordenada.
 * Cada vegada que s'afegeix un node, s'insereix en la posició correcta
 * per mantenir l'ordre creixent segons la freqüència.
 *
 * Aquesta implementació té un cost:
 * - afegir: O(n)
 * - extreure (mínim): O(1)
 * - mida, esBuida: O(1)
 *
 * @author tonitorres
 */
public class LlistaOrdenada implements CuaPrioritat {

    private final LinkedList<NodeHuffman> llista = new LinkedList<>();

    /**
     * Afegeix un node a la llista mantenint l'ordre creixent.
     *
     * @param node el node Huffman a inserir
     */
    @Override
    public void afegir(NodeHuffman node) {
        int i = 0;
        while (i < llista.size() && llista.get(i).compareTo(node) <= 0) {
            i++;
        }
        llista.add(i, node);
    }

    /**
     * Extreu el node amb la menor freqüència (sempre el primer).
     *
     * @return el node amb freqüència mínima
     */
    @Override
    public NodeHuffman extreure() {
        return llista.removeFirst();
    }

    /**
     * Retorna el nombre d'elements actuals a la cua.
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
