package vista;

import javax.swing.*;
import java.awt.*;

public class PanellEstadistiques extends JPanel {

    private final JTextArea areaEstadistiques;

    public PanellEstadistiques() {
        setLayout(new BorderLayout());
        areaEstadistiques = new JTextArea(6, 40);
        areaEstadistiques.setEditable(false);
        areaEstadistiques.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaEstadistiques.setBorder(BorderFactory.createTitledBorder("Estadístiques"));

        JScrollPane scrollPane = new JScrollPane(areaEstadistiques);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void mostrarEstadistiques(String text) {
        areaEstadistiques.setText(text);
    }

    public void netejar() {
        areaEstadistiques.setText("");
    }
}
