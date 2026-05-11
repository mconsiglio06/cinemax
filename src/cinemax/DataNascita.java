package cinemax;

public class DataNascita {
    private int giorno;
    private int mese;
    private int anno;

    public DataNascita (int g, int m, int a) throws DateFormatException {
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

    public boolean isAfter(DataNascita altra) {
        if (this.anno > altra.anno)
            return true;
        if (this.anno == altra.anno && this.mese > altra.mese)
            return true;
        if (this.anno == altra.anno && this.mese == altra.mese && this.giorno > altra.giorno)
            return true;

        return false;
    }

    public boolean isBefore(DataNascita altra) {
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
    
    public int getGiorno() {
        return giorno;
    }

    public void setGiorno(int giorno) {
        this.giorno = giorno;
    }

    public int getMese() {
        return mese;
    }

    public void setMese(int mese) {
        this.mese = mese;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataNascita))
            return false;
        
        DataNascita d = (DataNascita) o;
        if(this.giorno == d.giorno && this.mese == d.mese && this.anno == d.anno) {
            return true;
        } else {
            return false;
        }
    }
}
