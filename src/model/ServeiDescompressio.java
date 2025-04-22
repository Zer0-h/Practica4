package model;

import model.huffman.ArbreHuffman;
import model.huffman.EntradaHuffman;
import model.huffman.CodificadorHuffman;
import model.huffman.NodeHuffman;
import model.huffman.FitxerHuffman;
import model.huffman.DecodificadorHuffman;
import controlador.Controlador;
import controlador.Notificacio;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ServeiDescompressio {

    private final Controlador controlador;

    public ServeiDescompressio(Controlador c) {
        controlador = c;
    }

    public void comprimir(File fitxerOriginal, File fitxerSortida) {
        try {
            long inici = System.nanoTime();

            Model model = controlador.getModel();

            // Llegir dades binàries
            byte[] dades = FitxerHuffman.llegirBytes(fitxerOriginal);

            // Calcular freqüències
            Map<Byte, Integer> freq = calcularFrequencia(dades);

            // Codificació
            CodificadorHuffman codificador = new CodificadorHuffman(model.crearCua());
            NodeHuffman arrel = codificador.construirArbre(freq);
            Map<Byte, String> codis = codificador.generarCodis(arrel);
            int[] padding = new int[1];
            byte[] codificat = codificador.codificarBytes(codis, dades, padding);

            // Escriure fitxer .huff
            FitxerHuffman.escriureFitxer(fitxerSortida, codis, codificat, padding[0]);

            long fi = System.nanoTime();

            model.setArrelHuffman(arrel);
            model.setFitxerComprès(fitxerSortida);
            model.setTempsCompressioMs((fi - inici) / 1_000_000);
            model.setTaxaCompressio(calcularTaxa(fitxerOriginal, fitxerSortida));
            model.setLongitudMitjanaCodi(codificador.calcularLongitudMitjana(freq, codis));

            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    public void descomprimir(File fitxerComprès, File fitxerSortida) {
        try {
            EntradaHuffman entrada = FitxerHuffman.llegirTaulaIHBits(fitxerComprès);
            byte[] descompressat = DecodificadorHuffman.decodificar(
                entrada.getCodisInvers(),
                entrada.getDadesCodificades(),
                entrada.getPadding()
            );

            Files.write(fitxerSortida.toPath(), descompressat);

            controlador.getModel().setFitxerDescomprès(fitxerSortida);
            controlador.notificar(Notificacio.DESCOMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    // Reconstruir arbre
    public NodeHuffman reconstruirArbreDesDeFitxerHuff(File fitxer) throws IOException {
        EntradaHuffman entrada = FitxerHuffman.llegirTaulaIHBits(fitxer);
        Map<String, Byte> codisInvers = entrada.getCodisInvers();

        // Invertim de String→Byte a Byte→String
        Map<Byte, String> codis = new HashMap<>();
        for (Map.Entry<String, Byte> entry : codisInvers.entrySet()) {
            codis.put(entry.getValue(), entry.getKey());
        }

        return ArbreHuffman.reconstruirDesDeCodis(codis);
    }


    private Map<Byte, Integer> calcularFrequencia(byte[] dades) {
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : dades) {
            freq.put(b, freq.getOrDefault(b, 0) + 1);
        }
        return freq;
    }

    private double calcularTaxa(File original, File comprimit) {
        long midaOriginal = original.length();
        long midaComprimida = comprimit.length();
        return (1.0 - ((double) midaComprimida / midaOriginal)) * 100.0;
    }
}
