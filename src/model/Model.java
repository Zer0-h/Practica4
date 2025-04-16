package model;

import java.io.File;

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

    private NodeHuffman arrelHuffman;

    public long getTempsCompressioMs() {
        return tempsCompressioMs;
    }

    public void setTempsCompressioMs(long tempsCompressioMs) {
        this.tempsCompressioMs = tempsCompressioMs;
    }

    public double getLongitudMitjanaCodi() {
        return longitudMitjanaCodi;
    }

    public void setLongitudMitjanaCodi(double longitudMitjanaCodi) {
        this.longitudMitjanaCodi = longitudMitjanaCodi;
    }

    public double getTaxaCompressio() {
        return taxaCompressio;
    }

    public void setTaxaCompressio(double taxaCompressio) {
        this.taxaCompressio = taxaCompressio;
    }

    public File getFitxerOriginal() {
        return fitxerOriginal;
    }

    public void setFitxerOriginal(File fitxerOriginal) {
        this.fitxerOriginal = fitxerOriginal;
    }

    public File getFitxerComprès() {
        return fitxerComprès;
    }

    public void setFitxerComprès(File fitxerComprès) {
        this.fitxerComprès = fitxerComprès;
    }

    public File getFitxerDescomprès() {
        return fitxerDescomprès;
    }

    public void setFitxerDescomprès(File fitxerDescomprès) {
        this.fitxerDescomprès = fitxerDescomprès;
    }

    public NodeHuffman getArrelHuffman() {
        return arrelHuffman;
    }

    public void setArrelHuffman(NodeHuffman arrelHuffman) {
        this.arrelHuffman = arrelHuffman;
    }

    public String getEstadistiquesFormatades() {
        if (fitxerOriginal == null || fitxerComprès == null) return "";

        return String.format("""
            Mida comprimida: %d bytes
            Temps de compressió: %d ms
            Longitud mitjana del codi: %.3f bits/símbol
            Taxa de compressió: %.2f %%
            """,
            fitxerComprès.length(),
            tempsCompressioMs,
            longitudMitjanaCodi,
            taxaCompressio
        );
    }
}
