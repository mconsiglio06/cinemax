package cinemax;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Rappresenta una prenotazione associata a una proiezione e a un insieme di posti.
 * Contiene anche l'importo totale pagato.
 */
public class Prenotazione {
    private static int idCounter = 0;
    private int id;
    private Proiezione proiezione;
    private LinkedList<Posto> seats;
    private double spesa;

    /**
     * Allinea il contatore interno degli ID almeno al valore indicato.
     *
     * @param id ultimo ID noto.
     */
    public static void ensureIdCounterAtLeast(int id) {
        if (id > idCounter) {
            idCounter = id;
        }
    }

    /**
     * Crea una prenotazione assegnando automaticamente un ID progressivo.
     *
     * @param proiezione proiezione prenotata.
     * @param seats posti prenotati.
     * @param spesa importo totale.
     */
    public Prenotazione(Proiezione proiezione, LinkedList<Posto> seats, double spesa) {
        this.id = ++idCounter;
        this.proiezione = proiezione;
        this.seats = seats;
        this.spesa = spesa;
    }

    /**
     * Crea una prenotazione con ID esplicito, utile durante il caricamento o il riuso ID.
     *
     * @param id identificativo della prenotazione.
     * @param proiezione proiezione prenotata.
     * @param seats posti prenotati.
     * @param spesa importo totale.
     */
    public Prenotazione(int id, Proiezione proiezione, LinkedList<Posto> seats, double spesa) {
        this.id = id;
        this.proiezione = proiezione;
        this.seats = seats;
        this.spesa = spesa;
        if (id > idCounter) {
            idCounter = id;
        }
    }

    /**
     * Restituisce l'ID della prenotazione.
     *
     * @return ID della prenotazione.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce la proiezione associata.
     *
     * @return proiezione associata.
     */
    public Proiezione getProiezione() {
        return proiezione;
    }

    /**
     * Restituisce la lista dei posti prenotati.
     *
     * @return lista dei posti prenotati.
     */
    public LinkedList<Posto> getSeats() {
        return seats;
    }

    /**
     * Restituisce il numero di posti prenotati.
     *
     * @return numero di posti prenotati.
     */
    public int getNumPosti() {
        return seats == null ? 0 : seats.size();
    }

    /**
     * Restituisce l'importo totale della prenotazione.
     *
     * @return importo totale della prenotazione.
     */
    public double getSpesa() {
        return spesa;
    }

    /**
     * Imposta la proiezione associata.
     *
     * @param proiezione nuova proiezione associata.
     */
    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    /**
     * Imposta la lista dei posti prenotati.
     *
     * @param seats nuova lista dei posti.
     */
    public void setSeats(LinkedList<Posto> seats) {
        this.seats = seats;
    }

    /**
     * Imposta l'importo totale.
     *
     * @param spesa nuovo importo totale.
     */
    public void setSpesa(double spesa) {
        this.spesa = spesa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prenotazione)) return false;
        Prenotazione that = (Prenotazione) o;
        return Objects.equals(proiezione.getFilm().getTitolo().toLowerCase(), that.proiezione.getFilm().getTitolo().toLowerCase()) &&
               Objects.equals(proiezione.getDataProiezione(), that.proiezione.getDataProiezione()) &&
               Objects.equals(proiezione.getOraProiezione(), that.proiezione.getOraProiezione());
    }

    @Override
    public int hashCode() {
        return Objects.hash(proiezione.getFilm().getTitolo().toLowerCase(), proiezione.getDataProiezione(), proiezione.getOraProiezione());
    }

    @Override
    public String toString() {
        StringBuilder seatsText = new StringBuilder();
        if (seats != null) {
            for (Posto seat : seats) {
                seatsText.append(seat).append(" ");
            }
        }
        return "Prenotazione ID: " + id + "\n" +
               "Film: " + proiezione.getFilm().getTitolo() + "\n" +
               "Data: " + proiezione.getDataProiezione() + " " + proiezione.getOraProiezione() + "\n" +
               "Posti: " + getNumPosti() + "\n" +
               "Lista posti: " + seatsText.toString().trim() + "\n" +
               "Spesa totale: " + spesa + "€";
    }
}
