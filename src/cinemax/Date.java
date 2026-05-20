/**
 * Progetto: CineMax - Laboratorio Interdisciplinare A
 * Anno Accademico: 2025/2026
 * Sede: Varese (VA)
 * * Autori del progetto:
 * - Matteo Consiglio (Matricola: 765479)
 * - Giulia Alessandra Casagrande (Matricola: 765252)
 * - Valeria Ferrigno (Matricola: 766909)
 * - Edoardo Della Torre (Matricola: 766913)
 */

package cinemax;

import java.time.LocalDate;

/**
 * Rappresenta una data del calendario usando giorno, mese e anno.
 * La classe fornisce validazione del formato e confronti cronologici
 * utilizzati per proiezioni, utenti e prenotazioni.
 */
public class Date {
    private int giorno;
    private int mese;
    private int anno;

    /**
     * Restituisce la data corrente del sistema.
     *
     * @return data odierna.
     * @throws DateFormatException se la data corrente non potesse essere convertita.
     */
    public static Date today() throws DateFormatException {
        LocalDate localDate = LocalDate.now();
        return new Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
    }

    /**
     * Crea una data validando giorno, mese e anno.
     *
     * @param g giorno del mese.
     * @param m mese dell'anno.
     * @param a anno.
     * @throws DateFormatException se la combinazione giorno/mese/anno non e valida.
     */
    public Date (int g, int m, int a) throws DateFormatException {
        boolean v = verifyFormat(g, m, a);
        if(v) {
            this.giorno = g;
            this.mese = m;
            this.anno = a;
        } else {
            throw new DateFormatException();
        }
    }

    private boolean verifyFormat(int g, int m, int a) {
        if (m < 1 || m > 12 || g < 1) {
            return false;
        }

        boolean bisestile = (a % 400 == 0) || (a % 100 != 0 && a % 4 == 0);
        int[] giorniPerMese = { 31, (bisestile ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        return g <= giorniPerMese[m - 1];
    }

    /**
     * Indica se questa data e successiva a un'altra data.
     *
     * @param altra data da confrontare.
     * @return true se questa data e dopo {@code altra}.
     */
    public boolean isAfter(Date altra) {
        if (this.anno > altra.anno)
            return true;
        if (this.anno == altra.anno && this.mese > altra.mese)
            return true;
        if (this.anno == altra.anno && this.mese == altra.mese && this.giorno > altra.giorno)
            return true;

        return false;
    }

    /**
     * Indica se questa data e precedente a un'altra data.
     *
     * @param altra data da confrontare.
     * @return true se questa data e prima di {@code altra}.
     */
    public boolean isBefore(Date altra) {
        if (this.anno < altra.anno)
            return true;
        if (this.anno == altra.anno && this.mese < altra.mese)
            return true;
        if (this.anno == altra.anno && this.mese == altra.mese && this.giorno < altra.giorno)
            return true;

        return false;
    }

    
    //getters and setters
    


    @Override
    public String toString() {
        return this.giorno + "/" + this.mese + "/" + this.anno;
    }
    
    /**
     * Restituisce il giorno del mese.
     *
     * @return giorno del mese.
     */
    public int getGiorno() {
        return giorno;
    }

    /**
     * Imposta il giorno del mese.
     *
     * @param giorno nuovo giorno del mese.
     */
    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    /**
     * Restituisce il mese dell'anno.
     *
     * @return mese dell'anno.
     */
    public int getMese() {
        return mese;
    }

    /**
     * Imposta il mese dell'anno.
     *
     * @param mese nuovo mese dell'anno.
     */
    public void setMese(int mese) {
        this.mese = mese;
    }

    /**
     * Restituisce l'anno della data.
     *
     * @return anno della data.
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Imposta l'anno della data.
     *
     * @param anno nuovo anno della data.
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Date)) 
            return false;
        
        Date d = (Date) o;
        if(this.giorno == d.giorno && this.mese == d.mese && this.anno == d.anno) {
            return true;
        } else {
            return false;
        }
    }
}
