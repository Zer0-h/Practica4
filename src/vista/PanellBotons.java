package vista;

import controlador.Controlador;
import controlador.Notificacio;

import javax.swing.*;
import java.awt.*;

public class PanellBotons extends JPanel {

    private final JButton botoCarregar;
    private final JButton botoComprimir;
    private final JButton botoDescomprimir;
    private final JButton botoGuardar;

    public PanellBotons(Controlador controlador) {
        setLayout(new FlowLayout());

        botoCarregar = new JButton("Carregar Fitxer");
        botoComprimir = new JButton("Comprimir");
        botoDescomprimir = new JButton("Descomprimir");
        botoGuardar = new JButton("Guardar Comprimit");

        botoCarregar.addActionListener(e -> controlador.notificar(Notificacio.CARREGA_FITXER));
        botoComprimir.addActionListener(e -> controlador.notificar(Notificacio.COMPRIMIR));
        botoDescomprimir.addActionListener(e -> controlador.notificar(Notificacio.DESCOMPRIMIR));
        botoGuardar.addActionListener(e -> controlador.notificar(Notificacio.GUARDAR));

        add(botoCarregar);
        add(botoComprimir);
        add(botoDescomprimir);
        add(botoGuardar);
    }
}
