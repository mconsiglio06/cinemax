package cinemax;

public enum Ruolo {
    UTENTE(false, false),
    CLIENTE(false, true), 
    PROIEZIONISTA(true, true), 
    BIGLIETTAIO(true, true);

    private final boolean power;
    private boolean logged;

    // Costruttore dell'Enum
    Ruolo(boolean power, boolean logged) {
        this.power = power;
        this.logged = logged;
    }

    // Metodo per verificare se è un utente con privilegi
    public boolean haPermessi() {
        return power;
    }

    // Metodo per verificare se è un utente loggato
    public boolean isLogged() {
        return logged;
    }
}