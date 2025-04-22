package vista;

import model.huffman.NodeHuffman;

/**
 * Representa un node visual per a la representació gràfica de l'arbre de
 * Huffman.
 * Conté informació sobre la posició i els fills visuals corresponents.
 *
 * @author tonitorres
 */
public class NodeVisual {

    private NodeHuffman dada;            // Dada associada (node Huffman)
    private int x;                       // Posició X a la pantalla
    private int y;                       // Posició Y a la pantalla
    private NodeVisual esquerra;        // Fill visual esquerre
    private NodeVisual dreta;           // Fill visual dret

    /**
     * Constructor del node visual.
     *
     * @param d Node Huffman associat
     * @param x Posició X
     * @param y Posició Y
     */
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
