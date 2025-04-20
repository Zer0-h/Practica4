package model;

import java.util.*;

public class ArbreHuffman {

    public ArbreHuffman() {

    }

    public NodeHuffman construirArbreBytes(Map<Byte, Integer> freqBytes) {
        PriorityQueue<NodeHuffman> cua = new PriorityQueue<>();
        for (var entrada : freqBytes.entrySet()) {
            byte b = entrada.getKey();
            int freq = entrada.getValue();
            cua.add(new NodeHuffman((char) (b & 0xFF), freq));
        }

        while (cua.size() > 1) {
            NodeHuffman n1 = cua.poll();
            NodeHuffman n2 = cua.poll();
            NodeHuffman pare = new NodeHuffman('\0', n1.getFrequencia() + n2.getFrequencia());
            pare.setNodeEsquerra(n1);
            pare.setNodeDreta(n2);
            cua.add(pare);
        }

        return cua.poll();
    }

    public void generarCodisBytes(NodeHuffman arrel, String codiActual, Map<Byte, String> codis) {
        if (arrel == null) return;

        if (arrel.esFulla()) {
            codis.put((byte) arrel.getSimbol(), codiActual);
        } else {
            generarCodisBytes(arrel.getNodeEsquerra(), codiActual + "0", codis);
            generarCodisBytes(arrel.getNodeDreta(), codiActual + "1", codis);
        }
    }

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
