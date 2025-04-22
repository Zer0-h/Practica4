package model;

import controlador.Controlador;
import controlador.Notificacio;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.huffman.CodificadorHuffman;
import model.huffman.FitxerHuffman;
import model.huffman.NodeHuffman;

/**
 * Classe que representa el procés de compressió en un fil separat.
 * Aquest fil executa la lectura del fitxer original, la codificació Huffman,
 * l'escriptura del fitxer comprimit i l'actualització del model amb les
 * estadístiques.
 *
 * Utilitza el patró d'esdeveniments per notificar quan la compressió ha
 * finalitzat o ha fallat.
 *
 * @author tonitorres
 */
public class ProcessCompressio extends Thread {

    private final Controlador controlador;
    private final Model model;

    /**
     * Constructor que rep el controlador i accedeix al model de l'aplicació.
     *
     * @param c el controlador principal
     */
    public ProcessCompressio(Controlador c) {
        controlador = c;
        model = c.getModel();
    }

    /**
     * Mètode que s'executa en paral·lel quan es crida .start().
     * Gestiona tot el procés de compressió Huffman.
     */
    @Override
    public void run() {
        try {
            long inici = System.nanoTime();

            // Llegir dades binàries del fitxer original
            byte[] dades = FitxerHuffman.llegirBytes(model.getFitxerOriginal());

            // Calcular les freqüències dels símbols
            Map<Byte, Integer> freq = calcularFrequencia(dades);

            // Construir arbre de Huffman i generar codis
            CodificadorHuffman codificador = new CodificadorHuffman(model.crearCua());
            NodeHuffman arrel = codificador.construirArbre(freq);
            Map<Byte, String> codis = codificador.generarCodis(arrel);

            // Codificar el contingut a bits
            int[] padding = new int[1];
            byte[] codificat = codificador.codificarBytes(codis, dades, padding);

            // Escriure fitxer .huff
            FitxerHuffman.escriureFitxer(model.getFitxerComprès(), codis, codificat, padding[0]);

            long fi = System.nanoTime();

            // Actualitzar estadístiques al model
            model.setArrelHuffman(arrel);
            model.setTempsCompressioMs((fi - inici) / 1_000_000);
            model.setTaxaCompressio(calcularTaxa());
            model.setLongitudMitjanaCodi(codificador.calcularLongitudMitjana(freq, codis));

            // Notificar que hem acabat
            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    /**
     * Calcula les freqüències d’aparició de cada byte dins el contingut.
     *
     * @param dades el contingut llegit del fitxer original
     *
     * @return mapa de freqüències byte → comptador
     */
    private Map<Byte, Integer> calcularFrequencia(byte[] dades) {
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : dades) {
            freq.put(b, freq.getOrDefault(b, 0) + 1);
        }
        return freq;
    }

    /**
     * Calcula la taxa de compressió basada en les mides dels fitxers original i
     * comprimit.
     *
     * @return la taxa de compressió com a percentatge
     */
    private double calcularTaxa() {
        long midaOriginal = model.getFitxerOriginal().length();
        long midaComprimida = model.getFitxerComprès().length();
        return (1.0 - ((double) midaComprimida / midaOriginal)) * 100.0;
    }
}
