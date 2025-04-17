package vista;

import model.NodeHuffman;

public class NodeVisual {

    private NodeHuffman dada;
    private int x;
    private int y;
    private NodeVisual esquerra;
    private NodeVisual dreta;

    public NodeVisual(NodeHuffman d, int x, int y) {
        dada = d;
        this.x = x;
        this.y = y;
    }

    public NodeHuffman getDada() {
        return dada;
    }

    public void setDada(NodeHuffman value) {
        dada = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public NodeVisual getEsquerra() {
        return esquerra;
    }

    public void setEsquerra(NodeVisual value) {
        this.esquerra = value;
    }

    public NodeVisual getDreta() {
        return dreta;
    }

    public void setDreta(NodeVisual value) {
        dreta = value;
    }
}
