package model.huffman;

import java.util.*;

public class ArbreHuffman {

    public static NodeHuffman reconstruirDesDeCodis(Map<Byte, String> codis) {
        NodeHuffman arrel = new NodeHuffman('\0', 0);

        for (Map.Entry<Byte, String> entrada : codis.entrySet()) {
            NodeHuffman actual = arrel;
            String codi = entrada.getValue();
            byte simbol = entrada.getKey();

            for (char bit : codi.toCharArray()) {
                if (bit == '0') {
                    if (actual.getNodeEsquerra() == null) {
                        actual.setNodeEsquerra(new NodeHuffman('\0', 0));
                    }
                    actual = actual.getNodeEsquerra();
                } else {
                    if (actual.getNodeDreta() == null) {
                        actual.setNodeDreta(new NodeHuffman('\0', 0));
                    }
                    actual = actual.getNodeDreta();
                }
            }

            actual.setSimbol((char)(simbol & 0xFF));
        }

        return arrel;
    }
}
