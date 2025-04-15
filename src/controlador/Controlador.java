package controlador;

import model.ServeiCompressio;
import model.TestFidelitat;
import vista.Vista;

import javax.swing.*;
import java.io.File;

public class Controlador implements Notificar {

    private ServeiCompressio servei;
    private Vista vista;

    private File fitxerOriginal;
    private File fitxerComprès;
    private File fitxerDescomprès;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    public void inicialitzar() {
        this.servei = new ServeiCompressio(this);
        this.vista = new Vista(this);
        this.vista.mostrar();
    }

    public void carregarFitxer() {
        JFileChooser selector = new JFileChooser();
        int resultat = selector.showOpenDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            fitxerOriginal = selector.getSelectedFile();
            vista.mostrarMissatge("Fitxer carregat: " + fitxerOriginal.getName());
            notificar(Notificacio.FITXER_CARREGAT);
        }
    }

    public void comprimir() {
        if (fitxerOriginal == null) {
            vista.mostrarMissatge("Primer has de carregar un fitxer.");
            return;
        }

        try {
            String contingut = servei.llegirFitxer(fitxerOriginal);
            fitxerComprès = new File("comprés.txt");
            servei.comprimir(contingut, fitxerComprès);
        } catch (Exception e) {
            vista.mostrarMissatge("Error durant la compressió.");
        }
    }

    public void descomprimir() {
        // Es pot implementar més endavant
        vista.mostrarMissatge("Funcionalitat de descompressió encara no implementada.");
    }

    public void guardarFitxer() {
        if (fitxerComprès == null) {
            vista.mostrarMissatge("Cap fitxer comprimit per guardar.");
            return;
        }

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("comprés.huff"));
        int resultat = selector.showSaveDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            File desti = selector.getSelectedFile();
            fitxerComprès.renameTo(desti);
            vista.mostrarMissatge("Fitxer guardat com: " + desti.getName());
        }
    }

    public void testFidelitat() {
        if (fitxerOriginal == null || fitxerDescomprès == null) {
            vista.mostrarMissatge("Cal un fitxer original i un descomprimit per fer el test.");
            return;
        }

        try {
            boolean iguals = TestFidelitat.compararFitxers(fitxerOriginal, fitxerDescomprès);
            if (iguals) {
                notificar(Notificacio.TEST_FIDELITAT_OK);
                vista.mostrarMissatge("Els fitxers són idèntics.");
            } else {
                notificar(Notificacio.TEST_FIDELITAT_KO);
                vista.mostrarMissatge("Els fitxers són diferents.");
            }
        } catch (Exception e) {
            notificar(Notificacio.ERROR);
            vista.mostrarMissatge("Error durant el test de fidelitat.");
        }
    }

    @Override
    public void notificar(Notificacio notificacio) {
        System.out.println("Notificació rebuda: " + notificacio);
        // Aquí pots ampliar la gestió d’esdeveniments si ho necessites.
    }
}
