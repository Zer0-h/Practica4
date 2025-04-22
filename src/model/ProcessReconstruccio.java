package model;

import model.huffman.NodeHuffman;
import model.huffman.FitxerHuffman;
import controlador.Controlador;
import controlador.Notificacio;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import model.huffman.ArbreHuffman;
import model.huffman.EntradaHuffman;

public class ProcessReconstruccio extends Thread {

    private final Controlador controlador;

    public ProcessReconstruccio(Controlador c) {
        controlador = c;
    }

    @Override
    public void run() {
        try {
            Model model = controlador.getModel();

            EntradaHuffman entrada = FitxerHuffman.llegirTaulaIHBits(model.getFitxerComprès());
            Map<String, Byte> codisInvers = entrada.getCodisInvers();

            // Invertim de String→Byte a Byte→String
            Map<Byte, String> codis = new HashMap<>();
            for (Map.Entry<String, Byte> entry : codisInvers.entrySet()) {
                codis.put(entry.getValue(), entry.getKey());
            }

            NodeHuffman arrel = ArbreHuffman.reconstruirDesDeCodis(codis);
            model.setArrelHuffman(arrel);

            controlador.notificar(Notificacio.ARBRE_RECONSTRUIT);
        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }
}
