package cinemax;

public class DateFormatException extends RuntimeException {
    public DateFormatException() {
        super("Errore: Il formato della data è errato. Utilizzare il formato 'dd/MM/yyyy'.");
    }
}
