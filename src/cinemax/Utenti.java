package cinemax;

public class Utenti {
    String nome;
    String cognome;
    String username;
    String password;
    Role ruolo;
    Indirizzo indirizzo;
    DataNascita dataNascita;

    public Utenti(String nome, String cognome, String username, String password, Role ruolo, Indirizzo indirizzo, DataNascita dataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        this.indirizzo = indirizzo;

        if(dataNascita != null) this.dataNascita = dataNascita;
        
    }

    
    
}
