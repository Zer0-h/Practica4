package model;

import controlador.Controlador;
import controlador.Notificacio;
import java.io.IOException;
import java.nio.file.Files;
import model.huffman.DecodificadorHuffman;
import model.huffman.EntradaHuffman;
import model.huffman.FitxerHuffman;

/**
 * Classe que representa el procés de descompressió en un fil separat.
 * Aquest fil llegeix un fitxer .huff, reconstrueix les dades originals i
 * escriu el fitxer descomprimit.
 *
 * Utilitza el patró d'esdeveniments per notificar la finalització o l'error del
 * procés.
 *
 * @author tonitorres
 */
public class ProcessDescompressio extends Thread {

    private final Controlador controlador;

    /**
     * Constructor del procés. Rep el controlador per accedir al model i
     * notificar esdeveniments.
     *
     * @param c controlador principal de l'aplicació
     */
    public ProcessDescompressio(Controlador c) {
        controlador = c;
    }

    /**
     * Mètode que s'executa quan es llança el fil.
     * Llegeix el fitxer .huff, reconstrueix les dades i escriu el fitxer
     * descomprimit.
     */
    @Override
    public void run() {
        try {
            Model model = controlador.getModel();

            // Llegir la taula de codis i les dades codificades
            EntradaHuffman entrada = FitxerHuffman.llegirTaulaIHBits(model.getFitxerComprès());

            // Descodificar les dades binàries
            byte[] descompressat = DecodificadorHuffman.decodificar(
                    entrada.getCodisInvers(),
                    entrada.getDadesCodificades(),
                    entrada.getPadding()
            );

            // Escriure el fitxer descomprimit
            Files.write(model.getFitxerDescomprès().toPath(), descompressat);

            // Notificar que hem acabat
            controlador.notificar(Notificacio.DESCOMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }
}
