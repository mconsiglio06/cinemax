// Come dovrebbe diventare Proiezione.java
package cinemax;

public class Proiezione {
    private Film film;
    private DataFilm data;
    private Ora ora;
    private double prezzo;
    private int postiDisponibili = 200; // Come da direttive

    public Proiezione(Film film, DataFilm data, Ora ora, double prezzo) {
        this.film = film;
        this.data = data;
        this.ora = ora;
        this.prezzo = prezzo;
    }
    // Aggiungi getter e un metodo per scalare i posti quando si prenota
}
