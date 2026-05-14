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

    public String getGenere() {
        return genere;
    }

    public String getRegista() {
        return regista;
    }

    public int getAnno() {
        return anno;
    }

    public int getDurata() {
        return durata;
    }

    public int getEtaMinima() {
        return etaMinima;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public void setEtaMinima(int etaMinima) {
        this.etaMinima = etaMinima;
    }

    @Override
    public String toString() {
        return titolo.toUpperCase() + " (" + anno + ")\n" + genere + " - Regista: " + regista + "\nDurata: " + durata + " min \nEtà minima: " + etaMinima + "+";
    }
}
