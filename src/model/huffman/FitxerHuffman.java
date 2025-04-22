package model.huffman;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitària encarregada de llegir i escriure fitxers Huffman.
 * Permet serialitzar i deserialitzar la taula de codis i les dades codificades.
 *
 * @author tonitorres
 */
public class FitxerHuffman {

    /**
     * Llegeix el contingut complet d’un fitxer binari.
     *
     * @param fitxer Fitxer a llegir
     *
     * @return Array de bytes amb el contingut del fitxer
     *
     * @throws IOException Si hi ha algun problema de lectura
     */
    public static byte[] llegirBytes(File fitxer) throws IOException {
        return Files.readAllBytes(fitxer.toPath());
    }

    /**
     * Escriu un fitxer .huff amb la taula de codis Huffman i les dades
     * codificades.
     *
     * Format:
     * - Enter (int) amb el nombre d’entrades a la taula
     * - Per cada entrada:
     * - Byte amb el símbol
     * - Byte amb la llargada del codi
     * - Seqüència de bytes amb el codi (zero-padded)
     * - Byte amb el padding final
     * - Dades codificades (byte[])
     *
     * @param fitxerSortida    Fitxer on escriure
     * @param codis            Taula de codis Huffman (símbol → codi binari)
     * @param dadesCodificades Contingut codificat en bytes
     * @param padding          Nombre de bits de farciment a l’últim byte
     *
     * @throws IOException Si hi ha problemes d’escriptura
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

            dos.writeByte(padding);                 // Bits de farciment
            dos.write(dadesCodificades);            // Dades codificades
        }
    }

    /**
     * Llegeix un fitxer .huff i reconstrueix la taula de codis i les dades.
     *
     * @param fitxer Fitxer .huff
     *
     * @return EntradaHuffman amb la taula de codis, dades i padding
     *
     * @throws IOException Si hi ha algun problema de lectura
     */
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
