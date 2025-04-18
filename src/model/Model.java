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
    private String missatgeInformatiu;

    private NodeHuffman arrelHuffman;

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

    public String getMissatge() { return missatgeInformatiu; }
    public void setMissatge(String value) { missatgeInformatiu = value; }

}
