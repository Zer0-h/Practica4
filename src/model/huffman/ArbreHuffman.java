package model.huffman;

import java.util.Map;

/**
 * Classe utilitària per reconstruir un arbre de Huffman
 * a partir d'una taula de codis {byte -> codi binari}.
 *
 * Aquesta reconstrucció és necessària durant la descompressió,
 * per poder decodificar la informació codificada sense tenir
 * accés a les freqüències originals.
 *
 * @author tonitorres
 */
public class ArbreHuffman {

    /**
     * Reconstrueix l’arbre de Huffman utilitzant la taula de codis.
     * Cada codi s’interpreta com un camí de l’arrel fins a una fulla.
     *
     * @param codis taula {byte -> cadena binària}
     *
     * @return arrel de l’arbre de Huffman reconstruït
     */
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

            // Assigna el símbol a la fulla
            actual.setSimbol((char) (simbol & 0xFF));
        }

        return arrel;
    }
}
