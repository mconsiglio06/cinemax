package cinemax;

public class Bigliettaio extends Utente {
    public Bigliettaio(String nome, String cognome, String username, String password, DataFilm dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.BIGLIETTAIO);
    }
    
}
