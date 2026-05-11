package cinemax;

public class Indirizzo {
    String via;
    String numeroCivico;
    String comune;
    String provincia;
    int cap;
    String nazione;

    int[] validCivici = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public Indirizzo(String via, String numeroCivico, String comune, String provincia, int cap, String nazione) {
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.comune = comune;
        this.provincia = provincia;
        this.cap = cap;
        this.nazione = nazione;
    }

    public boolean verifyCivico(String numeroCivico) {
        for (int civico : validCivici) {
            if (numeroCivico.equals(String.valueOf(civico))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.via + " " + this.numeroCivico + ", " + this.comune + " (" + this.provincia + ") - " + this.cap + ", " + this.nazione;
    }
}
