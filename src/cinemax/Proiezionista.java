package cinemax;

public class Proiezionista extends Utente {
    public Proiezionista(String nome, String cognome, String username, String password, DataFilm dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.PROIEZIONISTA);
    }
}