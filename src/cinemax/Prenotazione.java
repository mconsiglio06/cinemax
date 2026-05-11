package cinemax;

public class Prenotazione {
    private Cliente cliente;
    private Proiezione proiezione;
    private int numeroPosti;

    public Prenotazione(Cliente cliente, Proiezione proiezione, int numeroPosti) {
        this.cliente = cliente;
        this.proiezione = proiezione;
        this.numeroPosti = numeroPosti;
    }
    
    // Getter semplici
    public Cliente getCliente() { return cliente; }
    public Proiezione getProiezione() { return proiezione; }
    public int getNumeroPosti() { return numeroPosti; }
}