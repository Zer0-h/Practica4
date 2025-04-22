package vista;

import java.awt.*;
import javax.swing.*;

/**
 * Panell dedicat a mostrar informació del fitxer carregat i estadístiques
 * de compressió, com la mida final, la taxa de compressió, etc.
 *
 * @author tonitorres
 */
public class PanellEstadistiques extends JPanel {

    private final JTextArea areaEstadistiques;
    private final JLabel labelFitxer;

    /**
     * Constructor que inicialitza i configura els components del panell.
     */
    public PanellEstadistiques() {
        setLayout(new BorderLayout());

        // Etiqueta superior amb el nom i mida del fitxer
        labelFitxer = new JLabel("Cap fitxer carregat");
        labelFitxer.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelFitxer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(labelFitxer, BorderLayout.NORTH);

        // Àrea de text per mostrar les estadístiques
        areaEstadistiques = new JTextArea(6, 40);
        areaEstadistiques.setEditable(false);
        areaEstadistiques.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaEstadistiques.setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        // Scroll per l'àrea d'estadístiques
        JScrollPane scrollPane = new JScrollPane(areaEstadistiques);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Mostra el nom i la mida del fitxer a la part superior.
     *
     * @param nom       Nom del fitxer
     * @param midaBytes Mida en bytes
     */
    public void mostrarNomIFitxer(String nom, long midaBytes) {
        labelFitxer.setText(String.format("Fitxer carregat: %s (%,d bytes)", nom, midaBytes));
    }

    /**
     * Mostra les estadístiques de compressió a l'àrea de text.
     *
     * @param text Estadístiques formatades
     */
    public void mostrarEstadistiques(String text) {
        areaEstadistiques.setText(text);
    }

    /**
     * Reinicialitza el panell, buidant les dades mostrades.
     */
    public void netejar() {
        labelFitxer.setText("Cap fitxer carregat");
        areaEstadistiques.setText("");
    }
}
