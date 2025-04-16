package model;

import controlador.Controlador;
import controlador.Notificacio;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ServeiCompressio {

    private final Controlador controlador;
    private NodeHuffman arrel;

    public ServeiCompressio(Controlador controlador) {
        this.controlador = controlador;
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

            // Guardar fitxer .huff
            try (FileOutputStream out = new FileOutputStream(fitxerSortida);
                 ObjectOutputStream oos = new ObjectOutputStream(out)) {

                oos.writeObject(codis);

                StringBuilder bits = new StringBuilder();
                for (char c : textOriginal.toCharArray()) {
                    bits.append(codis.get(c));
                }

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int index = 0;
                while (index + 8 <= bits.length()) {
                    String byteStr = bits.substring(index, index + 8);
                    buffer.write((byte) Integer.parseInt(byteStr, 2));
                    index += 8;
                }

                int restants = bits.length() - index;
                int padding = 8 - restants;
                if (restants > 0) {
                    String byteStr = bits.substring(index) + "0".repeat(padding);
                    buffer.write((byte) Integer.parseInt(byteStr, 2));
                }

                oos.writeInt(padding);
                buffer.writeTo(out);
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
        try (FileInputStream in = new FileInputStream(fitxerComprès);
             ObjectInputStream ois = new ObjectInputStream(in)) {

            Map<Character, String> codis = (Map<Character, String>) ois.readObject();

            Map<String, Character> codisInvers = new HashMap<>();
            for (Map.Entry<Character, String> entrada : codis.entrySet()) {
                codisInvers.put(entrada.getValue(), entrada.getKey());
            }

            int padding = ois.readInt();

            ByteArrayOutputStream bytesCodificats = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1) {
                bytesCodificats.write(b);
            }

            StringBuilder bits = new StringBuilder();
            byte[] bytes = bytesCodificats.toByteArray();
            for (byte bt : bytes) {
                String binari = String.format("%8s", Integer.toBinaryString(bt & 0xFF)).replace(' ', '0');
                bits.append(binari);
            }

            if (padding > 0 && bits.length() >= padding) {
                bits.setLength(bits.length() - padding);
            }

            StringBuilder buffer = new StringBuilder();
            BufferedWriter escriptor = new BufferedWriter(new FileWriter(fitxerSortida));
            for (char bit : bits.toString().toCharArray()) {
                buffer.append(bit);
                if (codisInvers.containsKey(buffer.toString())) {
                    escriptor.write(codisInvers.get(buffer.toString()));
                    buffer.setLength(0);
                }
            }
            escriptor.close();
            controlador.notificar(Notificacio.DESCOMPRESSIO_COMPLETA);

        } catch (IOException | ClassNotFoundException e) {
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
}
