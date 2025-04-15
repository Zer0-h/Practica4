package vista;

import controlador.Controlador;
import java.awt.*;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import model.Test;
import model.Model;
import model.Utils;
import model.NodeHuffman;

/**
 * Classe Vista: Interfície gràfica de l'aplicació
 * S'encarrega de mostrar els elements gràfics, gestionar els panells
 * i interactuar amb el controlador.
 *
 * @author tonitorres
 */
public class Vista extends JFrame {

    private final Controlador controlador;
    private final Utils model;

    // Panells de la vista
    private TopPanel topPanel;
    private BotonsPanel bottomPanel;
    private ArbrePanel graphPanel;
    private ComparativaVista comparativaVista;

    /**
     * Constructor que inicialitza la vista amb el controlador.
     *
     * @param controlador Controlador de l'aplicació.
     */
    public Vista(Controlador controlador) {
        this.controlador = controlador;
        this.model = controlador.getModel();
    }

    /**
     * Configura i mostra la finestra principal.
     */
    public void mostrar() {
        setTitle("Pràctica 3 - Divideix i Venceràs");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1500, 900);
        setLocationRelativeTo(null);

        // Panell superior amb les opcions de configuració
        topPanel = new TopPanel(this);
        add(topPanel, BorderLayout.NORTH);

        // Panell central per al gràfic
        graphPanel = new ArbrePanel(900, 600);
        add(graphPanel, BorderLayout.CENTER);

        // Panell inferior per Model informació i resultats
        bottomPanel = new BotonsPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Ajustar la mida del panell en el model
        model.setMidaPanel(graphPanel.getWidth(), graphPanel.getHeight());
    }

    /**
     * Repinta el panell gràfic.
     */
    public void pintar() {
        graphPanel.repaint();
    }

    /**
     * Actualitza el resultat òptim en el panell inferior.
     */
    public void setMillorResultat() {
        bottomPanel.displaySolution(model.getPuntsSolucio());
    }

    /**
     * Obté el model associat Model la vista.
     *
     * @return Utils de dades.
     */
    public Utils getModel() {
        return model;
    }

    /**
     * Acció en clicar el botó Utils'iniciar el càlcul.
     */
    protected void startClicked() {
        if (model.tePunts()) {
            // Configura el model segons el tipus de problema
            model.setMinimizar(topPanel.getProblema() == NodeHuffman.PROPER);
            model.setMetode(topPanel.getSolucio());

            // Calcula el temps estimat abans Utils'iniciar
            bottomPanel.setTempsEstimat(model.calcularTempsEstimacio());

            // Mostra la barra de progrés i desactiva els botons
            bottomPanel.startProgress();
            toggleInProgress(false);

            // Notifica al controlador per començar el procés
            controlador.iniciarProces();
        }
    }

    /**
     * Acció en clicar el botó de generar punts.
     */
    protected void generatePointsClicked() {
        Model distribucio = topPanel.getDistribucio();
        model.setDistribucio(distribucio);
        model.generarNuvolPunts(topPanel.getQuantitatPunts());
        graphPanel.colocaPunts(model.getPunts());
        pintar();
    }

    /**
     * Acció en clicar el botó de comparativa.
     */
    protected void comparativaClicked() {
        if (model.tePunts()) {
            // Mostra la barra de progrés durant la comparativa
            comparativaVista = new ComparativaVista();
            comparativaVista.setVisible(true);

            model.setMinimizar(topPanel.getProblema() == NodeHuffman.PROPER);

            // Notifica el controlador per iniciar la comparativa
            controlador.comparativaProcessos();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Genera punts abans de fer la comparativa.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Mostra el resultat de la comparativa immediatament després que un procés
     * finalitzi.
     *
     * @param resultat Resultat del procés.
     */
    public void mostrarResultatComparativa(Test resultat) {
        comparativaVista.afegirResultat(resultat);
    }

    /**
     * Finalitza el càlcul i mostra els resultats.
     */
    public void finalitza() {
        bottomPanel.stopProgress();
        toggleInProgress(true);
        graphPanel.dibuixaLineaSolucio(model.getPuntsSolucio());
        bottomPanel.setTempsReal(model.getTemps());
        setMillorResultat();
        pintar();
    }

    /**
     * Activa o desactiva els botons segons l'estat Utils'execució.
     *
     * @param enExecucio Estat Utils'execució (true si s'està executant).
     */
    protected void toggleInProgress(boolean enExecucio) {
        topPanel.toggleInProgress(enExecucio);
    }

    /**
     * Mostra un missatge Utils'error en cas Utils'operació no vàlida.
     */
    public void errorExecucio(String message) {
        toggleInProgress(true);
        bottomPanel.stopProgress();
        JOptionPane.showMessageDialog(
                null,
                message,
                "Error d'execució",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
