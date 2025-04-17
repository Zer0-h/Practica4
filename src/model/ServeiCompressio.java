package model;

import controlador.Controlador;
import controlador.Notificacio;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ServeiCompressio {

    private final Controlador controlador;
    private NodeHuffman arrel;

    public ServeiCompressio(Controlador c) {
        controlador = c;
    }

    public String llegirFitxer(File fitxer) throws IOException {
        return new String(Files.readAllBytes(fitxer.toPath()));
    }

    public void comprimir(String textOriginal, File fitxerSortida) {
        try {
            Model model = controlador.getModel();

            Map<Character, Integer> freq = calcularFrequencia(textOriginal);
            ArbreHuffman arbre = new ArbreHuffman();
            arrel = arbre.construirArbre(freq);
            model.setArrelHuffman(arrel);

            Map<Character, String> codis = new HashMap<>();
            arbre.generarCodis(arrel, "", codis);

            File fitxerOriginal = model.getFitxerOriginal();
            long inici = System.nanoTime();

            // Construir la cadena codificada
            StringBuilder codificat = new StringBuilder();
            for (char c : textOriginal.toCharArray()) {
                codificat.append(codis.get(c));
            }

            // Guardar fitxer amb taula codis (Base64) + cadena codificada
            try (BufferedWriter escriptor = new BufferedWriter(new FileWriter(fitxerSortida))) {
                for (Map.Entry<Character, String> entrada : codis.entrySet()) {
                    String simbolCodificat = Base64.getEncoder().encodeToString(
                            String.valueOf(entrada.getKey()).getBytes());

                    escriptor.write(simbolCodificat + ":" + entrada.getValue() + ":" + freq.get(entrada.getKey()));
                    escriptor.newLine();
                }
                escriptor.write("#");
                escriptor.newLine();
                escriptor.write(codificat.toString());
            }

            long fi = System.nanoTime();

            model.setFitxerComprès(fitxerSortida);
            model.setTempsCompressioMs((fi - inici) / 1_000_000);

            long midaOriginal = fitxerOriginal.length();
            long midaComprimida = fitxerSortida.length();
            double taxa = (1.0 - ((double) midaComprimida / midaOriginal)) * 100;
            model.setTaxaCompressio(taxa);

            double longitudMitjana = calcularLongitudMitjana(freq, codis);
            model.setLongitudMitjanaCodi(longitudMitjana);

            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    public void descomprimir(File fitxerComprès, File fitxerSortida) {
        try (BufferedReader lector = new BufferedReader(new FileReader(fitxerComprès))) {
            Map<String, Character> codisInvers = new HashMap<>();
            String linia;

            // Llegim la taula de codis
            while ((linia = lector.readLine()) != null && !linia.equals("#")) {
                String[] parts = linia.split(":", 3);
                if (parts.length == 3) {
                    byte[] decoded = Base64.getDecoder().decode(parts[0]);
                    char simbol = new String(decoded).charAt(0);
                    codisInvers.put(parts[1], simbol);
                }
            }

            // Llegim la cadena codificada
            StringBuilder codificat = new StringBuilder();
            while ((linia = lector.readLine()) != null) {
                codificat.append(linia);
            }

            // Descodifiquem i escrivim el fitxer
            BufferedWriter escriptor = new BufferedWriter(new FileWriter(fitxerSortida));
            StringBuilder buffer = new StringBuilder();
            for (char bit : codificat.toString().toCharArray()) {
                buffer.append(bit);
                if (codisInvers.containsKey(buffer.toString())) {
                    escriptor.write(codisInvers.get(buffer.toString()));
                    buffer.setLength(0);
                }
            }
            escriptor.close();

            controlador.getModel().setFitxerDescomprès(fitxerSortida);
            controlador.notificar(Notificacio.DESCOMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }


    public Map<Character, Integer> calcularFrequencia(String contingut) {
        Map<Character, Integer> frequencia = new HashMap<>();
        for (char c : contingut.toCharArray()) {
            frequencia.put(c, frequencia.getOrDefault(c, 0) + 1);
        }
        return frequencia;
    }

    public NodeHuffman getArrel() {
        return arrel;
    }

    private double calcularLongitudMitjana(Map<Character, Integer> freq, Map<Character, String> codis) {
        double total = 0;
        int totalFreq = freq.values().stream().mapToInt(i -> i).sum();
        for (var entry : freq.entrySet()) {
            char c = entry.getKey();
            int f = entry.getValue();
            int l = codis.get(c).length();
            total += ((double) f / totalFreq) * l;
        }
        return total;
    }

    // Reconstruir arbre

    public NodeHuffman reconstruirArbreDesDeFitxerHuff(File fitxer) throws IOException {
        try (BufferedReader lector = new BufferedReader(new FileReader(fitxer))) {
            Map<Character, String> codis = new HashMap<>();
            Map<Character, Integer> frequencies = new HashMap<>();
            String linia;

            // Llegim taula de codis
            while ((linia = lector.readLine()) != null && !linia.equals("#")) {
                String[] parts = linia.split(":", 3);
                if (parts.length == 3) {
                    byte[] decoded = Base64.getDecoder().decode(parts[0]);
                    char simbol = new String(decoded).charAt(0);
                    String codi = parts[1];
                    int freq = Integer.parseInt(parts[2]);
                    codis.put(simbol, codi);
                    frequencies.put(simbol, freq);
                }
            }

            NodeHuffman arrelArbre = reconstruirArbreDesDeCodis(codis, frequencies);
            propagarFrequencies(arrelArbre);

            return arrelArbre;
        }
    }

    // Reconstrucció de l'arbre a partir de la taula de codis
    private NodeHuffman reconstruirArbreDesDeCodis(Map<Character, String> codis, Map<Character, Integer> frequencies) {
        NodeHuffman arrelArbre = new NodeHuffman('\0', 0);
        for (Map.Entry<Character, String> entrada : codis.entrySet()) {
            NodeHuffman actual = arrelArbre;
            String codi = entrada.getValue();
            char simbol = entrada.getKey();

            for (int i = 0; i < codi.length(); i++) {
                char bit = codi.charAt(i);
                if (bit == '0') {
                    if (actual.getNodeEsquerra() == null) {
                        actual.setNodeEsquerra(new NodeHuffman('\0', 0));
                    }
                    actual = actual.getNodeEsquerra();
                } else {
                    if (actual.getNodeDreta() == null) {
                        actual.setNodeDreta(new NodeHuffman('\0', 0));
                    }
                    actual = actual.getNodeDreta();
                }
            }

            // Estem a la fulla: fixem el símbol
            actual.setSimbol(simbol);
            actual.setFrequencia(frequencies.get(simbol));
        }
        return arrelArbre;
    }

    private int propagarFrequencies(NodeHuffman node) {
        if (node == null) return 0;
        if (node.esFulla()) return node.getFrequencia();

        int freqEsq = propagarFrequencies(node.getNodeEsquerra());
        int freqDret = propagarFrequencies(node.getNodeDreta());

        int total = freqEsq + freqDret;
        node.setFrequencia(total);
        return total;
    }

}
