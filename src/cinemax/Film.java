package cinemax;

/**
 * Modello dati di un film programmabile in una proiezione.
 * Contiene le informazioni anagrafiche del film e i vincoli di visione.
 */
public class Film {
    private String titolo, genere, regista;
    private int anno, durata, etaMinima;

    /**
     * Crea un film con i dati principali.
     *
     * @param titolo titolo del film.
     * @param genere genere cinematografico.
     * @param regista regista del film.
     * @param anno anno di uscita.
     * @param durata durata in minuti.
     * @param etaMinima eta minima consigliata o richiesta.
     */
    public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinima) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    /**
     * Restituisce il titolo del film.
     *
     * @return titolo del film.
     */
    public String getTitolo() {
        return titolo; 
    }

    /**
     * Restituisce il genere del film.
     *
     * @return genere del film.
     */
    public String getGenere() {
        return genere;
    }

    /**
     * Restituisce il regista del film.
     *
     * @return regista del film.
     */
    public String getRegista() {
        return regista;
    }

    /**
     * Restituisce l'anno di uscita.
     *
     * @return anno di uscita.
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Restituisce la durata del film.
     *
     * @return durata in minuti.
     */
    public int getDurata() {
        return durata;
    }

    /**
     * Restituisce l'eta minima richiesta.
     *
     * @return eta minima richiesta.
     */
    public int getEtaMinima() {
        return etaMinima;
    }

    /**
     * Imposta il titolo del film.
     *
     * @param titolo nuovo titolo.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Imposta il genere del film.
     *
     * @param genere nuovo genere.
     */
    public void setGenere(String genere) {
        this.genere = genere;
    }

    /**
     * Imposta il regista del film.
     *
     * @param regista nuovo regista.
     */
    public void setRegista(String regista) {
        this.regista = regista;
    }

    /**
     * Imposta l'anno di uscita.
     *
     * @param anno nuovo anno di uscita.
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }

    /**
     * Imposta la durata del film.
     *
     * @param durata nuova durata in minuti.
     */
    public void setDurata(int durata) {
        this.durata = durata;
    }

    /**
     * Imposta l'eta minima richiesta.
     *
     * @param etaMinima nuova eta minima.
     */
    public void setEtaMinima(int etaMinima) {
        this.etaMinima = etaMinima;
    }

    @Override
    public String toString() {
        return titolo.toUpperCase() + " (" + anno + ")\n" + genere + " - Regista: " + regista + "\nDurata: " + durata + " min \nEtà minima: " + etaMinima + "+";
    }
}
