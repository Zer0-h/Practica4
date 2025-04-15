package model;

public class NodeHuffman implements Comparable<NodeHuffman> {
    private final char simbol;
    private final int frequencia;
    private NodeHuffman esquerra;
    private NodeHuffman dreta;

    public NodeHuffman(char simbol, int frequencia) {
        this.simbol = simbol;
        this.frequencia = frequencia;
        this.esquerra = null;
        this.dreta = null;
    }

    public void setNodeEsquerra(NodeHuffman node) {
        esquerra = node;
    }

    public void setNodeDreta(NodeHuffman node) {
        dreta = node;
    }

    public boolean esFulla() {
        return esquerra == null && dreta == null;
    }

    public char getSimbol(){
        return simbol;
    }

    public int getFrequencia(){
        return frequencia;
    }

    public NodeHuffman getNodeEsquerra(){
        return esquerra;
    }

    public NodeHuffman getNodeDreta(){
        return dreta;
    }

    @Override
    public int compareTo(NodeHuffman altre) {
        if (getFrequencia() > altre.getFrequencia()) {
            return 1;
        } else if(getFrequencia() < altre.getFrequencia()) {
            return -1;
        } else {
            return 0;
        }
    }
}
