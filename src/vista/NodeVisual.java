package vista;

import model.NodeHuffman;

public class NodeVisual {

    private NodeHuffman dada;
    private int x;
    private int y;
    private NodeVisual esquerra;
    private NodeVisual dreta;

    public NodeVisual(NodeHuffman dada, int x, int y) {
        this.dada = dada;
        this.x = x;
        this.y = y;
    }

    public NodeHuffman getDada() {
        return dada;
    }

    public void setDada(NodeHuffman dada) {
        this.dada = dada;
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

    public void setEsquerra(NodeVisual esquerra) {
        this.esquerra = esquerra;
    }

    public NodeVisual getDreta() {
        return dreta;
    }

    public void setDreta(NodeVisual dreta) {
        this.dreta = dreta;
    }
}
