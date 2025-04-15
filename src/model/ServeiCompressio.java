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

        Model model = controlador.getModel();

        model.setArrelHuffman(arrel);

        Map<Character, String> codis = new HashMap<>();
        arbre.generarCodis(arrel, "", codis);

        try (FileOutputStream out = new FileOutputStream(fitxerSortida);
             ObjectOutputStream oos = new ObjectOutputStream(out)) {

            // 1. Guardam la taula de codis com a objecte (serialitzat)
            oos.writeObject(codis);

            // 2. Convertim text a bits
            StringBuilder bits = new StringBuilder();
            for (char c : textOriginal.toCharArray()) {
                bits.append(codis.get(c));
            }

            // 3. Escriure els bits agrupats com a bytes
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int index = 0;
            while (index + 8 <= bits.length()) {
                String byteStr = bits.substring(index, index + 8);
                buffer.write((byte) Integer.parseInt(byteStr, 2));
                index += 8;
            }

            // 4. Si queden bits solts (no arriben a 8), afegim-los i anotem la quantitat
            int restants = bits.length() - index;
            String restantsBits = bits.substring(index);
            int padding = 8 - restants;

            if (restants > 0) {
                String byteStr = restantsBits + "0".repeat(padding);
                buffer.write((byte) Integer.parseInt(byteStr, 2));
            }

            // 5. Guardar el padding final (per descomprimir correctament)
            oos.writeInt(padding);

            // 6. Escriure el bloc binari
            buffer.writeTo(out);

            controlador.notificar(Notificacio.COMPRESSIO_COMPLETA);

        } catch (IOException e) {
            controlador.notificar(Notificacio.ERROR);
        }
    }

    public void descomprimir(File fitxerComprès, File fitxerSortida) {
        try (FileInputStream in = new FileInputStream(fitxerComprès);
             ObjectInputStream ois = new ObjectInputStream(in)) {

            // 1. Llegim la taula de codis (serialitzada)
            Map<Character, String> codis = (Map<Character, String>) ois.readObject();

            // 2. Construir codis inversos
            Map<String, Character> codisInvers = new HashMap<>();
            for (Map.Entry<Character, String> entrada : codis.entrySet()) {
                codisInvers.put(entrada.getValue(), entrada.getKey());
            }

            // 3. Llegim el padding final
            int padding = ois.readInt();

            // 4. Llegim tot el bloc binari restant
            ByteArrayOutputStream bytesCodificats = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1) {
                bytesCodificats.write(b);
            }

            // 5. Reconstruïm la cadena de bits
            StringBuilder bits = new StringBuilder();
            byte[] bytes = bytesCodificats.toByteArray();
            for (int i = 0; i < bytes.length; i++) {
                String binari = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
                bits.append(binari);
            }

            // 6. Eliminem padding
            if (padding > 0 && bits.length() >= padding) {
                bits.setLength(bits.length() - padding);
            }

            // 7. Decodificació
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

}
