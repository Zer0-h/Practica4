package model;

import model.huffman.FitxerHuffman;
import controlador.Controlador;
import controlador.Notificacio;
import java.io.*;
import java.nio.file.Files;
import model.huffman.DecodificadorHuffman;
import model.huffman.EntradaHuffman;

public class ProcessDescompressio extends Thread {

    private final Controlador controlador;

    public ProcessDescompressio(Controlador c) {
        controlador = c;
    }

    @Override
    public void run() {
        try {
            Model model = controlador.getModel();

            EntradaHuffman entrada = FitxerHuffman.llegirTaulaIHBits(model.getFitxerComprès());
            byte[] descompressat = DecodificadorHuffman.decodificar(
                entrada.getCodisInvers(),
                entrada.getDadesCodificades(),
                entrada.getPadding()
            );

            Files.write(model.getFitxerDescomprès().toPath(), descompressat);

            controlador.notificar(Notificacio.DESCOMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }
}
