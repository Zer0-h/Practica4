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

            // Codificar dades a bits
            StringBuilder bitsStr = new StringBuilder();
            for (byte b : dades) {
                bitsStr.append(codis.get(b));
            }

            // Convertir bits a byte[]
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

            // Escriure al fitxer binari
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fitxerSortida))) {
                dos.writeInt(codis.size());

                for (Map.Entry<Byte, String> entrada : codis.entrySet()) {
                    byte simbol = entrada.getKey();
                    String codi = entrada.getValue();
                    int llargada = codi.length();

                    dos.writeByte(simbol);
                    dos.writeByte(llargada);

                    int index = 0;
                    while (index + 8 <= codi.length()) {
                        String byteStr = codi.substring(index, index + 8);
                        dos.writeByte(Integer.parseInt(byteStr, 2));
                        index += 8;
                    }

                    if (index < codi.length()) {
                        String restant = codi.substring(index);
                        restant += "0".repeat(8 - restant.length());
                        dos.writeByte(Integer.parseInt(restant, 2));
                    }
                }

                dos.writeByte(padding);
                dos.write(buffer.toByteArray());
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
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fitxerComprès))) {
            int midaTaula = dis.readInt();
            Map<String, Byte> codisInvers = new HashMap<>();

            for (int i = 0; i < midaTaula; i++) {
                byte simbol = dis.readByte();
                int llargada = dis.readByte();
                int numBytes = (int) Math.ceil(llargada / 8.0);
                byte[] codiBytes = new byte[numBytes];
                dis.readFully(codiBytes);

                StringBuilder bits = new StringBuilder();
                for (byte b : codiBytes) {
                    bits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
                }
                String codi = bits.substring(0, llargada);
                codisInvers.put(codi, simbol);
            }

            int padding = dis.readByte();

            ByteArrayOutputStream dades = new ByteArrayOutputStream();
            while (dis.available() > 0) {
                dades.write(dis.readByte());
            }

            StringBuilder bits = new StringBuilder();
            for (byte b : dades.toByteArray()) {
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
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fitxer))) {
            int midaTaula = dis.readInt();
            Map<Byte, String> codis = new HashMap<>();
            Map<Byte, Integer> freq = new HashMap<>(); // només si vols propagar després

            for (int i = 0; i < midaTaula; i++) {
                byte simbol = dis.readByte();
                int llargada = dis.readByte();
                int numBytes = (int) Math.ceil(llargada / 8.0);
                byte[] codiBytes = new byte[numBytes];
                dis.readFully(codiBytes);

                StringBuilder bits = new StringBuilder();
                for (byte b : codiBytes) {
                    bits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
                }
                String codi = bits.substring(0, llargada);
                codis.put(simbol, codi);
                freq.put(simbol, 0); // podries ignorar si no les tens
            }

            NodeHuffman arrel = reconstruirArbreDesDeCodis(codis);
            propagarFrequencies(arrel); // opcional, si vols mostrar freqüències als interns
            return arrel;
        }
    }


    // Reconstrucció de l'arbre a partir de la taula de codis
    private NodeHuffman reconstruirArbreDesDeCodis(Map<Byte, String> codis) {
        NodeHuffman arrel = new NodeHuffman('\0', 0);

        for (Map.Entry<Byte, String> entrada : codis.entrySet()) {
            NodeHuffman actual = arrel;
            String codi = entrada.getValue();
            byte simbol = entrada.getKey();

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

            actual.setSimbol((char)(simbol & 0xFF)); // conversió segura
        }

        return arrel;
    }


    private int propagarFrequencies(NodeHuffman node) {
        if (node == null) return 0;
        if (node.esFulla()) return 1;

        int fE = propagarFrequencies(node.getNodeEsquerra());
        int fD = propagarFrequencies(node.getNodeDreta());

        node.setFrequencia(fE + fD);
        return fE + fD;
    }

}
