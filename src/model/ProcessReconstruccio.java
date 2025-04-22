package model;

import controlador.Controlador;
import controlador.Notificacio;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.huffman.ArbreHuffman;
import model.huffman.EntradaHuffman;
import model.huffman.FitxerHuffman;
import model.huffman.NodeHuffman;

/**
 * Classe que representa el procés de reconstrucció de l’arbre de Huffman
 * a partir de la informació emmagatzemada en un fitxer .huff.
 *
 * Aquest procés s'executa en un fil separat i notifica quan l'arbre està
 * reconstruït.
 *
 * @author tonitorres
 */
public class ProcessReconstruccio extends Thread {

    private final Controlador controlador;

    /**
     * Constructor del procés de reconstrucció.
     *
     * @param c controlador principal que gestiona l’aplicació
     */
    public ProcessReconstruccio(Controlador c) {
        controlador = c;
    }

    /**
     * Mètode que s'executa en un fil separat.
     * Reconstrueix l’arbre de Huffman a partir dels codis guardats al fitxer
     * .huff.
     */
    @Override
    public void run() {
        try {
            Model model = controlador.getModel();

            // Llegim la taula de codis i les dades binàries codificades
            EntradaHuffman entrada = FitxerHuffman.llegirTaulaIHBits(model.getFitxerComprès());
            Map<String, Byte> codisInvers = entrada.getCodisInvers();

            // Convertim la taula inversa (String → Byte) a (Byte → String)
            Map<Byte, String> codis = new HashMap<>();
            for (Map.Entry<String, Byte> entradaCodis : codisInvers.entrySet()) {
                codis.put(entradaCodis.getValue(), entradaCodis.getKey());
            }

            // Reconstruïm l’arbre
            NodeHuffman arrel = ArbreHuffman.reconstruirDesDeCodis(codis);
            model.setArrelHuffman(arrel);

            // Notificar que hem acabat
            controlador.notificar(Notificacio.ARBRE_RECONSTRUIT);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }
}
