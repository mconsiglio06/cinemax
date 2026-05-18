package cinemax;

import java.util.LinkedList;

public abstract class Utente {
    protected String nome;
    protected String cognome;
    protected String username;
    protected String password; // Nelle specifiche si parla di cifratura (vedremo dopo)
    protected Date dataNascita;
    protected String luogo;
    protected Ruolo ruolo;
    protected boolean isAdmin;
    protected boolean isLogged;
    
    // Riferimento statico al Manager per salvare gli utenti
    protected static Manager manager;

    public Utente(String nome, String cognome, String username, String password, 
                  Date dataNascita, String luogo, Ruolo ruolo, boolean isAdmin, boolean isLogged) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataNascita = dataNascita;
        this.luogo = luogo;
        this.ruolo = ruolo;
        this.isAdmin = isAdmin;
        this.isLogged = isLogged;
    }

    // METODI DI RICERCA FILM
    /**
     * Ricerca film per titolo
     * @param titolo il titolo del film da cercare
     * @return LinkedList di film che corrispondono al titolo
     */
    public LinkedList<Film> searchByTitle(String titolo) {
        // Implementazione della ricerca dei film per titolo
        // Interagirà con CinemaManager per ottenere i film
        return new LinkedList<>();
    }

    /**
     * Ricerca film per range di prezzo
     * @param minPrezzo prezzo minimo
     * @param maxPrezzo prezzo massimo
     * @return LinkedList di film nel range di prezzo specificato
     */
    public LinkedList<Film> searchByRange(double minPrezzo, double maxPrezzo) {
        // Implementazione della ricerca dei film per range di prezzo
        return new LinkedList<>();
    }

    /**
     * Ricerca film per genere
     * @param genere il genere del film da cercare
     * @return LinkedList di film del genere specificato
     */
    public LinkedList<Film> searchByGenre(String genere) {
        // Implementazione della ricerca dei film per genere
        return new LinkedList<>();
    }

    // METODI DI VISUALIZZAZIONE
    /**
     * Visualizza la mappa dei posti per una proiezione
     * Mostra i posti occupati e disponibili (solo se la proiezione è futura)
     * @param proiezione la proiezione di cui visualizzare la mappa
     */
    public void mappa(Proiezione proiezione) {
        // Verifica che la proiezione sia futura
        try {
            Date oggi = Date.today();
            
            if (proiezione.getDataProiezione().isBefore(oggi)) {
                System.out.println("Non è possibile visualizzare la mappa di una proiezione passata");
                return;
            }
            
            // Visualizza la mappa dei posti
            System.out.println("Mappa dei posti per la proiezione di: " + proiezione.getFilm().getTitolo());
            // TODO: Implementazione della visualizzazione della mappa
        } catch (DateFormatException e) {
            System.out.println("Errore nel recupero della data attuale");
        }
    }

    /**
     * Visualizza le informazioni di un film
     * @param film il film di cui visualizzare le informazioni
     */
    public void infoFilm(Film film) {
        System.out.println("=== Informazioni Film ===");
        System.out.println("Titolo: " + film.getTitolo());
        System.out.println("Genere: " + film.getGenere());
        System.out.println("Regista: " + film.getRegista());
        System.out.println("Anno: " + film.getAnno());
        System.out.println("Durata: " + film.getDurata() + " minuti");
        System.out.println("Età minima: " + film.getEtaMinima() + " anni");
    }

    // METODI DI AUTENTICAZIONE
    /**
     * Imposta il Manager per tutti gli utenti
     * @param mgr il Manager da utilizzare
     */
    public static void setManager(Manager mgr) {
        manager = mgr;
    }

    /**
     * Registra un nuovo utente e salva le credenziali nel file utenti.csv
     * La password viene cifrata prima di essere salvata
     */
    public void signup() {
        if (manager == null) {
            System.out.println("Errore: Manager non inizializzato");
            return;
        }
        
        // Verifica che l'username non sia già registrato
        if (manager.usernameExists(this.username)) {
            System.out.println("Errore: Username '" + this.username + "' è già registrato");
            return;
        }
        
        // Salva l'utente nel file (la password viene cifrata dentro saveUtente)
        manager.saveUtente(this);
        System.out.println("Registrazione completata per: " + this.username);
    }

    /**
     * Effettua il login dell'utente
     * Registra il timestamp del login usando la classe Time
     */
    public void login() {
        this.isLogged = true;
        System.out.println("Login effettuato per: " + this.username + " (" + this.ruolo + ")");
    }

    /**
     * Effettua il logout dell'utente
     */
    public void logout() {
        this.isLogged = false;
        System.out.println("Logout effettuato per: " + this.username);
    }

    /**
     * Verifica se la sessione è ancora valida
     * @return true se l'utente è loggato
     */
    public boolean isSessionValid() {
        return this.isLogged;
    }


    // GETTER E SETTER
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Ruolo getRuolo() {
        return ruolo; 
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
    
    public boolean isAdmin() { 
        return isAdmin; 
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isLogged() { 
        // Se l'utente è loggato, verifica che la sessione sia ancora valida
        if (this.isLogged) {
            return isSessionValid();
        }
        return false;
    }

    public void setLogged(boolean logged) {
        this.isLogged = logged;
    }


    @Override
    public String toString() {
        return nome + " " + cognome + " (" + ruolo + ")";
    }
}