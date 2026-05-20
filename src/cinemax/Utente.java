package cinemax;

import java.util.LinkedList;

/**
 * Classe astratta base per tutti gli utenti dell'applicazione.
 * Contiene dati anagrafici, credenziali, ruolo e stato della sessione.
 */
public abstract class Utente {
    /** Nome dell'utente. */
    protected String nome;
    /** Cognome dell'utente. */
    protected String cognome;
    /** Username usato per l'accesso. */
    protected String username;
    /** Password dell'utente nella sessione corrente. */
    protected String password;
    /** Data di nascita dell'utente. */
    protected Date dataNascita;
    /** Luogo associato all'utente. */
    protected String luogo;
    /** Ruolo applicativo dell'utente. */
    protected Ruolo ruolo;
    /** Indica se l'utente ha privilegi amministrativi. */
    protected boolean isAdmin;
    /** Indica se la sessione e attiva. */
    protected boolean isLogged;
    
    // Riferimento statico al Manager per salvare gli utenti
    /** Manager condiviso per persistenza e operazioni di dominio. */
    protected static Manager manager;

    /**
     * Crea un utente generico.
     *
     * @param nome nome dell'utente.
     * @param cognome cognome dell'utente.
     * @param username username di accesso.
     * @param password password in chiaro usata per la sessione corrente.
     * @param dataNascita data di nascita.
     * @param luogo luogo di nascita o residenza.
     * @param ruolo ruolo applicativo.
     * @param isAdmin true se l'utente ha privilegi amministrativi.
     * @param isLogged true se la sessione parte gia attiva.
     */
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
    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome nuovo nome dell'utente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome nuovo cognome dell'utente.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce lo username dell'utente.
     *
     * @return username dell'utente.
     */
    public String getUsername() {
        return username; 
    }

    /**
     * Imposta lo username dell'utente.
     *
     * @param username nuovo username.
     */
    public void setUsername(String username) {
        this.username = username; 
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return password dell'utente.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password nuova password.
     */
    public void setPassword(String password) {
        this.password = password; 
    }

    /**
     * Restituisce la data di nascita.
     *
     * @return data di nascita.
     */
    public Date getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta la data di nascita.
     *
     * @param dataNascita nuova data di nascita.
     */
    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce il luogo associato all'utente.
     *
     * @return luogo associato all'utente.
     */
    public String getLuogo() {
        return luogo;
    }

    /**
     * Imposta il luogo associato all'utente.
     *
     * @param luogo nuovo luogo associato all'utente.
     */
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @return ruolo dell'utente.
     */
    public Ruolo getRuolo() {
        return ruolo; 
    }

    /**
     * Imposta il ruolo dell'utente.
     *
     * @param ruolo nuovo ruolo dell'utente.
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
    
    /**
     * Indica se l'utente e amministratore.
     *
     * @return true se l'utente e amministratore.
     */
    public boolean isAdmin() {
        return isAdmin; 
    }

    /**
     * Imposta lo stato amministrativo.
     *
     * @param admin nuovo stato amministrativo.
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Indica se l'utente risulta loggato.
     *
     * @return true se l'utente risulta loggato.
     */
    public boolean isLogged() {
        // Se l'utente è loggato, verifica che la sessione sia ancora valida
        if (this.isLogged) {
            return isSessionValid();
        }
        return false;
    }

    /**
     * Imposta lo stato di login.
     *
     * @param logged nuovo stato di login.
     */
    public void setLogged(boolean logged) {
        this.isLogged = logged;
    }


    @Override
    public String toString() {
        return nome + " " + cognome + " (" + ruolo + ")";
    }
}
