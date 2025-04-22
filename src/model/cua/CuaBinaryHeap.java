package model.cua;

import java.util.Arrays;
import model.huffman.NodeHuffman;

/**
 * Implementació d'una cua de prioritat mitjançant un Heap binari mínim.
 * Els elements s'emmagatzemen en un array i es manté la propietat de heap.
 *
 * Aquesta implementació té un cost:
 * - afegir: O(log n)
 * - extreure (mínim): O(log n)
 * - mida, esBuida: O(1)
 *
 * @author tonitorres
 */
public class CuaBinaryHeap implements CuaPrioritat {

    private NodeHuffman[] monticle; // Array que representa el monticle binari
    private int mida;               // Nombre d'elements actuals
    private int capacitat;          // Capacitat màxima abans de redimensionar

    /**
     * Crea una cua amb capacitat inicial donada.
     *
     * @param capacitatInicial capacitat del monticle abans de redimensionar
     */
    public CuaBinaryHeap(int capacitatInicial) {
        capacitat = capacitatInicial;
        monticle = new NodeHuffman[capacitat];
        mida = 0;
    }

    /**
     * Afegeix un nou node a la cua de prioritat.
     *
     * @param node node Huffman a inserir
     */
    @Override
    public void afegir(NodeHuffman node) {
        if (mida >= capacitat) {
            redimensionar();
        }

        monticle[mida] = node;
        int actual = mida;
        mida++;

        // Reorganització ascendent per mantenir la propietat del heap
        while (actual > 0
                && monticle[actual].getFrequencia() < monticle[pare(actual)].getFrequencia()) {
            intercanviar(actual, pare(actual));
            actual = pare(actual);
        }
    }

    /**
     * Extreu el node amb la freqüència mínima.
     *
     * @return node amb la menor freqüència
     */
    @Override
    public NodeHuffman extreure() {
        if (mida == 0) {
            throw new RuntimeException("El monticle és buit");
        }

        NodeHuffman minim = monticle[0];
        monticle[0] = monticle[mida - 1];
        mida--;

        reorganitzarAvall(0);

        return minim;
    }

    /**
     * Retorna la mida actual de la llista.
     */
    @Override
    public int mida() {
        return mida;
    }

    /**
     * Indica si la cua és buida.
     */
    @Override
    public boolean esBuida() {
        return mida == 0;
    }

    /** Retorna l'índex del pare del node a la posició i */
    private int pare(int i) {
        return (i - 1) / 2;
    }

    /** Retorna l'índex del fill esquerre del node a la posició i */
    private int esquerra(int i) {
        return 2 * i + 1;
    }

    /** Retorna l'índex del fill dret del node a la posició i */
    private int dreta(int i) {
        return 2 * i + 2;
    }

    /** Intercanvia dues posicions del monticle */
    private void intercanviar(int i, int j) {
        NodeHuffman temp = monticle[i];
        monticle[i] = monticle[j];
        monticle[j] = temp;
    }

    /** Duplica la capacitat del monticle si s'exhaureix */
    private void redimensionar() {
        capacitat *= 2;
        monticle = Arrays.copyOf(monticle, capacitat);
    }

    /** Reorganitza el monticle cap avall per mantenir la propietat de min-heap */
    private void reorganitzarAvall(int i) {
        int menor = i;
        int esq = esquerra(i);
        int dret = dreta(i);

        if (esq < mida && monticle[esq].getFrequencia() < monticle[menor].getFrequencia()) {
            menor = esq;
        }

        if (dret < mida && monticle[dret].getFrequencia() < monticle[menor].getFrequencia()) {
            menor = dret;
        }

        if (menor != i) {
            intercanviar(i, menor);
            reorganitzarAvall(menor);
        }
    }
}
