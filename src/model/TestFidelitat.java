package model;

import java.io.*;

public class TestFidelitat {

    public static boolean compararFitxers(File original, File descomprimit) throws IOException {
        try (InputStream in1 = new FileInputStream(original);
             InputStream in2 = new FileInputStream(descomprimit)) {

            int byte1, byte2;
            do {
                byte1 = in1.read();
                byte2 = in2.read();
                if (byte1 != byte2) return false;
            } while (byte1 != -1 && byte2 != -1);

            return true;
        }
    }
}
