package cinemax;

public class Film {
    private DataFilm data;
    private Ora ora;
    private String titolo, genere, regista;
    private int anno, durata, etaMinima;
    private double prezzo;

    public Film(DataFilm data, Ora ora, String titolo, String genere, String regista, int anno, int durata, int etaMinima, double prezzo) {
        this.data = data;
        this.ora = ora;
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
        this.prezzo = prezzo;
    }

    public String getTitolo() { 
        return titolo; 
    }
    public String getDataOra() { 
        return data + " - " + ora; 
    }
}
