package model;

import java.io.File;

/**
 * Classe que encapsula l'estat de l'aplicació: fitxers implicats en la compressió.
 */
public class Model {
    private File fitxerOriginal;
    private File fitxerComprès;
    private File fitxerDescomprès;
    private NodeHuffman arrelHuffman;

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
}
