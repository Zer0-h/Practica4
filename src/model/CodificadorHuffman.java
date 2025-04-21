package model;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import model.cua.Cua;

public class CodificadorHuffman {

    private final Cua cua;

    public CodificadorHuffman(Cua c) {
        cua = c;
    }

    public NodeHuffman construirArbre(Map<Byte, Integer> freq) {
        for (var entrada : freq.entrySet()) {
            byte b = entrada.getKey();
            int f = entrada.getValue();
            cua.afegir(new NodeHuffman((char) (b & 0xFF), f));
        }

        while (cua.mida()> 1) {
            NodeHuffman n1 = cua.extreure();
            NodeHuffman n2 = cua.extreure();
            NodeHuffman pare = new NodeHuffman('\0', n1.getFrequencia() + n2.getFrequencia());
            pare.setNodeEsquerra(n1);
            pare.setNodeDreta(n2);
            cua.afegir(pare);
        }

        return cua.extreure();
    }

    public Map<Byte, String> generarCodis(NodeHuffman arrel) {
        Map<Byte, String> codis = new HashMap<>();
        generarRec(arrel, "", codis);
        return codis;
    }

    private void generarRec(NodeHuffman node, String codi, Map<Byte, String> codis) {
        if (node == null) return;
        if (node.esFulla()) {
            codis.put((byte) node.getSimbol(), codi);
        } else {
            generarRec(node.getNodeEsquerra(), codi + "0", codis);
            generarRec(node.getNodeDreta(), codi + "1", codis);
        }
    }

    public double calcularLongitudMitjana(Map<Byte, Integer> freq, Map<Byte, String> codis) {
        double total = 0;
        int totalFreq = freq.values().stream().mapToInt(i -> i).sum();
        for (var entry : freq.entrySet()) {
            byte b = entry.getKey();
            int f = entry.getValue();
            int l = codis.get(b).length();
            total += ((double) f / totalFreq) * l;
        }
        return total;
    }

    public byte[] codificarBytes(Map<Byte, String> codis, byte[] dades, int[] paddingOut) {
        StringBuilder bitsStr = new StringBuilder();
        for (byte b : dades) {
            bitsStr.append(codis.get(b));
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = 0;
        while (i + 8 <= bitsStr.length()) {
            String byteStr = bitsStr.substring(i, i + 8);
            buffer.write(Integer.parseInt(byteStr, 2));
            i += 8;
        }

        int padding = 0;
        if (i < bitsStr.length()) {
            String lastBits = bitsStr.substring(i);
            padding = 8 - lastBits.length();
            lastBits += "0".repeat(padding);
            buffer.write(Integer.parseInt(lastBits, 2));
        }

        paddingOut[0] = padding;
        return buffer.toByteArray();
    }
}
