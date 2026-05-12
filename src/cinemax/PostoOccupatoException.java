package cinemax;

public class PostoOccupatoException extends Exception {
    PostoOccupatoException(String message) {
        super(message);
    }

    PostoOccupatoException(int f, int p) {
        super("Spiacente, il posto alla fila " + (f+1) + " numero " + (p+1) + " è già occupato.");
    }

    PostoOccupatoException() {
        super("Il posto è già occupato.");
    }
}
