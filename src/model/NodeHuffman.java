package model;

public class NodeHuffman implements Comparable<NodeHuffman> {
    private char simbol;
    private int frequencia;
    private NodeHuffman esquerra;
    private NodeHuffman dreta;

    public NodeHuffman(char s, int f) {
        simbol = s;
        frequencia = f;
        esquerra = null;
        dreta = null;
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

    public void setSimbol(char value){
        simbol = value;
    }

    public int getFrequencia(){
        return frequencia;
    }

    public void setFrequencia(int value) {
        frequencia = value;
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
