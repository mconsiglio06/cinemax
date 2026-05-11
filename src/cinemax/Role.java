package cinemax;

public enum Role {
    NONLOGGATO, CLIENTE, BIGLIETTAIO, PROIEZIONISTA;

    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }
}
