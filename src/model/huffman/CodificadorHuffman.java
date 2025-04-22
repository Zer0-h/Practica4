package model.huffman;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import model.cua.CuaPrioritat;

/**
 * Classe responsable de construir l'arbre de Huffman i generar els codis
 * binaris
 * per a cada símbol a partir de les freqüències.
 *
 * També permet codificar les dades utilitzant aquests codis.
 *
 * @author tonitorres
 */
public class CodificadorHuffman {

    private final CuaPrioritat cua;

    /**
     * Constructor que rep la cua de prioritat a utilitzar (Heap binari,
     * Fibonacci, etc.).
     *
     * @param c la implementació de CuaPrioritat que s'ha de fer servir
     */
    public CodificadorHuffman(CuaPrioritat c) {
        cua = c;
    }

    /**
     * Construeix l'arbre de Huffman a partir d’un mapa de freqüències.
     *
     * @param freq mapa de símbol → freqüència
     *
     * @return arrel de l’arbre de Huffman construït
     */
    public NodeHuffman construirArbre(Map<Byte, Integer> freq) {
        for (var entrada : freq.entrySet()) {
            byte b = entrada.getKey();
            int f = entrada.getValue();
            cua.afegir(new NodeHuffman((char) (b & 0xFF), f));
        }

        while (cua.mida() > 1) {
            NodeHuffman n1 = cua.extreure();
            NodeHuffman n2 = cua.extreure();
            NodeHuffman pare = new NodeHuffman('\0', n1.getFrequencia() + n2.getFrequencia());
            pare.setNodeEsquerra(n1);
            pare.setNodeDreta(n2);
            cua.afegir(pare);
        }

        return cua.extreure();
    }

    /**
     * Genera els codis binaris de Huffman per a cada símbol a partir de
     * l’arbre.
     *
     * @param arrel arrel de l’arbre de Huffman
     *
     * @return mapa de símbol → codi binari
     */
    public Map<Byte, String> generarCodis(NodeHuffman arrel) {
        Map<Byte, String> codis = new HashMap<>();
        generarRec(arrel, "", codis);
        return codis;
    }

    /**
     * Mètode recursiu per generar els codis binaris.
     */
    private void generarRec(NodeHuffman node, String codi, Map<Byte, String> codis) {
        if (node == null) {
            return;
        }
        if (node.esFulla()) {
            codis.put((byte) node.getSimbol(), codi);
        } else {
            generarRec(node.getNodeEsquerra(), codi + "0", codis);
            generarRec(node.getNodeDreta(), codi + "1", codis);
        }
    }

    /**
     * Calcula la longitud mitjana del codi de Huffman, ponderada per les
     * freqüències.
     *
     * @param freq  mapa de freqüències
     * @param codis mapa de codis
     *
     * @return longitud mitjana en bits per símbol
     */
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

    /**
     * Codifica un array de bytes a una seqüència de bits segons els codis
     * Huffman.
     * Retorna un array de bytes compactat i retorna també el padding (bits de
     * farciment).
     *
     * @param codis      mapa de símbol → codi binari
     * @param dades      dades originals a codificar
     * @param paddingOut array de mida 1 per retornar el nombre de bits de
     *                   padding usats
     *
     * @return array de bytes codificats
     */
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
