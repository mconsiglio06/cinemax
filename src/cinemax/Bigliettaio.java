package cinemax;

import java.util.LinkedList;

/**
 * Classe che rappresenta un Bigliettaio, un tipo specifico di Utente con il ruolo di bigliettaio.
 * Il bigliettaio ha accesso a funzionalità specifiche legate alla gestione dei biglietti e delle prenotazioni.
 */

public class Bigliettaio extends Utente {
    public Bigliettaio(String nome, String cognome, String username, String password, Date dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.BIGLIETTAIO, true, true);
    }

    public void vediProiezioniGiornaliere() {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per visualizzare le proiezioni giornaliere.");
            return;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }
        try {
            Date oggi = Date.today();
            LinkedList<Proiezione> proiezioni = manager.getProiezioniByDate(oggi);
            if (proiezioni.isEmpty()) {
                System.out.println("Nessuna proiezione programmata per oggi.");
                return;
            }
            System.out.println("=== Proiezioni di oggi ===");
            for (Proiezione proiezione : proiezioni) {
                System.out.println(proiezione);
                System.out.println("-------------------------");
            }
        } catch (DateFormatException e) {
            System.out.println("Errore nel recupero della data odierna: " + e.getMessage());
        }
    }

    public void searchPrenotazioni() {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per cercare prenotazioni.");
            return;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }
        try {
            LinkedList<Prenotazione> prenotazioni = manager.getPrenotazioniByDate(Date.today());
            if (prenotazioni.isEmpty()) {
                System.out.println("Nessuna prenotazione trovata per oggi.");
                return;
            }
            System.out.println("=== Prenotazioni di oggi ===");
            for (Prenotazione p : prenotazioni) {
                System.out.println(p);
                System.out.println("-------------------------");
            }
        } catch (DateFormatException e) {
            System.out.println("Errore nel recupero della data odierna: " + e.getMessage());
        }
    }

    public void searchPrenotazioni(String cliente) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per cercare prenotazioni.");
            return;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }
        LinkedList<Prenotazione> prenotazioni = manager.searchPrenotazioniByCliente(cliente);
        if (prenotazioni.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata per: " + cliente);
            return;
        }
        System.out.println("=== Prenotazioni per cliente: " + cliente + " ===");
        for (Prenotazione p : prenotazioni) {
            System.out.println(p);
            System.out.println("-------------------------");
        }
    }

    public void searchPrenotazioni(int id) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per cercare prenotazioni.");
            return;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }
        Prenotazione prenotazione = manager.getPrenotazioneById(id);
        if (prenotazione == null) {
            System.out.println("Nessuna prenotazione trovata con ID: " + id);
            return;
        }
        System.out.println("=== Prenotazione ID: " + id + " ===");
        System.out.println(prenotazione);
    }

    public double report() {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per visualizzare il report giornaliero.");
            return 0;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return 0;
        }
        try {
            Date oggi = Date.today();
            return manager.getDailyRevenue(oggi);
        } catch (DateFormatException e) {
            System.out.println("Errore nel recupero della data odierna: " + e.getMessage());
            return 0;
        }
    }

    public void annulla() {
        System.out.println("Funzione annulla non implementata per il bigliettaio.");
    }

    public void mappa(Proiezione proiezione) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per visualizzare la mappa.");
            return;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }
        if (proiezione == null) {
            System.out.println("Proiezione non valida.");
            return;
        }
        boolean[][] occupancy = manager.getOccupancyMap(proiezione);
        System.out.println("=== Mappa posti per proiezione ===");
        int rowIndex = 0;
        for (boolean[] rowSeats : occupancy) {
            char rowLabel = (char) ('A' + rowIndex++);
            System.out.print("Fila " + rowLabel + ": ");
            for (boolean seat : rowSeats) {
                System.out.print(seat ? "[X]" : "[ ]");
            }
            System.out.println();
        }
    }

    public void cerca(String titolo) {
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }
        for (Proiezione proiezione : manager.getProiezioni()) {
            if (proiezione.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                System.out.println(proiezione);
            }
        }
    }
}
