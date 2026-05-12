import cinemax.*;
import java.util.*;

public class MainCinemax {
    public static void main(String[] args) {
        CinemaManager sistema = new CinemaManager();
            Scanner tastiera = new Scanner(System.in);
            int scelta = -1;

            System.out.println("=== BENVENUTO IN CINEMAX ===");

            do {
                System.out.println("\n1. Login");
                System.out.println("2. Registrati (Nuovo Cliente)");
                System.out.println("0. Esci");
                System.out.print("Scegli un'opzione: ");
                
                scelta = tastiera.nextInt();
                tastiera.nextLine(); // Pulizia buffer dopo nextInt()

                switch (scelta) {
                    case 1:
                        System.out.print("Username: ");
                        String user = tastiera.nextLine();
                        System.out.print("Password: ");
                        String pass = tastiera.nextLine();

                        if (sistema.login(user, pass)) {
                            Utente corrente = sistema.getUtenteCorrente();
                            System.out.println("\nAccesso eseguito come " + corrente.getRuolo());
                            // Qui in futuro chiameremo i menu specifici (Cliente/Personale)
                        } else {
                            System.out.println("Credenziali errate.");
                        }
                        break;

                    case 2:
                        System.out.println("--- MODULO REGISTRAZIONE ---");
                        System.out.print("Nome: "); String n = tastiera.nextLine();
                        System.out.print("Cognome: "); String c = tastiera.nextLine();
                        System.out.print("Username: "); String u = tastiera.nextLine();
                        System.out.print("Password: "); String p = tastiera.nextLine();
                        System.out.print("Giorno nascita: "); int g = tastiera.nextInt();
                        System.out.print("Mese nascita: "); int m = tastiera.nextInt();
                        System.out.print("Anno nascita: "); int a = tastiera.nextInt();
                        tastiera.nextLine();
                        System.out.print("Luogo: "); String l = tastiera.nextLine();

                        sistema.registraNuovoCliente(n, c, u, p, g, m, a, l);
                        break;

                    case 0:
                        System.out.println("Arrivederci!");
                        break;

                    default:
                        System.out.println("Opzione non valida.");
                }
            } while (scelta != 0);

            tastiera.close();
    }
}