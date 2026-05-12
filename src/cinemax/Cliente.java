package cinemax;

import java.util.LinkedList;

public class Cliente extends Utente {
    private LinkedList<Prenotazione> miePrenotazioni;

    public Cliente(String nome, String cognome, String username, String password, DataFilm dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.CLIENTE);
        this.miePrenotazioni = new LinkedList<>();
    }

    public void addPrenotazione(Prenotazione p) {
        this.miePrenotazioni.add(p);
    }

    public LinkedList<Prenotazione> vediPrenotazioni() {
        return miePrenotazioni;
    }
}