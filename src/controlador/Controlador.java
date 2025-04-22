package controlador;

import vista.Vista;

import javax.swing.*;
import java.io.File;
import model.Model;
import model.ProcessCompressio;
import model.ProcessDescompressio;
import model.ProcessReconstruccio;

public class Controlador implements Notificar {

    private Model model;
    private Vista vista;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    private void inicialitzar() {
        model = new Model();
        vista = new Vista(this);
    }

    private void carregaFitxer() {
        JFileChooser selector = new JFileChooser();
        if (selector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fitxer = selector.getSelectedFile();

            if (fitxer.getName().toLowerCase().endsWith(".huff")) {
                model.setFitxerComprès(fitxer);
                ProcessReconstruccio p = new ProcessReconstruccio(this);
                p.start();
            } else {
                model.setFitxerOriginal(fitxer);
                vista.mostrarNomFitxerCarregat(fitxer.getName(), fitxer.length());
            }
        }
    }

    private void comprimir() {
        if (model.getFitxerOriginal() == null) {
            mostrarMissatge("Primer has de carregar un fitxer per comprimir.");
            return;
        }

        try {
            File sortida = new File(model.getFitxerOriginal() + ".huff");
            model.setFitxerComprès(sortida);
            ProcessCompressio p = new ProcessCompressio(this);
            p.start();
        } catch (Exception e) {
            mostrarMissatge("Error durant la compressió.");
        }
    }

    private void descomprimir() {
        if (model.getFitxerComprès() == null) {
            mostrarMissatge("Has de comprimir o carregar un fitxer .huff primer.");
            return;
        }

        File fitxerDescomprès = new File(model.getFitxerComprès().getName().replace(".huff", ""));
        model.setFitxerDescomprès(fitxerDescomprès);

        try {
            ProcessDescompressio p = new ProcessDescompressio(this);
            p.start();
        } catch (Exception e) {
            mostrarMissatge("Error durant la descompressió.");
        }
    }

    private void guardar(File origen, String nomPerDefecte) {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File(nomPerDefecte));
        if (selector.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File desti = selector.getSelectedFile();
            origen.renameTo(desti);
            mostrarMissatge("Fitxer guardat com: " + desti.getName());
        }
    }

    private void mostrarMissatge(String text) {
        model.setMissatge(text);
        vista.notificar(Notificacio.MOSTRAR_MISSATGE);
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case CARREGA_FITXER ->
                carregaFitxer();
            case COMPRIMIR ->
                comprimir();
            case DESCOMPRIMIR ->
                descomprimir();
            case GUARDAR ->
                guardar(model.getFitxerComprès(), model.getFitxerComprès().getName());
            case COMPRESSIO_COMPLETA ->
                vista.notificar(Notificacio.PINTAR_ARBRE);
            case DESCOMPRESSIO_COMPLETA ->
                guardar(model.getFitxerDescomprès(), model.getFitxerDescomprès().getName());
            case ARBRE_RECONSTRUIT ->
                vista.notificar(Notificacio.PINTAR_ARBRE);
            case ERROR ->
                vista.notificar(Notificacio.ERROR);
        }
    }
}
