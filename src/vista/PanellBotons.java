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
        selectorCua.setSelectedItem(TipusCua.HEAP);
        botoCarregar = new JButton("Carregar Fitxer");
        botoComprimir = new JButton("Comprimir");
        botoDescomprimir = new JButton("Descomprimir");
        botoGuardar = new JButton("Guardar Comprimit");

        selectorCua.addActionListener(e -> {
            TipusCua tipus = (TipusCua) selectorCua.getSelectedItem();
            controlador.getModel().setTipusCua(tipus);
        });
        botoCarregar.addActionListener(e -> controlador.notificar(Notificacio.CARREGA_FITXER));
        botoComprimir.addActionListener(e -> controlador.notificar(Notificacio.COMPRIMIR));
        botoDescomprimir.addActionListener(e -> controlador.notificar(Notificacio.DESCOMPRIMIR));
        botoGuardar.addActionListener(e -> controlador.notificar(Notificacio.GUARDAR));

        add(new JLabel("Tipus de cua:"));
        add(selectorCua);
        add(botoCarregar);
        add(botoComprimir);
        add(botoDescomprimir);
        add(botoGuardar);
    }
}
