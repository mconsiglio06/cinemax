package cinemax;

public class DateFormatException extends RuntimeException {
    public DateFormatException() {
        super("ERROR! The given date's format is not accepted.");
    }
}
