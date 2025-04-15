package model;

import java.io.*;

public class UtilitatsFitxer {

    public static String llegirText(File fitxer) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(new FileReader(fitxer))) {
            String linia;
            while ((linia = lector.readLine()) != null) {
                sb.append(linia).append("\n");
            }
        }
        return sb.toString().trim();
    }

    public static void escriureText(String contingut, File fitxer) throws IOException {
        try (BufferedWriter escriptor = new BufferedWriter(new FileWriter(fitxer))) {
            escriptor.write(contingut);
        }
    }
}
