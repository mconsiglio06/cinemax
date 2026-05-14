package cinemax;

public class Posto {
    private final String fila;
    private final int numero;

    public Posto(String fila, int numero) {
        this.fila = fila.toUpperCase();
        this.numero = numero;
    }

    public String getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public int getRowIndex() {
        if (fila == null || fila.isEmpty()) {
            return -1;
        }
        String normalized = fila.toUpperCase();
        char c = normalized.charAt(0);
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 1;
        }
        try {
            return Integer.parseInt(normalized);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String toString() {
        return fila + "-" + numero;
    }
}
