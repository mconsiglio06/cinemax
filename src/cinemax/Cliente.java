package cinemax;

import java.util.LinkedList;

public class Cliente extends Utente {

    public Cliente(String nome, String cognome, String username, String password, DataFilm dataNascita, String luogo) {
        // Passiamo Ruolo.CLIENTE al costruttore del padre
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.CLIENTE);
    }

    public void prenota(Proiezione p, int posti) {
        // Logica per creare una prenotazione
    }

    public LinkedList<Prenotazione> vediPrenotazioni() {
        // Ritorna la lista delle prenotazioni di questo cliente
        return new LinkedList<>();
    }
    
    // toString rimosso o aggiornato per usare i campi del padre
}