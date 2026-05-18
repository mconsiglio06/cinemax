package cinemax;

import java.util.LinkedList;
import java.util.Objects;

public class Prenotazione {
    private static int idCounter = 0;
    private int id;
    private Proiezione proiezione;
    private LinkedList<Posto> seats;
    private double spesa;

    public static void ensureIdCounterAtLeast(int id) {
        if (id > idCounter) {
            idCounter = id;
        }
    }

    public Prenotazione(Proiezione proiezione, LinkedList<Posto> seats, double spesa) {
        this.id = ++idCounter;
        this.proiezione = proiezione;
        this.seats = seats;
        this.spesa = spesa;
    }

    public Prenotazione(int id, Proiezione proiezione, LinkedList<Posto> seats, double spesa) {
        this.id = id;
        this.proiezione = proiezione;
        this.seats = seats;
        this.spesa = spesa;
        if (id > idCounter) {
            idCounter = id;
        }
    }

    public int getId() {
        return id;
    }

    public Proiezione getProiezione() {
        return proiezione;
    }

    public LinkedList<Posto> getSeats() {
        return seats;
    }

    public int getNumPosti() {
        return seats == null ? 0 : seats.size();
    }

    public double getSpesa() {
        return spesa;
    }

    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    public void setSeats(LinkedList<Posto> seats) {
        this.seats = seats;
    }

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
