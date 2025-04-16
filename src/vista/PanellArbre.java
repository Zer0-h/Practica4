package vista;

import model.NodeHuffman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class PanellArbre extends JPanel {

    private NodeHuffman arrel;
    private double escala = 1.0;
    private Point arrossegantDesDe = null;
    private int offsetX = 0, offsetY = 0;

    public PanellArbre() {
        setBackground(Color.WHITE);

        // Scroll amb la roda del ratolí (zoom)
        addMouseWheelListener(e -> {
            escala *= (e.getWheelRotation() < 0) ? 1.1 : 0.9;
            escala = Math.max(0.1, Math.min(escala, 5.0));
            repaint();
        });

        // Arrossegar per moure
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                arrossegantDesDe = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (arrossegantDesDe != null) {
                    int dx = e.getX() - arrossegantDesDe.x;
                    int dy = e.getY() - arrossegantDesDe.y;
                    offsetX += dx;
                    offsetY += dy;
                    arrossegantDesDe = e.getPoint();
                    repaint();
                }
            }
        });
    }

    public void setArrel(NodeHuffman arrel) {
        this.arrel = arrel;
        this.offsetX = 0;
        this.offsetY = 0;
        this.escala = 1.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (arrel != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform original = g2.getTransform();

            // Aplicar escala i desplaçament
            g2.translate(offsetX, offsetY);
            g2.scale(escala, escala);

            // Dibuixar arbre
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dibuixarArbre(g2, arrel, getWidth() / 2, 30, getWidth() / 4);

            g2.setTransform(original);
        }
    }

    private void dibuixarArbre(Graphics2D g, NodeHuffman node, int x, int y, int dx) {
        if (node == null) return;

        int nextY = y + 60;
        int radi = 25;

        if (node.getNodeEsquerra() != null) {
            int xEsq = x - dx;
            g.setColor(Color.BLACK);
            g.drawLine(x, y, xEsq, nextY);
            g.drawString("0", (x + xEsq) / 2 - 10, (y + nextY) / 2);
            dibuixarArbre(g, node.getNodeEsquerra(), xEsq, nextY, dx / 2);
        }

        if (node.getNodeDreta() != null) {
            int xDret = x + dx;
            g.setColor(Color.BLACK);
            g.drawLine(x, y, xDret, nextY);
            g.drawString("1", (x + xDret) / 2 + 5, (y + nextY) / 2);
            dibuixarArbre(g, node.getNodeDreta(), xDret, nextY, dx / 2);
        }

        // Text del node
        String text = node.esFulla()
                ? "'" + node.getSimbol() + "':" + node.getFrequencia()
                : String.valueOf(node.getFrequencia());

        dibuixarCercle(g, x, y, radi, text, node.esFulla());
    }

    private void dibuixarCercle(Graphics2D g, int x, int y, int r, String text, boolean esFulla) {
        g.setColor(esFulla ? new Color(173, 216, 230) : new Color(200, 200, 200));
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
        g.setColor(Color.BLACK);
        g.drawOval(x - r, y - r, 2 * r, 2 * r);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, x - textWidth / 2, y + textHeight / 4);
    }
}
