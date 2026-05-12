package cinemax;

public abstract class Utente {
    protected String nome;
    protected String cognome;
    protected String username;
    protected String password; // Nelle specifiche si parla di cifratura (vedremo dopo)
    protected DataFilm dataNascita;
    protected String luogo;
    protected Ruolo ruolo;

    public Utente(String nome, String cognome, String username, String password, 
                  DataFilm dataNascita, String luogo, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataNascita = dataNascita;
        this.luogo = luogo;
        this.ruolo = ruolo;
    }

    // Getter comuni
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Ruolo getRuolo() { return ruolo; }


    // Setter comuni
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return nome + " " + cognome + " (" + ruolo + ")";
    }
}