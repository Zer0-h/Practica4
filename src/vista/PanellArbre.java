package vista;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.*;
import model.huffman.NodeHuffman;

/**
 * Panell Swing encarregat de dibuixar visualment l'arbre de Huffman.
 * Permet fer zoom i moure la vista arrossegant el ratolí.
 *
 * @author tonitorres
 */
public class PanellArbre extends JPanel {

    private NodeVisual arrelVisual;        // Arrel visual de l’arbre
    private double escala = 1.0;           // Escala de zoom
    private Point arrossegantDesDe = null; // Punt d'inici d'arrossegament
    private int offsetX = 0, offsetY = 0;  // Desplaçament de la vista
    private int posicioXActual = 0;        // Control de posicions X durant el dibuix

    /**
     * Constructor: configura el panell, zoom i esdeveniments d’arrossegament.
     */
    public PanellArbre() {
        setBackground(Color.WHITE);

        // Gestió del zoom amb la roda del ratolí
        addMouseWheelListener(e -> {
            escala *= (e.getWheelRotation() < 0) ? 1.1 : 0.9;
            escala = Math.max(0.1, Math.min(escala, 5.0));
            repaint();
        });

        // Detectar l’inici de l’arrossegament
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                arrossegantDesDe = e.getPoint();
            }
        });

        // Actualitzar la posició durant l’arrossegament
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

    /**
     * Estableix un nou arbre a mostrar.
     *
     * @param arrel Arrel de l’arbre Huffman a visualitzar
     */
    public void setArrel(NodeHuffman arrel) {
        posicioXActual = 0;

        if (arrel != null) {
            arrelVisual = construirArbreVisual(arrel, 0);
            Rectangle bounds = calcularDimensions(arrelVisual);

            // Reescalar per ajustar l’arbre al panell
            int amplePanell = getWidth();
            int altPanell = getHeight();

            double escalaX = amplePanell / (double) (bounds.width + 80);
            double escalaY = altPanell / (double) (bounds.height + 80);
            escala = Math.min(escalaX, escalaY) * 0.9;

            // Centrar al panell
            double centreArbreX = (bounds.x + bounds.width / 2.0) * escala;
            double centreArbreY = (bounds.y + bounds.height / 2.0) * escala;

            offsetX = (int) (getWidth() / 2 - centreArbreX);
            offsetY = (int) (getHeight() / 2 - centreArbreY);
        } else {
            arrelVisual = null;
            offsetX = 0;
            offsetY = 0;
            escala = 1.0;
        }

        repaint();
    }

    /**
     * Calcula els límits del dibuix per poder ajustar-lo correctament.
     */
    private Rectangle calcularDimensions(NodeVisual node) {
        ArrayList<Integer> xs = new ArrayList<>();
        ArrayList<Integer> ys = new ArrayList<>();
        recollirCoordenades(node, xs, ys);

        int minX = xs.stream().min(Integer::compareTo).orElse(0);
        int maxX = xs.stream().max(Integer::compareTo).orElse(0);
        int minY = ys.stream().min(Integer::compareTo).orElse(0);
        int maxY = ys.stream().max(Integer::compareTo).orElse(0);

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * Recull totes les coordenades X i Y dels nodes visuals.
     */
    private void recollirCoordenades(NodeVisual node, ArrayList<Integer> xs, ArrayList<Integer> ys) {
        if (node == null) {
            return;
        }
        xs.add(node.getX());
        ys.add(node.getY());
        recollirCoordenades(node.getEsquerra(), xs, ys);
        recollirCoordenades(node.getDreta(), xs, ys);
    }

    /**
     * Construeix de manera recursiva l’arbre visual a partir del model Huffman.
     */
    private NodeVisual construirArbreVisual(NodeHuffman node, int profunditat) {
        if (node == null) {
            return null;
        }

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

    /**
     * Dibuixa tot el panell, incloent els nodes visuals.
     */
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

    /**
     * Dibuixa un node concret i les seves connexions recursivament.
     */
    private void dibuixarNodeVisual(Graphics2D g, NodeVisual node) {
        if (node == null) {
            return;
        }

        int r = 25;

        // Dibuixar connexions i etiquetes 0/1
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

        // Dibuixar el node (cercle i text)
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
