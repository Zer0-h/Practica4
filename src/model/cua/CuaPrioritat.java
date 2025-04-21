package model.cua;

import model.NodeHuffman;

public interface CuaPrioritat {
    void afegir(NodeHuffman node);
    NodeHuffman extreure();
    int mida();
    boolean esBuida();
}
