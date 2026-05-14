import java.util.Scanner;

public class MainCinemax {
    public static void main(String[] args) {
        System.out.println("---- CineMax - All'avanguardia del cinema ----");
        System.out.println("Effettua l'accesso per prenotare i tuoi film preferiti..." +
            "\nOppure accedi come amministratore per gestirli!"
        );

        System.out.println("\n--------" +
            "\n1 - Accedi come cliente" +
            "\n2 - Accedi come amministratore" +
            "\n3 - Registrati" +
            "\n------------------------------" +
            "\n4 - Entra come ospite" +
            "\n0 - Chiudi il programma"
        );

        Scanner sc = new Scanner(System.in);

        int init = sc.nextInt();

        while(init < 0 || init > 4) {
            System.out.println("Scelta non valida, riprova!");
            init = sc.nextInt();
        }

        switch (init) {
            case 1:
                // Logica per accedere come cliente
                System.out.println("(Lasciare il campo vuoto per annullare l'azione)");
                System.out.print("Username: ");
                String username = sc.next();
                System.out.println("(Lasciare il campo vuoto per annullare l'azione)");
                System.out.print("Password: ");
                String password = sc.next();

                System.out.println("Accesso effettuato con successo! Benvenuto, " + username + "!");

                break;
            case 2:
                // Logica per accedere come amministratore
                break;
            case 3:
                // Logica per registrarsi
                break;
            case 4:
                // Logica per entrare come ospite
                break;
            case 0:
                System.out.println("Grazie per aver utilizzato CineMax! Arrivederci!");
                sc.close();
                return;
        }

    }
}