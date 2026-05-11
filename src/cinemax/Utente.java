package cinemax;

public class Utente {
    String nome;
    String cognome;
    String username;
    String password;
    DataFilm dataNascita;
    String luogo;
    Ruolo ruolo;
    boolean logged;

    public Utente(String nome, String cognome, String username, String password, DataFilm dataNascita, String luogo, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataNascita = dataNascita;
        this.luogo = luogo;
        this.ruolo = ruolo;
        this.logged = false;
    }


}
