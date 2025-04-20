package model;

import java.util.Map;

public class EntradaHuffman {
    private final Map<String, Byte> codisInvers;
    private final byte[] dadesCodificades;
    private final int padding;

    public EntradaHuffman(Map<String, Byte> c, byte[] d, int p) {
        codisInvers = c;
        dadesCodificades = d;
        padding = p;
    }

    public Map<String, Byte> getCodisInvers() {
        return codisInvers;
    }

    public byte[] getDadesCodificades() {
        return dadesCodificades;
    }

    public int getPadding() {
        return padding;
    }
}
