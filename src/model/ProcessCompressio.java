package model;

import model.huffman.CodificadorHuffman;
import model.huffman.NodeHuffman;
import model.huffman.FitxerHuffman;
import controlador.Controlador;
import controlador.Notificacio;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ProcessCompressio extends Thread {

    private final Controlador controlador;
    private final Model model;

    public ProcessCompressio(Controlador c) {
        controlador = c;
        model = c.getModel();
    }

    @Override
    public void run() {
        try {
            long inici = System.nanoTime();

            // Llegir dades binàries
            byte[] dades = FitxerHuffman.llegirBytes(model.getFitxerOriginal());

            // Calcular freqüències
            Map<Byte, Integer> freq = calcularFrequencia(dades);

            // Codificació
            CodificadorHuffman codificador = new CodificadorHuffman(model.crearCua());
            NodeHuffman arrel = codificador.construirArbre(freq);
            Map<Byte, String> codis = codificador.generarCodis(arrel);
            int[] padding = new int[1];
            byte[] codificat = codificador.codificarBytes(codis, dades, padding);

            // Escriure fitxer .huff
            FitxerHuffman.escriureFitxer(model.getFitxerComprès(), codis, codificat, padding[0]);

            long fi = System.nanoTime();

            model.setArrelHuffman(arrel);
            model.setTempsCompressioMs((fi - inici) / 1_000_000);
            model.setTaxaCompressio(calcularTaxa());
            model.setLongitudMitjanaCodi(codificador.calcularLongitudMitjana(freq, codis));

            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }


    private Map<Byte, Integer> calcularFrequencia(byte[] dades) {
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : dades) {
            freq.put(b, freq.getOrDefault(b, 0) + 1);
        }
        return freq;
    }

    private double calcularTaxa() {
        long midaOriginal = model.getFitxerOriginal().length();
        long midaComprimida = model.getFitxerComprès().length();
        return (1.0 - ((double) midaComprimida / midaOriginal)) * 100.0;
    }
}
