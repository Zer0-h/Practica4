package model;

/**
 *
 * @author tonitorres
 */
public enum Model {
    GAUSSIAN("Gaussiana"),
    EXPONENCIAL("Exponencial"),
    UNIFORME("Uniforme");

    private final String description;

    /**
     * Constructor per inicialitzar la descripció de la distribució.
     *
     * @param d La descripció textual del tipus de distribució.
     */
    Model(String d) {
        description = d;
    }

    /**
     * Retorna la descripció del tipus de distribució.
     *
     * @return Una cadena amb la descripció de la distribució.
     */
    @Override
    public String toString() {
        return description;
    }
}
