package vista;

import controlador.Controlador;
import controlador.Notificacio;

import javax.swing.*;
import java.awt.*;
import model.cua.TipusCua;

public class PanellBotons extends JPanel {

    private final JButton botoCarregar;
    private final JButton botoComprimir;
    private final JButton botoDescomprimir;
    private final JButton botoGuardar;
    private final JComboBox<TipusCua> selectorCua;

    public PanellBotons(Controlador controlador) {
        setLayout(new FlowLayout());

        selectorCua = new JComboBox<>(TipusCua.values());
        selectorCua.setSelectedItem(TipusCua.BINARY_HEAP);
        botoCarregar = new JButton("Carregar Fitxer");
        botoComprimir = new JButton("Comprimir");
        botoGuardar = new JButton("Guardar Comprimit");
        botoDescomprimir = new JButton("Descomprimir");

        selectorCua.addActionListener(e -> {
            TipusCua tipus = (TipusCua) selectorCua.getSelectedItem();
            controlador.getModel().setTipusCua(tipus);
        });
        botoCarregar.addActionListener(e -> controlador.notificar(Notificacio.CARREGA_FITXER));
        botoComprimir.addActionListener(e -> controlador.notificar(Notificacio.COMPRIMIR));
        botoGuardar.addActionListener(e -> controlador.notificar(Notificacio.GUARDAR));
        botoDescomprimir.addActionListener(e -> controlador.notificar(Notificacio.DESCOMPRIMIR));

        add(new JLabel("Tipus de cua:"));
        add(selectorCua);
        add(botoCarregar);
        add(botoComprimir);
        add(botoGuardar);
        add(botoDescomprimir);
    }
}
