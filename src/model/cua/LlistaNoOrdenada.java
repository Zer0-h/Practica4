package model.cua;

import model.NodeHuffman;

import java.util.ArrayList;

public class LlistaNoOrdenada implements Cua {
    private final ArrayList<NodeHuffman> llista = new ArrayList<>();

    @Override
    public void afegir(NodeHuffman node) {
        llista.add(node); // inserció directa
    }

    @Override
    public NodeHuffman extreure() {
        if (llista.isEmpty()) return null;

        int indexMin = 0;
        for (int i = 1; i < llista.size(); i++) {
            if (llista.get(i).compareTo(llista.get(indexMin)) < 0) {
                indexMin = i;
            }
        }
        return llista.remove(indexMin); // eliminació del mínim
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
