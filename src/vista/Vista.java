package vista;

import controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import model.Model;

public class Vista extends JFrame {

    private final Controlador controlador;
    private final PanellBotons panellBotons;
    private final PanellArbre panellArbre;
    private final PanellEstadistiques panellEstadistiques;

    public Vista(Controlador c) {
        super("Compressor Huffman");
        controlador = c;

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

    public void mostrarEstadistiquesCompressio() {
        Model model = controlador.getModel();

        String text = String.format("""
            Mida comprimida: %d bytes
            Temps de compressió: %d ms
            Longitud mitjana del codi: %.3f bits/símbol
            Taxa de compressió: %.2f %%
            """,
            model.getFitxerComprès().length(),
            model.getTempsCompressioMs(),
            model.getLongitudMitjanaCodi(),
            model.getTaxaCompressio()
        );

        panellEstadistiques.mostrarEstadistiques(text);
    }

    public void mostrarNomFitxerCarregat(String nom, long mida) {
        panellEstadistiques.mostrarNomIFitxer(nom, mida);
    }

    public PanellArbre getPanellArbre() {
        return panellArbre;
    }
}
