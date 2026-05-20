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
 * Elenco dei ruoli supportati dall'applicazione.
 * Ogni ruolo specifica se possiede privilegi amministrativi.
 */
public enum Ruolo {
    /** Utente generico non autenticato. */
    UTENTE(false, false),
    /** Cliente registrato. */
    CLIENTE(false, true),
    /** Addetto alla gestione del palinsesto. */
    PROIEZIONISTA(true, true),
    /** Addetto alla biglietteria. */
    BIGLIETTAIO(true, true);

    private final boolean power;
    private boolean logged;

    // Costruttore dell'Enum
    Ruolo(boolean power, boolean logged) {
        this.power = power;
        this.logged = logged;
    }

    // Metodo per verificare se è un utente con privilegi
    /**
     * Indica se il ruolo dispone di poteri amministrativi.
     *
     * @return true per ruoli del personale con privilegi.
     */
    public boolean hasPowers() {
        return power;
    }

    // Metodo per verificare se è un utente loggato
    /**
     * Restituisce lo stato di login associato al ruolo.
     *
     * @return true se il ruolo e marcato come loggato.
     */
    public boolean isLogged() {
        return logged;
    }
}
