package cinemax;

public class Proiezionista extends Utente {
    public Proiezionista(String nome, String cognome, String username, String password, Date dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.PROIEZIONISTA, true, true);
    }

    public boolean aggiungiProiezione(Proiezione proiezione) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per aggiungere una proiezione.");
            return false;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return false;
        }
        boolean risultato = manager.addProiezione(proiezione);
        if (risultato) {
            System.out.println("Proiezione aggiunta con successo.");
        }
        return risultato;
    }

    public boolean rimuoviProiezione(Proiezione proiezione) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per rimuovere una proiezione.");
            return false;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return false;
        }
        boolean risultato = manager.removeProiezione(proiezione);
        if (risultato) {
            System.out.println("Proiezione rimossa con successo.");
        }
        return risultato;
    }

    public boolean modificaProiezione(Proiezione originale, Proiezione aggiornata) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per modificare una proiezione.");
            return false;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return false;
        }
        boolean risultato = manager.updateProiezione(originale, aggiornata);
        if (risultato) {
            System.out.println("Proiezione modificata con successo.");
        }
        return risultato;
    }

    public void visualizzaStatistiche(Proiezione proiezione) {
        if (!isSessionValid()) {
            System.out.println("Devi essere loggato per visualizzare le statistiche.");
            return;
        }
        if (manager == null) {
            System.out.println("Manager non inizializzato.");
            return;
        }

        boolean[][] occupancy = manager.getOccupancyMap(proiezione);
        int postiOccupati = 0;
        int totalePosti = occupancy.length * occupancy[0].length;
        for (boolean[] rowSeats : occupancy) {
            for (boolean seat : rowSeats) {
                if (seat) {
                    postiOccupati++;
                }
            }
        }

        double incassoTotale = postiOccupati * proiezione.getPrezzo();
        double percentuale = totalePosti == 0 ? 0 : (100.0 * postiOccupati / totalePosti);

        System.out.println("=== Statistiche sala per proiezione ===");
        System.out.println("Film: " + proiezione.getFilm().getTitolo());
        System.out.println("Data: " + proiezione.getDataProiezione() + " " + proiezione.getOraProiezione());
        System.out.println("Incasso totale: €" + incassoTotale);
        System.out.println("Occupazione: " + postiOccupati + "/" + totalePosti + " (" + String.format("%.2f", percentuale) + "%)");
        System.out.println("Mappa posti:");
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
