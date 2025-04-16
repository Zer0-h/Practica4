package vista;

import javax.swing.*;
import java.awt.*;

public class PanellEstadistiques extends JPanel {

    private final JTextArea areaEstadistiques;
    private final JLabel labelFitxer;

    public PanellEstadistiques() {
        setLayout(new BorderLayout());

        labelFitxer = new JLabel("Cap fitxer carregat");
        labelFitxer.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelFitxer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(labelFitxer, BorderLayout.NORTH);

        areaEstadistiques = new JTextArea(6, 40);
        areaEstadistiques.setEditable(false);
        areaEstadistiques.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaEstadistiques.setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        JScrollPane scrollPane = new JScrollPane(areaEstadistiques);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void mostrarNomIFitxer(String nom, long midaBytes) {
        labelFitxer.setText(String.format("Fitxer carregat: %s (%,d bytes)", nom, midaBytes));
    }

    public void mostrarEstadistiques(String text) {
        areaEstadistiques.setText(text);
    }

    public void netejar() {
        labelFitxer.setText("Cap fitxer carregat");
        areaEstadistiques.setText("");
    }
}
