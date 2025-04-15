package controlador;

/**
 * Enumeració que defineix els diferents tipus de notificacions utilitzades
 * en el patró d'esdeveniments per comunicar canvis entre el Model, la Vista i
 * el Controlador.
 *
 * @author tonitorres
 */
public enum Notificacio {
    FITXER_CARREGAT,
    COMPRESSIO_COMPLETA,
    DESCOMPRESSIO_COMPLETA,
    ERROR,
    RESULTATS,
    TEST_FIDELITAT_OK,
    TEST_FIDELITAT_KO
}
