package model;

import java.util.*;

public class ArbreHuffman {

    public ArbreHuffman() {
        
    }

    public NodeHuffman construirArbre(Map<Character, Integer> frequenciaSimbols) {
        PriorityQueue<NodeHuffman> cua = new PriorityQueue<>();

        for (var entrada : frequenciaSimbols.entrySet()) {
            cua.add(new NodeHuffman(entrada.getKey(), entrada.getValue()));
        }

        while (cua.size() > 1) {
            NodeHuffman n1 = cua.poll();
            NodeHuffman n2 = cua.poll();
            NodeHuffman pare = new NodeHuffman('\0', n1.getFrequencia() + n2.getFrequencia());
            pare.setNodeEsquerra(n1);
            pare.setNodeDreta(n2);
            cua.add(pare);
        }

        return cua.poll(); // Arrel de l’arbre
    }

    public void generarCodis(NodeHuffman arrel, String codiActual, Map<Character, String> codis) {
        if (arrel == null) return;

        if (arrel.esFulla()) {
            codis.put(arrel.getSimbol(), codiActual);
        } else {
            generarCodis(arrel.getNodeEsquerra(), codiActual + "0", codis);
            generarCodis(arrel.getNodeDreta(), codiActual + "1", codis);
        }
    }
}
