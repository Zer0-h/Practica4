package model;

import controlador.Controlador;
import controlador.Notificacio;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ServeiCompressio {

    private final Controlador controlador;

    public ServeiCompressio(Controlador c) {
        controlador = c;
    }

    public Map<Character, Integer> calcularFrequencia(String contingut) {
        Map<Character, Integer> frequencia = new HashMap<>();
        for (char c : contingut.toCharArray()) {
            frequencia.put(c, frequencia.getOrDefault(c, 0) + 1);
        }
        return frequencia;
    }

    public String llegirFitxer(File fitxer) throws IOException {
        return new String(Files.readAllBytes(fitxer.toPath()));
    }

    public void comprimir(String textOriginal, File fitxerSortida) {
        Map<Character, Integer> freq = calcularFrequencia(textOriginal);
        ArbreHuffman arbre = new ArbreHuffman();
        NodeHuffman arrel = arbre.construirArbre(freq);

        Map<Character, String> codis = new HashMap<>();
        arbre.generarCodis(arrel, "", codis);

        try (BufferedWriter escriptor = new BufferedWriter(new FileWriter(fitxerSortida))) {
            for (char c : textOriginal.toCharArray()) {
                escriptor.write(codis.get(c));
            }
            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);
        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    // La descompressió i codificació real amb bits s'afegirà més endavant.
}
