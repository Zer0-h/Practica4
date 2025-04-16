package vista;

import controlador.Controlador;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {

    private final Controlador controlador;
    private final PanellBotons panellBotons;
    private final PanellArbre panellArbre;
    private final PanellEstadistiques panellEstadistiques;

    public Vista(Controlador controlador) {
        super("Compressor Huffman");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panellBotons = new PanellBotons(controlador);
        panellArbre = new PanellArbre();
        panellEstadistiques = new PanellEstadistiques();

        add(panellBotons, BorderLayout.NORTH);
        add(panellArbre, BorderLayout.CENTER);
        add(panellEstadistiques, BorderLayout.SOUTH);

        setSize(800, 600);
        setLocationRelativeTo(null); // Centrat
    }

    public void mostrar() {
        setVisible(true);
    }

    public void mostrarMissatge(String missatge) {
        JOptionPane.showMessageDialog(this, missatge);
    }

    public void mostrarEstadistiques(String text) {
        panellEstadistiques.mostrarEstadistiques(text);
    }

    public PanellArbre getPanellArbre() {
        return panellArbre;
    }
}
