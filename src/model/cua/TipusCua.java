package model.cua;

public enum TipusCua {
    BINARY_HEAP("Heap binari"),
    FIBONACCI_HEAP("Heap fibonacci"),
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
