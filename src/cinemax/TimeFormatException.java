package cinemax;

public class TimeFormatException extends Exception {
    public TimeFormatException() {
        super("Formato dell'ora non valido. L'ora deve essere compresa tra 0 e 23, e i minuti tra 0 e 59.");
    }
}
