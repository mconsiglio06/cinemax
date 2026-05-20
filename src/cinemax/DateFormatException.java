package cinemax;

/**
 * Eccezione lanciata quando una data non rispetta il formato o i limiti attesi.
 */
public class DateFormatException extends RuntimeException {
    /**
     * Crea l'eccezione con messaggio standard per data non valida.
     */
    public DateFormatException() {
        super("ERROR! The given date's format is not accepted.");
    }
}
