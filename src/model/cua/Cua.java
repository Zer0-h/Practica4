package model.cua;

import model.NodeHuffman;

public interface Cua {
    void afegir(NodeHuffman node);
    NodeHuffman extreure();
    int mida();
    boolean esBuida();
}
