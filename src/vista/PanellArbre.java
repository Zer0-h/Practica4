package vista;

import javax.swing.*;
import java.awt.*;

public class PanellArbre extends JPanel {

    public PanellArbre() {
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Placeholder: aquí es podrà dibuixar l’arbre de Huffman en el futur.
        g.drawString("Arbre de Huffman (visualització pendent)", 20, 20);
    }

    // Mètodes per rebre i visualitzar l’arbre es poden afegir aquí.
}
