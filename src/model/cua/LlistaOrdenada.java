package model.cua;

import model.NodeHuffman;

import java.util.LinkedList;

public class LlistaOrdenada implements CuaPrioritat {
    private final LinkedList<NodeHuffman> llista = new LinkedList<>();

    @Override
    public void afegir(NodeHuffman node) {
        int i = 0;
        while (i < llista.size() && llista.get(i).compareTo(node) <= 0) {
            i++;
        }
        llista.add(i, node); // inserció ordenada
    }

    @Override
    public NodeHuffman extreure() {
        return llista.removeFirst(); // mínim sempre a l’inici
    }

    @Override
    public int mida() {
        return llista.size();
    }

    @Override
    public boolean esBuida() {
        return llista.isEmpty();
    }
}
