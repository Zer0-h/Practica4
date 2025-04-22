package model.huffman;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Classe responsable de la descompressió de dades codificades amb Huffman.
 * Realitza la decodificació de la cadena de bits a partir dels codis Huffman
 * inversos.
 *
 * @author tonitorres
 */
public class DecodificadorHuffman {

    /**
     * Decodifica un array de bytes codificats amb Huffman a partir dels codis
     * inversos.
     *
     * @param codisInvers      mapa de codi binari → byte original
     * @param dadesCodificades array de bytes codificats
     * @param padding          nombre de bits de farciment a l’últim byte
     *
     * @return array de bytes descomprimits
     */
    public static byte[] decodificar(Map<String, Byte> codisInvers, byte[] dadesCodificades, int padding) {
        // Convertim els bytes a una cadena de bits
        StringBuilder bits = new StringBuilder();
        for (byte b : dadesCodificades) {
            bits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        // Eliminam els bits de padding afegits a l’últim byte
        bits.setLength(bits.length() - padding);

        ByteArrayOutputStream resultat = new ByteArrayOutputStream();
        StringBuilder buffer = new StringBuilder();

        // Llegim bit a bit i reconstruïm el símbol original si coincideix amb un codi
        for (char bit : bits.toString().toCharArray()) {
            buffer.append(bit);
            if (codisInvers.containsKey(buffer.toString())) {
                resultat.write(codisInvers.get(buffer.toString()));
                buffer.setLength(0);
            }
        }

        return resultat.toByteArray();
    }
}
