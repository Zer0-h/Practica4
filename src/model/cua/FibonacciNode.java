package model.cua;

import model.huffman.NodeHuffman;

/**
 * Classe que representa un node dins d'un Heap de Fibonacci.
 * Cada node conté una referència al seu valor (NodeHuffman), així com
 * els seus fills, pare, germans i metadades com el grau i la marca.
 *
 * @author tonitorres
 */
public class FibonacciNode {

    private final NodeHuffman valor;
    private int grau;
    private boolean marca;
    private FibonacciNode pare;
    private FibonacciNode fill;
    private FibonacciNode esquerra;
    private FibonacciNode dreta;

    /**
     * Constructor que inicialitza un node individual amb enllaços circulars.
     *
     * @param n node Huffman a encapsular
     */
    public FibonacciNode(NodeHuffman n) {
        valor = n;
        grau = 0;
        marca = false;
        pare = null;
        fill = null;
        esquerra = this;
        dreta = this;
    }

    /**
     * Retorna el valor Huffman del node.
     */
    public NodeHuffman getValor() {
        return valor;
    }

    /**
     * Retorna la freqüència associada al valor Huffman.
     */
    public int getFrequencia() {
        return valor.getFrequencia();
    }

    public int getGrau() {
        return grau;
    }

    public void setGrau(int value) {
        grau = value;
    }

    public boolean isMarca() {
        return marca;
    }

    public void setMarca(boolean value) {
        marca = value;
    }

    public FibonacciNode getPare() {
        return pare;
    }

    public void setPare(FibonacciNode value) {
        pare = value;
    }

    public FibonacciNode getFill() {
        return fill;
    }

    public void setFill(FibonacciNode value) {
        fill = value;
    }

    public FibonacciNode getEsquerra() {
        return esquerra;
    }

    public void setEsquerra(FibonacciNode value) {
        esquerra = value;
    }

    public FibonacciNode getDreta() {
        return dreta;
    }

    public void setDreta(FibonacciNode value) {
        dreta = value;
    }
}
