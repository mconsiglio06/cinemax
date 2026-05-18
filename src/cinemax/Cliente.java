package cinemax;

import java.util.LinkedList;

public class Cliente extends Utente {
    private LinkedList<Prenotazione> bookings;

    public Cliente(String nome, String cognome, String username, String password, Date dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.CLIENTE, false, true);
        bookings = new LinkedList<>();
    }

    public boolean prenota(Proiezione proiezione, LinkedList<Posto> seats) {
        if (!isSessionValid()) {
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
        Prenotazione prenotazione = new Prenotazione(proiezione, seats, spesa);
        bookings.add(prenotazione);
        if (manager != null) {
            manager.savePrenotazione(this, prenotazione);
        }
        System.out.println("Prenotazione creata con successo: " + prenotazione.getId());
        return true;
    }

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

    public boolean areSeatsOccupied(Proiezione proiezione, LinkedList<Posto> seats) {
        return hasOccupiedSeats(proiezione, seats);
    }

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

    public LinkedList<Prenotazione> storico() {
        if (manager == null) {
            return new LinkedList<>(bookings);
        }
        bookings = manager.getPrenotazioniByUser(username);
        return new LinkedList<>(bookings);
    }

    public Prenotazione vediPrenotazione(int id) {
        for (Prenotazione prenotazione : storico()) {
            if (prenotazione.getId() == id) {
                return prenotazione;
            }
        }
        return null;
    }

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
