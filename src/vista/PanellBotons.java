package vista;

import controlador.Controlador;
import controlador.Notificacio;
import java.awt.*;
import javax.swing.*;
import model.cua.TipusCua;

/**
 * Panell de control que conté els botons per carregar, comprimir, descomprimir
 * i guardar fitxers, així com un selector per triar el tipus de cua de
 * prioritats.
 *
 * @author tonitorres
 */
public class PanellBotons extends JPanel {

    private final JButton botoCarregar;
    private final JButton botoComprimir;
    private final JButton botoDescomprimir;
    private final JButton botoGuardar;
    private final JComboBox<TipusCua> selectorCua;

    /**
     * Constructor que crea i configura tots els components del panell.
     *
     * @param controlador Referència al controlador per emetre notificacions
     */
    public PanellBotons(Controlador controlador) {
        setLayout(new FlowLayout());

        // ComboBox per seleccionar el tipus de cua de prioritat
        selectorCua = new JComboBox<>(TipusCua.values());
        selectorCua.setSelectedItem(TipusCua.BINARY_HEAP);

        // Botons principals de control
        botoCarregar = new JButton("Carregar Fitxer");
        botoComprimir = new JButton("Comprimir");
        botoGuardar = new JButton("Guardar Comprimit");
        botoDescomprimir = new JButton("Descomprimir");

        // Accions dels components
        selectorCua.addActionListener(e -> {
            TipusCua tipus = (TipusCua) selectorCua.getSelectedItem();
            controlador.getModel().setTipusCua(tipus);
        });

        botoCarregar.addActionListener(e -> controlador.notificar(Notificacio.CARREGA_FITXER));
        botoComprimir.addActionListener(e -> controlador.notificar(Notificacio.COMPRIMIR));
        botoGuardar.addActionListener(e -> controlador.notificar(Notificacio.GUARDAR));
        botoDescomprimir.addActionListener(e -> controlador.notificar(Notificacio.DESCOMPRIMIR));

        // Afegir components al panell
        add(new JLabel("Tipus de cua:"));
        add(selectorCua);
        add(botoCarregar);
        add(botoComprimir);
        add(botoGuardar);
        add(botoDescomprimir);
    }
}
