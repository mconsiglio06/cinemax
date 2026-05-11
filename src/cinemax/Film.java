package cinemax;

public class Film {
    private String titolo, genere, regista;
    private int anno, durata, etaMinima;

    public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinima) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    public String getTitolo() { 
        return titolo; 
    }

    @Override
    public String toString() {
        return "Film: " + titolo + " (" + anno + ")\n" + genere + " - Regista: " + regista + "\nDurata: " + durata + " min \nEtà minima: " + etaMinima + "+";
    }
}
