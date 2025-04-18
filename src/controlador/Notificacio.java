package controlador;

/**
 * Enumeració que defineix els diferents tipus de notificacions utilitzades
 * en el patró d'esdeveniments per comunicar canvis entre el Model, la Vista i
 * el Controlador.
 *
 * @author tonitorres
 */
public enum Notificacio {
    CARREGA_FITXER,
    COMPRIMIR,
    DESCOMPRIMIR,
    GUARDAR,
    COMPRESSIO_COMPLETA,
    DESCOMPRESSIO_COMPLETA,
    PINTAR_ARBRE,
    ERROR,
    MOSTRAR_MISSATGE,
}
