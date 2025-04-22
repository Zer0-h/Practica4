package model;

import java.io.File;
import model.cua.*;
import model.huffman.NodeHuffman;

/**
 * Classe que encapsula l'estat de l'aplicació.
 * Conté les referències als fitxers (original, comprimit, descomprimit),
 * així com les estadístiques de compressió i la configuració del tipus de cua.
 *
 * També actua com a font d'informació per a la vista i altres components,
 * mantenint l'estructura del patró MVC.
 *
 * @author tonitorres
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

    /**
     * Constructor per defecte.
     * Inicialitza el tipus de cua per defecte com a heap binari.
     */
    public Model() {
        tipusCua = TipusCua.BINARY_HEAP;
    }

    /**
     * Crea una instància d'una cua de prioritats segons el tipus seleccionat.
     * Aquesta cua serà utilitzada per a la construcció de l'arbre de Huffman.
     *
     * @return una instància de CuaPrioritat segons el valor actual de tipusCua.
     */
    public CuaPrioritat crearCua() {
        return switch (tipusCua) {
            case BINARY_HEAP ->
                new CuaBinaryHeap(16);
            case FIBONACCI_HEAP ->
                new CuaFibonacciHeap();
            case ORDENADA ->
                new LlistaOrdenada();
            case NO_ORDENADA ->
                new LlistaNoOrdenada();
            default ->
                new CuaBinaryHeap(16); // per seguretat
        };
    }

    public void setTipusCua(TipusCua tipus) {
        tipusCua = tipus;
    }

    public long getTempsCompressioMs() {
        return tempsCompressioMs;
    }

    public void setTempsCompressioMs(long valor) {
        tempsCompressioMs = valor;
    }

    public double getLongitudMitjanaCodi() {
        return longitudMitjanaCodi;
    }

    public void setLongitudMitjanaCodi(double valor) {
        longitudMitjanaCodi = valor;
    }

    public double getTaxaCompressio() {
        return taxaCompressio;
    }

    public void setTaxaCompressio(double valor) {
        taxaCompressio = valor;
    }

    public File getFitxerOriginal() {
        return fitxerOriginal;
    }

    public void setFitxerOriginal(File fitxer) {
        fitxerOriginal = fitxer;
    }

    public File getFitxerComprès() {
        return fitxerComprès;
    }

    public void setFitxerComprès(File fitxer) {
        fitxerComprès = fitxer;
    }

    public File getFitxerDescomprès() {
        return fitxerDescomprès;
    }

    public void setFitxerDescomprès(File fitxer) {
        fitxerDescomprès = fitxer;
    }

    public NodeHuffman getArrelHuffman() {
        return arrelHuffman;
    }

    public void setArrelHuffman(NodeHuffman arrel) {
        arrelHuffman = arrel;
    }

    public String getMissatge() {
        return missatgeInformatiu;
    }

    public void setMissatge(String missatge) {
        missatgeInformatiu = missatge;
    }
}
