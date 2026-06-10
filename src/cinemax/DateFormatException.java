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
 * Eccezione lanciata quando una data non rispetta il formato o i limiti attesi.
 */
public class DateFormatException extends Exception {
    /**
     * Crea l'eccezione con messaggio standard per data non valida.
     */
    public DateFormatException() {
        super("ERROR! The given date's format is not accepted.");
    }
}
