import cinemax.*;
import java.util.*;
import java.io.*;

public class MainCinemax {
    private static List<Proiezione> proiezioni = new ArrayList<>();
    private static List<Utente> utenti = new ArrayList<>();
    private static Utente utenteLoggato = null;

    public static void main(String[] args) {
        caricaDatiIniziali();
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n--- BENVENUTI IN CINEMAX ---");
            if (utenteLoggato == null) {
                menuGuest(sc);
            } else {
                menuUser(sc);
            }
        }
    }

    private static void caricaDatiIniziali() {
        // Qui dovresti implementare la lettura da data/proiezioni.csv e data/utenti.csv
        // Esempio manuale per test:
        try {
            Film f = new Film("Interstellar", "Sci-Fi", "Nolan", 2014, 169, 12);
            proiezioni.add(new Proiezione(f, new DataFilm(20, 5, 2024), new Ora(21, 0), 8.50));
            // Aggiungi utenti di default (2 proiezionisti, 5 bigliettai come da specifiche)
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void menuGuest(Scanner sc) {
        System.out.println("1. Vedi Proiezioni\n2. Login\n3. Esci");
        int scelta = sc.nextInt();
        switch (scelta) {
            case 1: proiezioni.forEach(System.out::println); break;
            case 2: login(sc); break;
            case 3: System.exit(0);
        }
    }

    private static void login(Scanner sc) {
        System.out.print("Username: "); String u = sc.next();
        System.out.print("Password: "); String p = sc.next();
        // Logica di verifica utenti...
        // Per ora simuliamo un cliente:
        utenteLoggato = new Cliente("Mario", "Rossi", u, p);
        System.out.println("Login effettuato come " + utenteLoggato.getRuolo());
    }

    private static void menuUser(Scanner sc) {
        if (utenteLoggato.getRuolo() == Ruolo.CLIENTE) {
            System.out.println("1. Prenota\n2. Mie Prenotazioni\n3. Logout");
            // Logica cliente...
        } else if (utenteLoggato.getRuolo().haPermessi()) {
            System.out.println("1. Gestione Proiezioni (Staff)\n2. Logout");
            // Logica staff...
        }
        if (sc.nextInt() == 3 || (utenteLoggato.getRuolo().haPermessi() && sc.nextInt() == 2)) 
            utenteLoggato = null;
    }
}