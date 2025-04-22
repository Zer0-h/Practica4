package model.huffman;

import java.util.Map;

/**
 * Classe que encapsula la informació llegida d’un fitxer comprimit amb Huffman:
 * - La taula de codis inversos (codi binari → byte)
 * - Les dades codificades (en format binari)
 * - El nombre de bits de farciment afegits a l’últim byte
 *
 * Aquesta classe serveix com a contenidor per facilitar la descompressió.
 *
 * @author tonitorres
 */
public class EntradaHuffman {

    private final Map<String, Byte> codisInvers;
    private final byte[] dadesCodificades;
    private final int padding;

    /**
     * Crea una entrada Huffman amb la taula de codis, les dades i el padding.
     *
     * @param c mapa de codis inversos (codi binari → símbol original)
     * @param d array de bytes codificats
     * @param p nombre de bits de farciment a l’últim byte
     */
    public EntradaHuffman(Map<String, Byte> c, byte[] d, int p) {
        codisInvers = c;
        dadesCodificades = d;
        padding = p;
    }

    /**
     * Retorna la taula de codis inversos.
     */
    public Map<String, Byte> getCodisInvers() {
        return codisInvers;
    }

    /**
     * Retorna les dades codificades.
     */
    public byte[] getDadesCodificades() {
        return dadesCodificades;
    }

    /**
     * Retorna el nombre de bits de farciment.
     */
    public int getPadding() {
        return padding;
    }
}
