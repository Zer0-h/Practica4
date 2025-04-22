/**
 * Classe Controlador del patró MVC.
 * Gestiona la comunicació entre el Model i la Vista
 * mitjançant el patró d’esdeveniments.
 *
 * @author tonitorres
 */
package controlador;

import java.io.File;
import javax.swing.*;
import model.Model;
import model.ProcessCompressio;
import model.ProcessDescompressio;
import model.ProcessReconstruccio;
import vista.Vista;

public class Controlador implements Notificar {

    private Model model;
    private Vista vista;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    /**
     * Inicialitza el model i la vista.
     */
    private void inicialitzar() {
        model = new Model();
        vista = new Vista(this);
    }

    /**
     * Gestiona la càrrega d’un fitxer.
     * Si és un fitxer .huff, reconstrueix l’arbre.
     * Si és un fitxer normal, el registra com a fitxer original.
     */
    private void carregarFitxer() {
        JFileChooser selector = new JFileChooser();
        if (selector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fitxer = selector.getSelectedFile();

            if (fitxer.getName().toLowerCase().endsWith(".huff")) {
                model.setFitxerComprès(fitxer);
                new ProcessReconstruccio(this).start();
            } else {
                model.setFitxerOriginal(fitxer);
                vista.mostrarNomFitxerCarregat(fitxer.getName(), fitxer.length());
            }
        }
    }

    /**
     * Inicia el procés de compressió del fitxer carregat.
     */
    private void comprimir() {
        if (model.getFitxerOriginal() == null) {
            mostrarMissatge("Primer has de carregar un fitxer per comprimir.");
            return;
        }

        try {
            File sortida = new File(model.getFitxerOriginal() + ".huff");
            model.setFitxerComprès(sortida);
            new ProcessCompressio(this).start();
        } catch (Exception e) {
            mostrarMissatge("Error durant la compressió.");
        }
    }

    /**
     * Inicia el procés de descompressió del fitxer .huff carregat.
     */
    private void descomprimir() {
        if (model.getFitxerComprès() == null) {
            mostrarMissatge("Has de comprimir o carregar un fitxer .huff primer.");
            return;
        }

        File fitxerDescomprès = new File(model.getFitxerComprès().getName().replace(".huff", ""));
        model.setFitxerDescomprès(fitxerDescomprès);

        try {
            new ProcessDescompressio(this).start();
        } catch (Exception e) {
            mostrarMissatge("Error durant la descompressió.");
        }
    }

    /**
     * Mostra un diàleg per guardar un fitxer.
     *
     * @param origen        Fitxer a guardar.
     * @param nomPerDefecte Nom per defecte del fitxer.
     */
    private void guardar(File origen, String nomPerDefecte) {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File(nomPerDefecte));
        if (selector.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File desti = selector.getSelectedFile();
            origen.renameTo(desti);
            mostrarMissatge("Fitxer guardat com: " + desti.getName());
        }
    }

    /**
     * Mostra un missatge a través de la vista.
     *
     * @param text Missatge a mostrar.
     */
    private void mostrarMissatge(String text) {
        model.setMissatge(text);
        vista.notificar(Notificacio.MOSTRAR_MISSATGE);
    }

    public Model getModel() {
        return model;
    }

    /**
     * Reacciona a les notificacions rebudes segons el patró d’esdeveniments.
     *
     * @param notificacio Tipus d’esdeveniment.
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case CARREGA_FITXER ->
                carregarFitxer();
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
