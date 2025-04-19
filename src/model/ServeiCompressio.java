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

    public byte[] llegirBytes(File fitxer) throws IOException {
        return Files.readAllBytes(fitxer.toPath());
    }

    public void comprimir(File fitxerOriginal, File fitxerSortida) {
        try {
            Model model = controlador.getModel();
            byte[] dades = llegirBytes(fitxerOriginal);
            Map<Byte, Integer> freq = calcularFrequencia(dades);

            ArbreHuffman arbre = new ArbreHuffman();
            arrel = arbre.construirArbreBytes(freq);
            model.setArrelHuffman(arrel);

            Map<Byte, String> codis = new HashMap<>();
            arbre.generarCodisBytes(arrel, "", codis);

            long inici = System.nanoTime();

            // Codificar les dades a bits
            StringBuilder bitsStr = new StringBuilder();
            for (byte b : dades) {
                bitsStr.append(codis.get(b));
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int i = 0;
            while (i + 8 <= bitsStr.length()) {
                String byteStr = bitsStr.substring(i, i + 8);
                buffer.write(Integer.parseInt(byteStr, 2));
                i += 8;
            }
            int padding = 0;
            if (i < bitsStr.length()) {
                String lastBits = bitsStr.substring(i);
                padding = 8 - lastBits.length();
                lastBits += "0".repeat(padding);
                buffer.write(Integer.parseInt(lastBits, 2));
            }
            String bitsBase64 = Base64.getEncoder().encodeToString(buffer.toByteArray());

            // Escriure fitxer comprimit
            try (BufferedWriter escriptor = new BufferedWriter(new FileWriter(fitxerSortida))) {
                for (Map.Entry<Byte, String> entrada : codis.entrySet()) {
                    String byteCodificat = Base64.getEncoder().encodeToString(new byte[]{entrada.getKey()});
                    escriptor.write(byteCodificat + ":" + entrada.getValue() + ":" + freq.get(entrada.getKey()));
                    escriptor.newLine();
                }
                escriptor.write("#\n");
                escriptor.write(padding + "\n");
                escriptor.write(bitsBase64);
            }

            long fi = System.nanoTime();
            model.setFitxerComprès(fitxerSortida);
            model.setTempsCompressioMs((fi - inici) / 1_000_000);

            long midaOriginal = fitxerOriginal.length();
            long midaComprimida = fitxerSortida.length();
            double taxa = (1.0 - ((double) midaComprimida / midaOriginal)) * 100;
            model.setTaxaCompressio(taxa);

            model.setLongitudMitjanaCodi(calcularLongitudMitjana(freq, codis));
            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    public void descomprimir(File fitxerComprès, File fitxerSortida) {
        try (BufferedReader lector = new BufferedReader(new FileReader(fitxerComprès))) {
            Map<String, Byte> codisInvers = new HashMap<>();
            String linia;

            // Llegim la taula de codis
            while ((linia = lector.readLine()) != null && !linia.equals("#")) {
                String[] parts = linia.split(":", 3);
                if (parts.length == 3) {
                    byte[] simbol = Base64.getDecoder().decode(parts[0]);
                    codisInvers.put(parts[1], simbol[0]);
                }
            }

            int padding = Integer.parseInt(lector.readLine());
            String encodedBits = lector.readLine();
            byte[] bytes = Base64.getDecoder().decode(encodedBits);

            StringBuilder bits = new StringBuilder();
            for (byte b : bytes) {
                bits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            }
            bits.setLength(bits.length() - padding);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StringBuilder buffer = new StringBuilder();
            for (char bit : bits.toString().toCharArray()) {
                buffer.append(bit);
                if (codisInvers.containsKey(buffer.toString())) {
                    out.write(codisInvers.get(buffer.toString()));
                    buffer.setLength(0);
                }
            }

            Files.write(fitxerSortida.toPath(), out.toByteArray());
            controlador.getModel().setFitxerDescomprès(fitxerSortida);
            controlador.notificar(Notificacio.DESCOMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    public NodeHuffman getArrel() {
        return arrel;
    }

    public Map<Byte, Integer> calcularFrequencia(byte[] dades) {
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : dades) {
            freq.put(b, freq.getOrDefault(b, 0) + 1);
        }
        return freq;
    }

    private double calcularLongitudMitjana(Map<Byte, Integer> freq, Map<Byte, String> codis) {
        double total = 0;
        int totalFreq = freq.values().stream().mapToInt(i -> i).sum();
        for (var entry : freq.entrySet()) {
            byte b = entry.getKey();
            int f = entry.getValue();
            int l = codis.get(b).length();
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
