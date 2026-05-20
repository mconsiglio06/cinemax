package cinemax;

import java.util.LinkedList;

/**
 * Rappresenta una proiezione cinematografica in una data e ora specifiche.
 * Mantiene il riferimento al film, il prezzo e una matrice dei posti della sala.
 */
public class Proiezione {
    /** Film proiettato. */
    protected Film film;
    /** Data della proiezione. */
    protected Date dataProiezione;
    /** Ora di inizio della proiezione. */
    protected Time oraProiezione;
    /** Prezzo unitario del biglietto. */
    protected double prezzo;
    /** Matrice dei posti della sala. */
    protected boolean[][] sala = new boolean[10][20];

    /**
     * Crea una nuova proiezione.
     *
     * @param film film proiettato.
     * @param dataProiezione data della proiezione.
     * @param oraProiezione ora di inizio.
     * @param prezzo prezzo unitario del biglietto.
     */
    public Proiezione(Film film, Date dataProiezione, Time oraProiezione, double prezzo) {
        this.film = film;
        this.dataProiezione = dataProiezione;
        this.oraProiezione = oraProiezione;
        this.prezzo = prezzo;
    }

    /**
     * Restituisce il film proiettato.
     *
     * @return film proiettato.
     */
    public Film getFilm() {
        return film;
    }

    /**
     * Restituisce la data della proiezione.
     *
     * @return data della proiezione.
     */
    public Date getDataProiezione() {
        return dataProiezione;
    }

    /**
     * Restituisce l'ora della proiezione.
     *
     * @return ora della proiezione.
     */
    public Time getOraProiezione() {
        return oraProiezione;
    }

    /**
     * Restituisce il prezzo unitario.
     *
     * @return prezzo unitario.
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Restituisce la matrice dei posti della sala.
     *
     * @return matrice dei posti della sala.
     */
    public boolean[][] getSala() {
        return sala;
    }

    /**
     * Conta i posti liberi nella matrice della sala.
     *
     * @return numero di posti disponibili.
     */
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

    /**
     * Verifica se un posto e valido e libero.
     *
     * @param row indice di riga a partire da 1.
     * @param col indice di colonna a partire da 1.
     * @return true se il posto puo essere prenotato.
     */
    public boolean isSeatAvailable(int row, int col) {
        if (!isValidSeat(row, col)) {
            return false;
        }
        return !sala[row - 1][col - 1];
    }

    /**
     * Prenota un singolo posto se disponibile.
     *
     * @param row indice di riga a partire da 1.
     * @param col indice di colonna a partire da 1.
     * @return true se la prenotazione del posto e riuscita.
     */
    public boolean reserveSeat(int row, int col) {
        if (!isValidSeat(row, col) || sala[row - 1][col - 1]) {
            return false;
        }
        sala[row - 1][col - 1] = true;
        return true;
    }

    /**
     * Prenota un insieme di posti in modo atomico rispetto alla matrice in memoria.
     *
     * @param posti posti da occupare.
     * @return true se tutti i posti sono stati prenotati.
     */
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

    /**
     * Libera i posti indicati nella matrice della sala.
     *
     * @param posti posti da rendere disponibili.
     */
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

    /**
     * Genera una rappresentazione testuale semplice della sala.
     *
     * @return mappa della sala in formato testo.
     */
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
