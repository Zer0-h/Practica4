package model;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class DecodificadorHuffman {

    public static byte[] decodificar(Map<String, Byte> codisInvers, byte[] dadesCodificades, int padding) {
        StringBuilder bits = new StringBuilder();
        for (byte b : dadesCodificades) {
            bits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        bits.setLength(bits.length() - padding);

        ByteArrayOutputStream resultat = new ByteArrayOutputStream();
        StringBuilder buffer = new StringBuilder();

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
