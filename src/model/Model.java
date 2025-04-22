package model;

import model.huffman.NodeHuffman;
import java.io.File;
import model.cua.*;

/**
 * Classe que encapsula l'estat de l'aplicació: fitxers implicats en la compressió.
 */
public class Model {
    private File fitxerOriginal;
    private File fitxerComprès;
    private File fitxerDescomprès;
    private long tempsCompressioMs;
    private double longitudMitjanaCodi;
    private double taxaCompressio;
    private String missatgeInformatiu;
    private TipusCua tipusCua;

    private NodeHuffman arrelHuffman;

    public Model() {
        tipusCua = TipusCua.BINARY_HEAP;
    }

    public CuaPrioritat crearCua() {
        return switch (tipusCua) {
            case BINARY_HEAP -> new CuaBinaryHeap(16);
            case FIBONACCI_HEAP -> new CuaFibonacciHeap();
            case ORDENADA -> new LlistaOrdenada();
            case NO_ORDENADA -> new LlistaNoOrdenada();
            default -> new CuaBinaryHeap(16);
        };
    }

    public void setTipusCua(TipusCua value) {
        tipusCua = value;
    }

    public long getTempsCompressioMs() {
        return tempsCompressioMs;
    }

    public void setTempsCompressioMs(long value) {
        tempsCompressioMs = value;
    }

    public double getLongitudMitjanaCodi() {
        return longitudMitjanaCodi;
    }

    public void setLongitudMitjanaCodi(double value) {
        longitudMitjanaCodi = value;
    }

    public double getTaxaCompressio() {
        return taxaCompressio;
    }

    public void setTaxaCompressio(double value) {
        taxaCompressio = value;
    }

    public File getFitxerOriginal() {
        return fitxerOriginal;
    }

    public void setFitxerOriginal(File value) {
        fitxerOriginal = value;
    }

    public File getFitxerComprès() {
        return fitxerComprès;
    }

    public void setFitxerComprès(File value) {
        fitxerComprès = value;
    }

    public File getFitxerDescomprès() {
        return fitxerDescomprès;
    }

    public void setFitxerDescomprès(File value) {
        fitxerDescomprès = value;
    }

    public NodeHuffman getArrelHuffman() {
        return arrelHuffman;
    }

    public void setArrelHuffman(NodeHuffman value) {
        arrelHuffman = value;
    }

    public String getMissatge() { return missatgeInformatiu; }
    public void setMissatge(String value) { missatgeInformatiu = value; }

}
