/**
 * Progetto: CineMax - Laboratorio Interdisciplinare A
 * Anno Accademico: 2025/2026
 * Sede: Varese (VA)
 * * Autori del progetto:
 * - Matteo Consiglio (Matricola: 765479)
 * - Giulia Alessandra Casagrande (Matricola: 765252)
 * - Valeria Ferrigno (Matricola: 766909)
 * - Edoardo Della Torre (Matricola: 766913)
 */

package cinemax;

/**
 * Rappresenta un posto in sala identificato da fila e numero.
 * La fila puo essere espressa come lettera; il metodo {@link #getRowIndex()}
 * converte la fila in indice numerico per accedere alla matrice dei posti.
 */
public class Posto {
    private final String fila;
    private final int numero;

    /**
     * Crea un posto normalizzando la fila in maiuscolo.
     *
     * @param fila fila del posto.
     * @param numero numero del posto nella fila.
     */
    public Posto(String fila, int numero) {
        this.fila = fila.toUpperCase();
        this.numero = numero;
    }

    /**
     * Restituisce la fila del posto.
     *
     * @return fila del posto.
     */
    public String getFila() {
        return fila;
    }

    /**
     * Restituisce il numero del posto.
     *
     * @return numero del posto.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Converte la fila in indice numerico a partire da 1.
     *
     * @return indice della fila, oppure -1 se la fila non e valida.
     */
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
