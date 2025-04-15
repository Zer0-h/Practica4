package vista;

import controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PanellBotons extends JPanel {

    private final JButton botoCarregar;
    private final JButton botoComprimir;
    private final JButton botoDescomprimir;
    private final JButton botoGuardar;
    private final JButton botoTestFidelitat;

    public PanellBotons(Controlador controlador) {
        setLayout(new FlowLayout());

        botoCarregar = new JButton("Carregar Fitxer");
        botoComprimir = new JButton("Comprimir");
        botoDescomprimir = new JButton("Descomprimir");
        botoGuardar = new JButton("Guardar Comprimits");
        botoTestFidelitat = new JButton("Test Fidelitat");

        botoCarregar.addActionListener(e -> controlador.carregarFitxer());
        botoComprimir.addActionListener(e -> controlador.comprimir());
        botoDescomprimir.addActionListener(e -> controlador.descomprimir());
        botoGuardar.addActionListener(e -> controlador.guardarFitxer());
        botoTestFidelitat.addActionListener(e -> controlador.testFidelitat());

        add(botoCarregar);
        add(botoComprimir);
        add(botoDescomprimir);
        add(botoGuardar);
        add(botoTestFidelitat);
    }
}
