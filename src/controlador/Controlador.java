package controlador;

import model.ServeiCompressio;
import vista.Vista;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import model.Model;
import model.NodeHuffman;

public class Controlador implements Notificar {

    private Model model;
    private ServeiCompressio servei;
    private Vista vista;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    public void inicialitzar() {
        servei = new ServeiCompressio(this);
        model = new Model();
        vista = new Vista(this);
    }

    public void carregaFitxer() {
        JFileChooser selector = new JFileChooser();
        if (selector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fitxer = selector.getSelectedFile();

            if (fitxer.getName().toLowerCase().endsWith(".huff")) {
                try {
                    model.setFitxerComprès(fitxer);
                    NodeHuffman arrel = servei.reconstruirArbreDesDeFitxerHuff(fitxer);
                    model.setArrelHuffman(arrel);

                    vista.notificar(Notificacio.PINTAR_ARBRE);
                } catch (IOException e) {
                    model.setMissatge("Error en llegir el fitxer .huff");
                    vista.notificar(Notificacio.MOSTRAR_MISSATGE);
                }
            } else {
                model.setFitxerOriginal(fitxer);
                vista.mostrarNomFitxerCarregat(fitxer.getName(), fitxer.length());
            }
        }
    }

    public void comprimir() {
        if (model.getFitxerOriginal() == null) {
            model.setMissatge("Primer has de carregar un fitxer per comprimir.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
            return;
        }

        try {
            String contingut = servei.llegirFitxer(model.getFitxerOriginal());
            File sortida = new File(model.getFitxerOriginal() + ".huff");
            servei.comprimir(contingut, sortida);

        } catch (Exception e) {
            model.setMissatge("Error durant la compressió.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
        }
    }

    public void descomprimir() {
        if (model.getFitxerComprès() == null) {
            model.setMissatge("Has de comprimir o carregar un fitxer .huff primer.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
            return;
        }

        File fitxerDescomprès = new File(model.getFitxerComprès().getName().replace(".huff", ""));
        try {
            servei.descomprimir(model.getFitxerComprès(), fitxerDescomprès);
            model.setFitxerDescomprès(fitxerDescomprès);

            // Guardam
            JFileChooser selector = new JFileChooser();

            selector.setSelectedFile(new File(model.getFitxerDescomprès().getName()));
            int resultat = selector.showSaveDialog(null);
            if (resultat == JFileChooser.APPROVE_OPTION) {
                File desti = selector.getSelectedFile();
                model.getFitxerDescomprès().renameTo(desti);
                model.setMissatge("Fitxer guardat com: " + desti.getName());
                vista.notificar(Notificacio.MOSTRAR_MISSATGE);
            }

        } catch (Exception e) {
                                    model.setMissatge("Error durant la descompressió.");
            vista.notificar(Notificacio.MOSTRAR_MISSATGE);
        }
    }

    public void guardarFitxer() {
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

    public Model getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case Notificacio.CARREGA_FITXER ->
                carregaFitxer();
            case Notificacio.COMPRIMIR ->
                comprimir();
            case Notificacio.DESCOMPRIMIR ->
                descomprimir();
            case Notificacio.GUARDAR ->
                guardarFitxer();
            case Notificacio.COMPRESSIO_COMPLETA ->
                vista.notificar(Notificacio.PINTAR_ARBRE);
            case Notificacio.ERROR ->
                vista.notificar(Notificacio.ERROR);
        }
    }
}
