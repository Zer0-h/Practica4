package controlador;

/**
 * Enumeració que defineix els diferents tipus de notificacions utilitzades
 * en el patró d'esdeveniments per comunicar canvis entre el Model, la Vista i
 * el Controlador.
 *
 * Aquestes notificacions permeten desacoblar les accions entre capes,
 * fent servir missatges per indicar què ha passat dins el sistema.
 *
 * @author tonitorres
 */
public enum Notificacio {
    /** S'ha sol·licitat carregar un fitxer des del sistema de fitxers. */
    CARREGA_FITXER,
    /** L'usuari ha
     * demanat comprimir el fitxer carregat. */
    COMPRIMIR,
    /** L'usuari ha
     * demanat descomprimir un fitxer .huff. */
    DESCOMPRIMIR,
    /** L'usuari
     * ha demanat guardar un fitxer (comprimit o descomprimit). */
    GUARDAR,
    /** El procés de
     * compressió ha finalitzat amb èxit. */
    COMPRESSIO_COMPLETA,
    /** El procés de
     * descompressió ha finalitzat amb èxit. */
    DESCOMPRESSIO_COMPLETA,
    /** S'ha de
     * pintar l'arbre de Huffman a la interfície gràfica. */
    PINTAR_ARBRE,
    /** S'ha produït un
     * error durant algun dels processos. */
    ERROR,
    /** Cal mostrar un
     * missatge informatiu a l'usuari. */
    MOSTRAR_MISSATGE,
    /** L'arbre
     * de Huffman ha estat reconstruït a partir d'un fitxer .huff. */
    ARBRE_RECONSTRUIT
}
