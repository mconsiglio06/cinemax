package cinemax;

/**
 * Eccezione lanciata quando un orario non rispetta il formato o i limiti attesi.
 */
public class TimeFormatException extends Exception {
    /**
     * Crea l'eccezione con messaggio standard per orario non valido.
     */
    public TimeFormatException() {
        super("L'orario inserito non è valido.");
    }
    
}
