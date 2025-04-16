package controlador;

import model.ServeiCompressio;
import model.TestFidelitat;
import vista.Vista;

import javax.swing.*;
import java.io.File;
import model.Model;

public class Controlador implements Notificar {

    private Model model;
    private ServeiCompressio servei;
    private Vista vista;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    public void inicialitzar() {
        this.servei = new ServeiCompressio(this);
        this.vista = new Vista(this);
        this.model = new Model();
        this.vista.mostrar();
    }

    public void carregarFitxer() {
        JFileChooser selector = new JFileChooser();
        if (selector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fitxer = selector.getSelectedFile();
            model.setFitxerOriginal(fitxer);
            vista.mostrarMissatge("Fitxer carregat: " + fitxer.getName());
            notificar(Notificacio.FITXER_CARREGAT);
        }
    }

    public void comprimir() {
        if (model.getFitxerOriginal() == null) {
            vista.mostrarMissatge("Primer has de carregar un fitxer.");
            return;
        }

        try {
            String contingut = servei.llegirFitxer(model.getFitxerOriginal());
            File sortida = new File("comprés.txt");
            servei.comprimir(contingut, sortida);
            model.setFitxerComprès(sortida);
            vista.getPanellArbre().setArrel(model.getArrelHuffman());
            vista.getPanellArbre().repaint();
            vista.mostrarEstadistiques(String.format("""
                📊 Estadístiques de compressió
                ------------------------------
                Mida original: %d bytes
                Mida comprimida: %d bytes
                Temps de compressió: %d ms
                Longitud mitjana del codi: %.3f bits/símbol
                Taxa de compressió: %.2f %%
                """,
                model.getFitxerOriginal().length(),
                model.getFitxerComprès().length(),
                model.getTempsCompressioMs(),
                model.getLongitudMitjanaCodi(),
                model.getTaxaCompressio()
            ));

        } catch (Exception e) {
            vista.mostrarMissatge("Error durant la compressió.");
        }
    }

    public void descomprimir() {
        if (model.getFitxerComprès() == null) {
            vista.mostrarMissatge("Has de comprimir o carregar un fitxer .huff primer.");
            return;
        }

        File fitxerDescomprès = new File("descomprès.txt");
        try {
            servei.descomprimir(model.getFitxerComprès(), fitxerDescomprès);
            model.setFitxerDescomprès(fitxerDescomprès);
            vista.mostrarMissatge("Fitxer descomprimit correctament.");
        } catch (Exception e) {
            vista.mostrarMissatge("Error durant la descompressió.");
        }
    }

    public void guardarFitxer() {
        if (model.getFitxerComprès() == null) {
            vista.mostrarMissatge("Cap fitxer comprimit per guardar.");
            return;
        }

        JFileChooser selector = new JFileChooser();

        String nomSenseExtensio = model.getFitxerOriginal().getName().replaceFirst("[.][^.]+$", "");

        selector.setSelectedFile(new File(nomSenseExtensio + "_comprés.huff"));
        int resultat = selector.showSaveDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            File desti = selector.getSelectedFile();
            model.getFitxerComprès().renameTo(desti);
            vista.mostrarMissatge("Fitxer guardat com: " + desti.getName());
        }
    }

    public void testFidelitat() {
        if (model.getFitxerOriginal() == null || model.getFitxerDescomprès() == null) {
            vista.mostrarMissatge("Cal un fitxer original i un descomprimit per fer el test.");
            return;
        }

        try {
            boolean iguals = TestFidelitat.compararFitxers(
                model.getFitxerOriginal(),
                model.getFitxerDescomprès()
            );
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

    public Model getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio notificacio) {
        System.out.println("Notificació rebuda: " + notificacio);
        // Aquí pots ampliar la gestió d’esdeveniments si ho necessites.
    }
}
