/**
 * Progetto: CineMax - Laboratorio Interdisciplinare A
 * Anno Accademico: 2025/2026
 * Sede: Varese (VA)
 * * Autori del progetto:
 * - Matteo Consiglio (Matricola: 765479)
 * - Giulia Alessandra Casagrande (Matricola: 765252)
 * - Valeria Ferrigno (Matricola: 766909)
 * - Edoardo Della Torre (Matricola: 766913)
 */

package cinemax;

import java.util.LinkedList;

/**
 * Classe che rappresenta un Bigliettaio, un tipo specifico di Utente con il ruolo di bigliettaio.
 * Il bigliettaio ha accesso a funzionalità specifiche legate alla gestione dei biglietti e delle prenotazioni.
 */

public class Bigliettaio extends Utente {
    /**
     * Crea un utente bigliettaio.
     *
     * @param nome nome del bigliettaio.
     * @param cognome cognome del bigliettaio.
     * @param username username di accesso.
     * @param password password di accesso.
     * @param dataNascita data di nascita.
     * @param luogo luogo associato all'utente.
     */
    public Bigliettaio(String nome, String cognome, String username, String password, Date dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.BIGLIETTAIO, true, true);
    }

    /**
     * Stampa le proiezioni programmate nella data odierna.
     */
    public void vediProiezioniGiornaliere() {
        if (!isLogged()) {
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

    /**
     * Stampa le prenotazioni della data odierna.
     */
    public void searchPrenotazioni() {
        if (!isLogged()) {
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

    /**
     * Cerca prenotazioni per nome, cognome o username del cliente.
     *
     * @param cliente stringa di ricerca.
     */
    public void searchPrenotazioni(String cliente) {
        if (!isLogged()) {
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

    /**
     * Cerca e stampa una prenotazione per ID.
     *
     * @param id identificativo della prenotazione.
     */
    public void searchPrenotazioni(int id) {
        if (!isLogged()) {
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

    /**
     * Calcola l'incasso giornaliero.
     *
     * @return totale incassato nella data odierna.
     */
    public double report() {
        if (!isLogged()) {
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

    /**
     * Annulla una prenotazione esistente e aggiorna la persistenza.
     *
     * @param idPrenotazione ID della prenotazione da annullare.
     * @return true se l'annullamento e completato.
     */
    public boolean annulla(int idPrenotazione) {
        if (!isLogged()) {
            System.out.println("Devi essere loggato per annullare una prenotazione.");
            return false;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return false;
        }
        Prenotazione prenotazione = manager.getPrenotazioneById(idPrenotazione);
        if (prenotazione == null) {
            System.out.println("Nessuna prenotazione trovata con ID: " + idPrenotazione);
            return false;
        }
        if (prenotazione.getProiezione() != null) {
            prenotazione.getProiezione().releaseSeats(prenotazione.getSeats());
        }
        manager.removePrenotazione(null, prenotazione);
        System.out.println("Prenotazione annullata: " + idPrenotazione);
        return true;
    }

    /**
     * Registra una vendita al banco associandola all'utente fittizio CASSA.
     *
     * @param proiezione proiezione acquistata.
     * @param postiScelti posti venduti.
     * @return true se la vendita viene registrata.
     */
    public boolean venditaDiretta(Proiezione proiezione, LinkedList<Posto> postiScelti) {
        if (!isLogged()) {
            System.out.println("Devi essere loggato per effettuare una vendita diretta.");
            return false;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return false;
        }
        if (proiezione == null || postiScelti == null || postiScelti.isEmpty()) {
            System.out.println("Seleziona almeno un posto valido.");
            return false;
        }
        if (hasInvalidSeats(proiezione, postiScelti)) {
            System.out.println("Alcuni dei posti selezionati non esistono.");
            return false;
        }
        if (hasOccupiedSeats(proiezione, postiScelti)) {
            System.out.println("Alcuni posti selezionati sono occupati.");
            return false;
        }
        if (!proiezione.reserveSeats(postiScelti)) {
            System.out.println("Alcuni posti selezionati non sono disponibili.");
            return false;
        }
        double spesa = proiezione.getPrezzo() * postiScelti.size();
        Prenotazione prenotazione = new Prenotazione(manager.getNextPrenotazioneId(), proiezione, postiScelti, spesa);
        try {
            Cliente cassa = new Cliente("CASSA", "CASSA", "CASSA", "CASSA", new Date(1, 1, 1900), "CASSA");
            manager.savePrenotazione(cassa, prenotazione);
        } catch (DateFormatException e) {
            System.out.println("Errore nella creazione dell'utente fittizio CASSA: " + e.getMessage());
            proiezione.releaseSeats(postiScelti);
            return false;
        }
        System.out.println("Vendita diretta completata. ID prenotazione: " + prenotazione.getId() + " | Totale: EUR " + String.format("%.2f", spesa));
        return true;
    }

    private boolean hasInvalidSeats(Proiezione proiezione, LinkedList<Posto> seats) {
        boolean[][] sala = proiezione.getSala();
        for (Posto seat : seats) {
            int row = seat.getRowIndex();
            int col = seat.getNumero();
            if (row < 1 || row > sala.length || col < 1 || col > sala[0].length) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOccupiedSeats(Proiezione proiezione, LinkedList<Posto> seats) {
        boolean[][] occupancy = manager.getOccupancyMap(proiezione);
        for (Posto seat : seats) {
            int row = seat.getRowIndex();
            int col = seat.getNumero();
            if (occupancy[row - 1][col - 1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Stampa la mappa dei posti occupati per una proiezione.
     *
     * @param proiezione proiezione da visualizzare.
     */
    public void mappa(Proiezione proiezione) {
        if (!isLogged()) {
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

    /**
     * Cerca e stampa proiezioni in base al titolo.
     *
     * @param titolo titolo o porzione di titolo.
     */
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
