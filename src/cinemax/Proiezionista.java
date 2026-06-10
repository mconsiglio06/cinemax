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

/**
 * Rappresenta il ruolo amministrativo responsabile della gestione del palinsesto.
 */
public class Proiezionista extends Utente {
    /**
     * Crea un utente proiezionista.
     *
     * @param nome nome del proiezionista.
     * @param cognome cognome del proiezionista.
     * @param username username di accesso.
     * @param password password di accesso.
     * @param dataNascita data di nascita.
     * @param luogo luogo associato all'utente.
     */
    public Proiezionista(String nome, String cognome, String username, String password, Date dataNascita, String luogo) {
        super(nome, cognome, username, password, dataNascita, luogo, Ruolo.PROIEZIONISTA, true, true);
    }

    /**
     * Aggiunge una proiezione al palinsesto.
     *
     * @param proiezione proiezione da aggiungere.
     * @return true se l'aggiunta e completata.
     */
    public boolean aggiungiProiezione(Proiezione proiezione) {
        if (!isLogged()) {
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

    /**
     * Rimuove una proiezione, se non ha prenotazioni associate.
     *
     * @param proiezione proiezione da rimuovere.
     * @return true se la rimozione e completata.
     */
    public boolean rimuoviProiezione(Proiezione proiezione) {
        if (!isLogged()) {
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

    /**
     * Modifica una proiezione esistente.
     *
     * @param originale proiezione attuale.
     * @param aggiornata nuova versione della proiezione.
     * @return true se la modifica e completata.
     */
    public boolean modificaProiezione(Proiezione originale, Proiezione aggiornata) {
        if (!isLogged()) {
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

    /**
     * Stampa statistiche di occupazione e incasso per una proiezione.
     *
     * @param proiezione proiezione da analizzare.
     */
    public void visualizzaStatistiche(Proiezione proiezione) {
        if (!isLogged()) {
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
        System.out.println("Incasso totale: EUR " + String.format("%.2f", incassoTotale));
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
