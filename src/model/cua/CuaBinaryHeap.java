package model.cua;

import model.NodeHuffman;
import java.util.Arrays;

public class CuaBinaryHeap implements CuaPrioritat {

    private NodeHuffman[] monticle;
    private int mida;
    private int capacitat;

    public CuaBinaryHeap(int capacitatInicial) {
        capacitat = capacitatInicial;
        monticle = new NodeHuffman[capacitat];
        mida = 0;
    }

    @Override
    public void afegir(NodeHuffman node) {
        if (mida >= capacitat) {
            redimensionar();
        }

        monticle[mida] = node;
        int actual = mida;
        mida++;

        while (actual > 0 && monticle[actual].getFrequencia() < monticle[pare(actual)].getFrequencia()) {
            intercanviar(actual, pare(actual));
            actual = pare(actual);
        }
    }

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

    @Override
    public int mida() {
        return mida;
    }

    @Override
    public boolean esBuida() {
        return mida == 0;
    }

    private int pare(int i) {
        return (i - 1) / 2;
    }

    private int esquerra(int i) {
        return 2 * i + 1;
    }

    private int dreta(int i) {
        return 2 * i + 2;
    }

    private void intercanviar(int i, int j) {
        NodeHuffman temp = monticle[i];
        monticle[i] = monticle[j];
        monticle[j] = temp;
    }

    private void redimensionar() {
        capacitat *= 2;
        monticle = Arrays.copyOf(monticle, capacitat);
    }

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
