package vista;

import controlador.Controlador;
import controlador.Notificacio;
import controlador.Notificar;
import java.awt.*;
import javax.swing.*;
import model.Model;

/**
 * Classe principal de la interfície gràfica de l'aplicació.
 * Implementa la vista del patró MVC i mostra l'estat de l'aplicació a l'usuari.
 * Aquesta classe rep notificacions del controlador i actualitza la GUI en
 * conseqüència.
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    private final Controlador controlador;
    private final PanellBotons panellBotons;
    private final PanellArbre panellArbre;
    private final PanellEstadistiques panellEstadistiques;

    /**
     * Constructor que inicialitza i construeix la finestra principal.
     *
     * @param c controlador associat a la vista
     */
    public Vista(Controlador c) {
        super("Compressor Huffman");
        controlador = c;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Components de la vista
        panellBotons = new PanellBotons(controlador);
        panellArbre = new PanellArbre();
        panellEstadistiques = new PanellEstadistiques();

        // Disposició dels panells
        add(panellBotons, BorderLayout.NORTH);
        add(panellArbre, BorderLayout.CENTER);
        add(panellEstadistiques, BorderLayout.SOUTH);

        // Configuració de la finestra
        setSize(1200, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Mostra un diàleg amb un missatge informatiu.
     */
    public void mostrarMissatge() {
        JOptionPane.showMessageDialog(this, controlador.getModel().getMissatge());
    }

    /**
     * Mostra les estadístiques de compressió a l'àrea inferior de la GUI.
     */
    public void mostrarEstadistiquesCompressio() {
        Model model = controlador.getModel();

        String text = String.format("""
            Mida comprimida: %d bytes
            Temps de compressió: %d ms
            Longitud mitjana del codi: %.3f bits/símbol
            Taxa de compressió: %.2f %%
            """,
                model.getFitxerComprès().length(),
                model.getTempsCompressioMs(),
                model.getLongitudMitjanaCodi(),
                model.getTaxaCompressio()
        );

        panellEstadistiques.mostrarEstadistiques(text);
    }

    /**
     * Mostra el nom i mida del fitxer carregat a l'àrea superior.
     *
     * @param nom  nom del fitxer
     * @param mida mida en bytes
     */
    public void mostrarNomFitxerCarregat(String nom, long mida) {
        panellEstadistiques.mostrarNomIFitxer(nom, mida);
    }

    /**
     * Actualitza el panell central amb l'arbre de Huffman i mostra
     * estadístiques.
     */
    public void pintarArbre() {
        panellArbre.setArrel(controlador.getModel().getArrelHuffman());
        panellArbre.repaint();
        mostrarEstadistiquesCompressio();
    }

    /**
     * Mostra un missatge d'error genèric.
     */
    public void error() {
        JOptionPane.showMessageDialog(this, "Hi ha hagut un error a l'hora de comprimir o descomprimir el fitxer. Consulta la consola per més informació.");
    }

    /**
     * Rep notificacions del controlador i executa l'acció corresponent.
     *
     * @param notificacio tipus d'esdeveniment
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case PINTAR_ARBRE ->
                pintarArbre();
            case ERROR ->
                error();
            case MOSTRAR_MISSATGE ->
                mostrarMissatge();
        }
    }
}
