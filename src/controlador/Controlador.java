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
            model.setMissatge("Primer has de carregar un fitxer per comprimir.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
            return;
        }

        try {
            File sortida = new File(model.getFitxerOriginal() + ".huff");
            model.setFitxerComprès(sortida);
            ProcessCompressio p = new ProcessCompressio(this);
            p.start();
        } catch (Exception e) {
            model.setMissatge("Error durant la compressió.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
        }
    }

    private void descomprimir() {
        if (model.getFitxerComprès() == null) {
            model.setMissatge("Has de comprimir o carregar un fitxer .huff primer.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
            return;
        }

        File fitxerDescomprès = new File(model.getFitxerComprès().getName().replace(".huff", ""));
        model.setFitxerDescomprès(fitxerDescomprès);

        try {
            ProcessDescompressio p = new ProcessDescompressio(this);
            p.start();
        } catch (Exception e) {
            model.setMissatge("Error durant la descompressió.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
        }
    }

    private void guardarFitxer() {
        if (model.getFitxerComprès() == null) {
            model.setMissatge("Cap fitxer comprimit per guardar.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
            return;
        }

        JFileChooser selector = new JFileChooser();

        selector.setSelectedFile(new File(model.getFitxerComprès().getName()));
        int resultat = selector.showSaveDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            File desti = selector.getSelectedFile();
            model.getFitxerComprès().renameTo(desti);
            model.setMissatge("Fitxer guardat com: " + desti.getName());
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
        }
    }

    private void guardarDescomprimit() {
        JFileChooser selector = new JFileChooser();

        selector.setSelectedFile(new File(model.getFitxerDescomprès().getName()));
        int resultat = selector.showSaveDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            File desti = selector.getSelectedFile();
            model.getFitxerDescomprès().renameTo(desti);
            model.setMissatge("Fitxer guardat com: " + desti.getName());
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
        }
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
                guardarFitxer();
            case COMPRESSIO_COMPLETA ->
                vista.notificar(Notificacio.PINTAR_ARBRE);
            case DESCOMPRESSIO_COMPLETA ->
                guardarDescomprimit();
            case ARBRE_RECONSTRUIT ->
                vista.notificar(Notificacio.PINTAR_ARBRE);
            case ERROR ->
                vista.notificar(Notificacio.ERROR);
        }
    }
}
