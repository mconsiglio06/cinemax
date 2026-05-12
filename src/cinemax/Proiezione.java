package cinemax;

public class Proiezione {
    private Film film;
    private DataFilm data;
    private Ora ora;
    private double prezzo;
    // Matrice 20 file x 10 posti
    private boolean[][] sala = new boolean[20][10]; 
    private int postiDisponibili = 200;

    public Proiezione(Film film, DataFilm data, Ora ora, double prezzo) {
        this.film = film;
        this.data = data;
        this.ora = ora;
        this.prezzo = prezzo;
        // Inizialmente tutti i posti sono liberi (false)
    }

    // Metodo per visualizzare la sala a schermo
    public void mostraSala() {
        System.out.println("\n--- SCHERMO ---");
        for (int i = 0; i < sala.length; i++) {
            System.out.print("Fila " + (i + 1) + (i < 9 ? " : " : ": "));
            for (int j = 0; j < sala[i].length; j++) {
                // [ ] posto libero, [X] posto occupato
                System.out.print(sala[i][j] ? "[X] " : "[ ] ");
            }
            System.out.println();
        }
    }

    // Metodo per prenotare un posto specifico
    public void prenotaPosto(int fila, int colonna) throws PostoOccupatoException {
        if (fila < 0 || fila >= 20 || colonna < 0 || colonna >= 10) {
            throw new IndexOutOfBoundsException("Coordinate posto non valide.");
        }
        if (sala[fila][colonna]) {
            throw new PostoOccupatoException("Il posto selezionato è già occupato.");
        }
        sala[fila][colonna] = true;
        postiDisponibili--;
    }

    // Getter necessari
    public Film getFilm() { return film; }
    public DataFilm getData() { return data; }
    public int getPostiDisponibili() { return postiDisponibili; }
}