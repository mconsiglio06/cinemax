package cinemax;

/**
 * Classe utilitaria per la gestione della sicurezza, in particolare la cifratura delle password.
 * Utilizza il cifrario di Cesare per scopo dimostrativo.
 */
public class SecurityUtils {
    private static final int SHIFT = 3;  // Shift del cifrario di Cesare

    /**
     * Costruttore privato per impedire l'istanza della classe utility.
     */
    private SecurityUtils() {
    }

    /**
     * Cifra una password usando il cifrario di Cesare
     * @param password la password da cifrare
     * @return la password cifrata
     */
    public static String hashPassword(String password) {
        StringBuilder encrypted = new StringBuilder();
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int offset = c - base;
                int newOffset = (offset + SHIFT) % 26;
                encrypted.append((char) (base + newOffset));
            } else if (Character.isDigit(c)) {
                int digit = c - '0';
                int newDigit = (digit + SHIFT) % 10;
                encrypted.append((char) ('0' + newDigit));
            } else {
                // Caratteri speciali rimangono uguali
                encrypted.append(c);
            }
        }
        
        return encrypted.toString();
    }

    /**
     * Verifica se una password corrisponde a un hash
     * @param password la password da verificare
     * @param hash l'hash memorizzato
     * @return true se la password corrisponde, false altrimenti
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(hash);
    }
}
