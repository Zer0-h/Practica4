package vista;

import controlador.Controlador;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {

    private final Controlador controlador;
    private final PanellBotons panellBotons;
    private final PanellArbre panellArbre;

    public Vista(Controlador controlador) {
        super("Compressor Huffman");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panellBotons = new PanellBotons(controlador);
        panellArbre = new PanellArbre();

        add(panellBotons, BorderLayout.NORTH);
        add(panellArbre, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null); // Centrat
    }

    public void mostrar() {
        setVisible(true);
    }

    public void mostrarMissatge(String missatge) {
        JOptionPane.showMessageDialog(this, missatge);
    }

    public PanellArbre getPanellArbre() {
        return panellArbre;
    }
}
