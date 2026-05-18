package cinemax;

import java.util.LinkedList;

public class Proiezione {
    protected Film film;
    protected Date dataProiezione;
    protected Time oraProiezione;
    protected double prezzo;
    protected boolean[][] sala = new boolean[10][20];

    public Proiezione(Film film, Date dataProiezione, Time oraProiezione, double prezzo) {
        this.film = film;
        this.dataProiezione = dataProiezione;
        this.oraProiezione = oraProiezione;
        this.prezzo = prezzo;
    }

    public Film getFilm() {
        return film;
    }

    public Date getDataProiezione() {
        return dataProiezione;
    }

    public Time getOraProiezione() {
        return oraProiezione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public boolean[][] getSala() {
        return sala;
    }

    public int getAvailableSeatsCount() {
        int count = 0;
        for (boolean[] rowSeats : sala) {
            for (boolean seat : rowSeats) {
                if (!seat) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isSeatAvailable(int row, int col) {
        if (!isValidSeat(row, col)) {
            return false;
        }
        return !sala[row - 1][col - 1];
    }

    public boolean reserveSeat(int row, int col) {
        if (!isValidSeat(row, col) || sala[row - 1][col - 1]) {
            return false;
        }
        sala[row - 1][col - 1] = true;
        return true;
    }

    public boolean reserveSeats(LinkedList<Posto> posti) {
        for (Posto seat : posti) {
            int row = seat.getRowIndex();
            int col = seat.getNumero();
            if (!isSeatAvailable(row, col)) {
                return false;
            }
        }
        for (Posto seat : posti) {
            int row = seat.getRowIndex();
            int col = seat.getNumero();
            sala[row - 1][col - 1] = true;
        }
        return true;
    }

    public void releaseSeats(LinkedList<Posto> posti) {
        for (Posto seat : posti) {
            int row = seat.getRowIndex();
            int col = seat.getNumero();
            if (isValidSeat(row, col)) {
                sala[row - 1][col - 1] = false;
            }
        }
    }

    private boolean isValidSeat(int row, int col) {
        return row >= 1 && row <= sala.length && col >= 1 && col <= sala[0].length;
    }

    public String renderSala() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < sala.length; row++) {
            char rowLabel = (char) ('A' + row);
            sb.append("Fila ").append(rowLabel).append(": ");
            for (int col = 0; col < sala[row].length; col++) {
                sb.append(sala[row][col] ? "[X]" : "[ ]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return film.toString() + "\nProiezione: " + dataProiezione + " " + oraProiezione + "\nPrezzo: EUR " + prezzo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proiezione)) return false;
        Proiezione that = (Proiezione) o;
        return film.getTitolo().equalsIgnoreCase(that.film.getTitolo()) &&
               dataProiezione.equals(that.dataProiezione) &&
               oraProiezione.equals(that.oraProiezione);
    }

    @Override
    public int hashCode() {
        int result = film.getTitolo().toLowerCase().hashCode();
        result = 31 * result + dataProiezione.hashCode();
        result = 31 * result + oraProiezione.hashCode();
        return result;
    }
}
