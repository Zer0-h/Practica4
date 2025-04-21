package model.cua;

public enum TipusCua {
    HEAP("Heap binari"),
    ORDENADA("Llista ordenada"),
    NO_ORDENADA("Llista no ordenada");

    private final String descripcio;

    TipusCua(String d) {
        descripcio = d;
    }

    @Override
    public String toString() {
        return descripcio;
    }
}
