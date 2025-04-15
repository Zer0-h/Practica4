package vista;

import model.NodeHuffman;

import javax.swing.*;
import java.awt.*;

public class PanellArbre extends JPanel {

    private NodeHuffman arrel;

    public void setArrel(NodeHuffman arrel) {
        this.arrel = arrel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (arrel != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dibuixarArbre(g2, arrel, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private void dibuixarArbre(Graphics2D g, NodeHuffman node, int x, int y, int dx) {
        if (node == null) return;

        // Dibuixa el node actual
        String text = node.esFulla()
                ? "'" + node.getSimbol() + "':" + node.getFrequencia()
                : String.valueOf(node.getFrequencia());
        int mida = 40;
        g.drawOval(x - mida / 2, y - mida / 2, mida, mida);
        g.drawString(text, x - mida / 2 + 5, y + 5);

        // Dibuixa les connexions amb els fills
        if (node.getNodeEsquerra() != null) {
            int xEsq = x - dx;
            int yFill = y + 60;
            g.drawLine(x, y + mida / 2, xEsq, yFill - mida / 2);
            dibuixarArbre(g, node.getNodeEsquerra(), xEsq, yFill, dx / 2);
        }

        if (node.getNodeDreta() != null) {
            int xDret = x + dx;
            int yFill = y + 60;
            g.drawLine(x, y + mida / 2, xDret, yFill - mida / 2);
            dibuixarArbre(g, node.getNodeDreta(), xDret, yFill, dx / 2);
        }
    }
}
