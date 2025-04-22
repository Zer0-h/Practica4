package model.huffman;

/**
 * Representa un node de l’arbre de Huffman.
 * Pot ser un node intern (sense símbol) o un node fulla (amb símbol).
 * Es fa servir tant per construir l’arbre com per codificar/decodificar.
 *
 * @author tonitorres
 */
public class NodeHuffman implements Comparable<NodeHuffman> {

    private char simbol;                // Símbol (caràcter) associat al node
    private int frequencia;            // Freqüència del símbol (nombre d’aparicions)
    private NodeHuffman esquerra;      // Fill esquerre
    private NodeHuffman dreta;         // Fill dret

    /**
     * Crea un nou node amb un símbol i una freqüència associada.
     *
     * @param s símbol (caràcter) representat pel node
     * @param f freqüència del símbol
     */
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

    /**
     * Indica si aquest node és una fulla (no té fills).
     */
    public boolean esFulla() {
        return esquerra == null && dreta == null;
    }

    public char getSimbol() {
        return simbol;
    }

    public void setSimbol(char value) {
        simbol = value;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int value) {
        frequencia = value;
    }

    public NodeHuffman getNodeEsquerra() {
        return esquerra;
    }

    public NodeHuffman getNodeDreta() {
        return dreta;
    }

    /**
     * Compara dos nodes segons la seva freqüència.Es fa servir per mantenir
     * l’ordre a la cua de prioritats.
     *
     * @param altre Node a comparar.
     */
    @Override
    public int compareTo(NodeHuffman altre) {
        return Integer.compare(this.getFrequencia(), altre.getFrequencia());
    }
}
