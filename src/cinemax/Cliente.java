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