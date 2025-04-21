package model;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FitxerHuffman {

    /**
     * Llegeix el contingut complet d'un fitxer en format binari.
     * @param fitxer Fixter que volem llegir.
     * @return Array de bytes del fitxer.
     * @throws java.io.IOException
     */
    public static byte[] llegirBytes(File fitxer) throws IOException {
        return Files.readAllBytes(fitxer.toPath());
    }

    /**
     * Escriu un fitxer .huff amb la taula de codis Huffman i les dades codificades.
     *
     * @param fitxerSortida Fitxer de sortida
     * @param codis Taula de codis: byte -> cadena binària
     * @param dadesCodificades Dades ja codificades en forma de byte[]
     * @param padding Nombre de bits de farciment a l’últim byte
     * @throws java.io.IOException
     */
    public static void escriureFitxer(File fitxerSortida,
                                      Map<Byte, String> codis,
                                      byte[] dadesCodificades,
                                      int padding) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fitxerSortida))) {
            dos.writeInt(codis.size());

            for (Map.Entry<Byte, String> entrada : codis.entrySet()) {
                byte simbol = entrada.getKey();
                String codi = entrada.getValue();
                int llargada = codi.length();

                dos.writeByte(simbol);
                dos.writeByte(llargada);

                int index = 0;
                while (index + 8 <= llargada) {
                    String byteStr = codi.substring(index, index + 8);
                    dos.writeByte(Integer.parseInt(byteStr, 2));
                    index += 8;
                }

                if (index < llargada) {
                    String restant = codi.substring(index);
                    restant += "0".repeat(8 - restant.length());
                    dos.writeByte(Integer.parseInt(restant, 2));
                }
            }

            dos.writeByte(padding); // Padding final
            dos.write(dadesCodificades); // Dades codificades reals
        }
    }

    public static EntradaHuffman llegirTaulaIHBits(File fitxer) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fitxer))) {
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

            return new EntradaHuffman(codisInvers, dades.toByteArray(), padding);
        }
    }
}
