package vista;

import model.NodeHuffman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class PanellArbre extends JPanel {

    private NodeVisual arrelVisual;
    private double escala = 1.0;
    private Point arrossegantDesDe = null;
    private int offsetX = 0, offsetY = 0;
    private int posicioXActual = 0;

    public PanellArbre() {
        setBackground(Color.WHITE);

        // Zoom amb scroll
        addMouseWheelListener(e -> {
            escala *= (e.getWheelRotation() < 0) ? 1.1 : 0.9;
            escala = Math.max(0.1, Math.min(escala, 5.0));
            repaint();
        });

        // Arrossegar
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
        this.offsetX = 0;
        this.offsetY = 0;
        this.escala = 1.0;
        this.posicioXActual = 0;

        if (arrel != null) {
            arrelVisual = construirArbreVisual(arrel, 0);
        } else {
            arrelVisual = null;
        }
    }

    private NodeVisual construirArbreVisual(NodeHuffman node, int profunditat) {
        if (node == null) return null;

        NodeVisual esquerra = construirArbreVisual(node.getNodeEsquerra(), profunditat + 1);
        NodeVisual dreta = construirArbreVisual(node.getNodeDreta(), profunditat + 1);

        int x;
        if (esquerra == null && dreta == null) {
            x = posicioXActual++ * 80;
        } else if (esquerra != null && dreta != null) {
            x = (esquerra.getX() + dreta.getX()) / 2;
        } else if (esquerra != null) {
            x = esquerra.getX();
        } else {
            x = dreta.getX();
        }

        int y = profunditat * 80;
        NodeVisual visual = new NodeVisual(node, x, y);
        visual.setEsquerra(esquerra);
        visual.setDreta(dreta);
        return visual;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        if (arrelVisual != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform original = g2.getTransform();

            g2.translate(offsetX, offsetY);
            g2.scale(escala, escala);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dibuixarNodeVisual(g2, arrelVisual);

            g2.setTransform(original);
        }
    }

    private void dibuixarNodeVisual(Graphics2D g, NodeVisual node) {
        if (node == null) return;

        int r = 25;

        // Connexions
        if (node.getEsquerra() != null) {
            g.setColor(Color.BLACK);
            g.drawLine(node.getX(), node.getY(), node.getEsquerra().getX(), node.getEsquerra().getY());
            g.drawString("0", (node.getX() + node.getEsquerra().getX()) / 2 - 10,
                              (node.getY() + node.getEsquerra().getY()) / 2);
            dibuixarNodeVisual(g, node.getEsquerra());
        }

        if (node.getDreta() != null) {
            g.setColor(Color.BLACK);
            g.drawLine(node.getX(), node.getY(), node.getDreta().getX(), node.getDreta().getY());
            g.drawString("1", (node.getX() + node.getDreta().getX()) / 2 + 5,
                              (node.getY() + node.getDreta().getY()) / 2);
            dibuixarNodeVisual(g, node.getDreta());
        }

        // Dibuixar el node
        String text = node.getDada().esFulla()
                ? "'" + node.getDada().getSimbol() + "':" + node.getDada().getFrequencia()
                : String.valueOf(node.getDada().getFrequencia());

        g.setColor(node.getDada().esFulla() ? new Color(173, 216, 230) : new Color(200, 200, 200));
        g.fillOval(node.getX() - r, node.getY() - r, 2 * r, 2 * r);
        g.setColor(Color.BLACK);
        g.drawOval(node.getX() - r, node.getY() - r, 2 * r, 2 * r);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, node.getX() - textWidth / 2, node.getY() + textHeight / 4);
    }
}
