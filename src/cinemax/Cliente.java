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
 * Rappresenta un utente cliente.
 * Gestisce prenotazioni, modifiche, cancellazioni e ricerche delle proiezioni
 * disponibili per l'acquisto.
 */
public class Cliente extends Utente {
    private LinkedList<Prenotazione> bookings;

    /**
     * Crea un cliente con i dati anagrafici e di accesso.
     *
     * @param nome nome del cliente.
     * @param cognome cognome del cliente.
     * @param username username di accesso.
     * @param password password di accesso.
     * @param dataNascita data di nascita.
     * @param luogo luogo associato all'utente.
     */
    public Cliente(String nome, String cognome, String username, String password, Date dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.CLIENTE, false, true);
        bookings = new LinkedList<>();
    }

    /**
     * Crea una nuova prenotazione per una proiezione futura.
     *
     * @param proiezione proiezione da prenotare.
     * @param seats posti richiesti.
     * @return true se la prenotazione viene completata e salvata.
     */
    public boolean prenota(Proiezione proiezione, LinkedList<Posto> seats) {
        if (!isLogged()) {
            System.out.println("Devi essere loggato per prenotare.");
            return false;
        }
        if (proiezione == null || seats == null || seats.isEmpty()) {
            System.out.println("Seleziona almeno un posto valido per prenotare.");
            return false;
        }
        if (!isFutureProiezione(proiezione)) {
            System.out.println("Non è possibile prenotare una proiezione passata.");
            return false;
        }
        if (proiezione.getAvailableSeatsCount() < seats.size()) {
            System.out.println("Posti insufficienti disponibili per questa proiezione.");
            return false;
        }
        if (hasInvalidSeats(proiezione, seats)) {
            System.out.println("Alcuni dei posti selezionati non esistono.");
            return false;
        }
        if (hasOccupiedSeats(proiezione, seats)) {
            System.out.println("Alcuni posti selezionati sono occupati.");
            return false;
        }
        if (!proiezione.reserveSeats(seats)) {
            System.out.println("Alcuni posti selezionati non sono disponibili.");
            return false;
        }

        double spesa = proiezione.getPrezzo() * seats.size();
        Prenotazione prenotazione = manager == null
                ? new Prenotazione(proiezione, seats, spesa)
                : new Prenotazione(manager.getNextPrenotazioneId(), proiezione, seats, spesa);
        bookings.add(prenotazione);
        if (manager != null) {
            manager.savePrenotazione(this, prenotazione);
        }
        System.out.println("Prenotazione creata con successo: " + prenotazione.getId());
        return true;
    }

    /**
     * Modifica i posti associati a una prenotazione esistente.
     *
     * @param prenotazione prenotazione da modificare.
     * @param nuoviPosti nuovi posti richiesti.
     * @return true se la modifica viene salvata.
     */
    public boolean edit(Prenotazione prenotazione, LinkedList<Posto> nuoviPosti) {
        if (prenotazione == null || !bookings.contains(prenotazione)) {
            System.out.println("Prenotazione non trovata.");
            return false;
        }
        if (!isFutureProiezione(prenotazione.getProiezione())) {
            System.out.println("Impossibile modificare una prenotazione per una proiezione passata.");
            return false;
        }
        if (nuoviPosti == null || nuoviPosti.isEmpty()) {
            System.out.println("Devi selezionare almeno un nuovo posto.");
            return false;
        }

        Proiezione proiezione = prenotazione.getProiezione();
        LinkedList<Posto> postiPrecedenti = prenotazione.getSeats();
        if (hasInvalidSeats(proiezione, nuoviPosti)) {
            System.out.println("Alcuni dei posti selezionati non esistono.");
            return false;
        }
        if (hasOccupiedSeats(proiezione, nuoviPosti)) {
            System.out.println("Alcuni posti selezionati sono occupati.");
            return false;
        }
        proiezione.releaseSeats(postiPrecedenti);
        boolean canReserve = proiezione.reserveSeats(nuoviPosti);
        if (!canReserve) {
            proiezione.reserveSeats(postiPrecedenti);
            System.out.println("I nuovi posti non sono disponibili. Modifica annullata.");
            return false;
        }

        prenotazione.setSeats(nuoviPosti);
        prenotazione.setSpesa(proiezione.getPrezzo() * nuoviPosti.size());
        if (manager != null) {
            manager.updatePrenotazione(this, prenotazione);
        }
        System.out.println("Prenotazione modificata con successo: " + prenotazione.getId());
        return true;
    }

    /**
     * Verifica se almeno uno dei posti indicati risulta gia occupato.
     *
     * @param proiezione proiezione da controllare.
     * @param seats posti da verificare.
     * @return true se almeno un posto e occupato.
     */
    public boolean areSeatsOccupied(Proiezione proiezione, LinkedList<Posto> seats) {
        return hasOccupiedSeats(proiezione, seats);
    }

    /**
     * Verifica se almeno uno dei posti indicati non appartiene alla sala.
     *
     * @param proiezione proiezione da controllare.
     * @param seats posti da verificare.
     * @return true se almeno un posto non esiste.
     */
    public boolean areSeatsInvalid(Proiezione proiezione, LinkedList<Posto> seats) {
        return hasInvalidSeats(proiezione, seats);
    }

    private boolean hasInvalidSeats(Proiezione proiezione, LinkedList<Posto> seats) {
        if (proiezione == null || seats == null) {
            return true;
        }
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
        if (manager == null || proiezione == null || seats == null) {
            return false;
        }
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
     * Sposta una prenotazione su un'altra proiezione dello stesso film.
     *
     * @param prenotazione prenotazione da spostare.
     * @param nuovaProiezione nuova proiezione scelta.
     * @return true se lo spostamento viene salvato.
     */
    public boolean changeProiezione(Prenotazione prenotazione, Proiezione nuovaProiezione) {
        if (prenotazione == null || !bookings.contains(prenotazione)) {
            System.out.println("Prenotazione non trovata.");
            return false;
        }
        if (nuovaProiezione == null) {
            System.out.println("Nuova proiezione non valida.");
            return false;
        }
        if (!isFutureProiezione(nuovaProiezione)) {
            System.out.println("La nuova proiezione deve essere futura.");
            return false;
        }
        if (!nuovaProiezione.getFilm().getTitolo().equalsIgnoreCase(prenotazione.getProiezione().getFilm().getTitolo())) {
            System.out.println("La nuova proiezione deve essere dello stesso film.");
            return false;
        }
        if (nuovaProiezione.equals(prenotazione.getProiezione())) {
            System.out.println("La nuova proiezione è la stessa di quella attuale.");
            return false;
        }

        Proiezione vecchiaProiezione = prenotazione.getProiezione();
        LinkedList<Posto> posti = prenotazione.getSeats();

        // Rilascia posti dalla vecchia proiezione
        vecchiaProiezione.releaseSeats(posti);

        // Prova a riservare nella nuova
        boolean canReserve = nuovaProiezione.reserveSeats(posti);
        if (!canReserve) {
            // Se fallisce, rimetti nella vecchia
            vecchiaProiezione.reserveSeats(posti);
            System.out.println("I posti non sono disponibili nella nuova proiezione. Modifica annullata.");
            return false;
        }

        // Aggiorna prenotazione
        prenotazione.setProiezione(nuovaProiezione);
        prenotazione.setSpesa(nuovaProiezione.getPrezzo() * posti.size());
        if (manager != null) {
            manager.updatePrenotazione(this, prenotazione);
        }
        System.out.println("Proiezione modificata con successo: " + prenotazione.getId());
        return true;
    }

    /**
     * Cancella una prenotazione liberando i posti e aggiornando il file CSV.
     *
     * @param prenotazione prenotazione da annullare.
     * @return true se la cancellazione viene completata.
     */
    public boolean cancel(Prenotazione prenotazione) {
        if (prenotazione == null || !bookings.contains(prenotazione)) {
            System.out.println("Prenotazione non trovata.");
            return false;
        }
        if (!canCancel(prenotazione)) {
            System.out.println("Non è possibile cancellare questa prenotazione.");
            return false;
        }

        prenotazione.getProiezione().releaseSeats(prenotazione.getSeats());
        bookings.remove(prenotazione);
        if (manager != null) {
            manager.removePrenotazione(this, prenotazione);
        }
        System.out.println("Prenotazione cancellata: " + prenotazione.getId());
        return true;
    }

    /**
     * Restituisce lo storico prenotazioni del cliente.
     *
     * @return lista delle prenotazioni associate allo username corrente.
     */
    public LinkedList<Prenotazione> storico() {
        if (manager == null) {
            return new LinkedList<>(bookings);
        }
        bookings = manager.getPrenotazioniByUser(username);
        return new LinkedList<>(bookings);
    }

    /**
     * Cerca una prenotazione nello storico del cliente per ID.
     *
     * @param id identificativo della prenotazione.
     * @return prenotazione trovata, oppure null.
     */
    public Prenotazione vediPrenotazione(int id) {
        for (Prenotazione prenotazione : storico()) {
            if (prenotazione.getId() == id) {
                return prenotazione;
            }
        }
        return null;
    }

    /**
     * Cerca proiezioni future il cui titolo contiene la stringa indicata.
     *
     * @param titolo testo da cercare nel titolo.
     * @return lista di proiezioni compatibili.
     */
    public LinkedList<Proiezione> searchProiezioniByTitle(String titolo) {
        LinkedList<Proiezione> risultati = new LinkedList<>();
        if (manager == null) {
            return risultati;
        }
        for (Proiezione proiezione : manager.getProiezioni()) {
            if (proiezione.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase()) && isFutureProiezione(proiezione)) {
                risultati.add(proiezione);
            }
        }
        return risultati;
    }

    /**
     * Cerca proiezioni future in una fascia di prezzo.
     *
     * @param minPrezzo prezzo minimo.
     * @param maxPrezzo prezzo massimo.
     * @return lista di proiezioni compatibili.
     */
    public LinkedList<Proiezione> searchProiezioniByRange(double minPrezzo, double maxPrezzo) {
        LinkedList<Proiezione> risultati = new LinkedList<>();
        if (manager == null) {
            return risultati;
        }
        for (Proiezione proiezione : manager.getProiezioni()) {
            double prezzo = proiezione.getPrezzo();
            if (prezzo >= minPrezzo && prezzo <= maxPrezzo && isFutureProiezione(proiezione)) {
                risultati.add(proiezione);
            }
        }
        return risultati;
    }

    /**
     * Cerca proiezioni future per genere.
     *
     * @param genere genere richiesto.
     * @return lista di proiezioni compatibili.
     */
    public LinkedList<Proiezione> searchProiezioniByGenre(String genere) {
        LinkedList<Proiezione> risultati = new LinkedList<>();
        if (manager == null) {
            return risultati;
        }
        for (Proiezione proiezione : manager.getProiezioni()) {
            if (proiezione.getFilm().getGenere().equalsIgnoreCase(genere) && isFutureProiezione(proiezione)) {
                risultati.add(proiezione);
            }
        }
        return risultati;
    }

    /**
     * Stampa la mappa della sala per una proiezione.
     *
     * @param proiezione proiezione da visualizzare.
     */
    public void stampaMappa(Proiezione proiezione) {
        if (proiezione == null) {
            System.out.println("Proiezione non valida.");
            return;
        }
        System.out.println(proiezione.renderSala());
    }

    private boolean isFutureProiezione(Proiezione proiezione) {
        try {
            Date oggi = Date.today();
            if (proiezione.getDataProiezione().isAfter(oggi)) {
                return true;
            }
            if (proiezione.getDataProiezione().equals(oggi)) {
                Time now = Time.now();
                return now.isBefore(proiezione.getOraProiezione());
            }
        } catch (DateFormatException | TimeFormatException e) {
            System.out.println("Errore durante il controllo della data della proiezione: " + e.getMessage());
        }
        return false;
    }

    private boolean canCancel(Prenotazione prenotazione) {
        if (isFutureProiezione(prenotazione.getProiezione())) {
            return true;
        }
        for (Prenotazione precedente : bookings) {
            if (!precedente.equals(prenotazione) &&
                    precedente.getProiezione().getFilm().getTitolo().equalsIgnoreCase(prenotazione.getProiezione().getFilm().getTitolo()) &&
                    precedente.getProiezione().getDataProiezione().isBefore(prenotazione.getProiezione().getDataProiezione())) {
                return true;
            }
        }
        return false;
    }
}
