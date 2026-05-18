import cinemax.*;
import java.util.LinkedList;
import java.util.Scanner;

public class MainCinemax {
    private static final Scanner sc = new Scanner(System.in);
    private static final Manager manager = new Manager();
    private static Utente currentUser;
    private static int tentativiLoginFalliti = 0;

    public static void main(String[] args) {
        Utente.setManager(manager);
        System.out.println("---- CineMax - All'avanguardia del cinema ----");
        System.out.println("Premi INVIO per avviare il programma...");
        sc.nextLine();
        runApplication();
        sc.close();
    }

    private static void runApplication() {
        while (true) {
            if (currentUser == null || !currentUser.isSessionValid()) {
                if (currentUser != null) {
                    System.out.println("Sessione scaduta. Effettua nuovamente il login.");
                    currentUser.logout();
                    currentUser = null;
                }
                showLoginMenu();
                continue;
            }
            switch (currentUser.getRuolo()) {
                case CLIENTE:
                    customerMenu((Cliente) currentUser);
                    break;
                case PROIEZIONISTA:
                    proiezionistaMenu((Proiezionista) currentUser);
                    break;
                case BIGLIETTAIO:
                    bigliettaioMenu((Bigliettaio) currentUser);
                    break;
                default:
                    currentUser.logout();
                    currentUser = null;
                    break;
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n--- Menu principale ---");
        System.out.println("1 - Login");
        System.out.println("2 - Registrazione");
        System.out.println("-----");
        System.out.println("3 - Entra come ospite");
        System.out.println("---");
        System.out.println("0 - Esci");
        System.out.print("Scelta: ");
        int scelta = readInt();

        switch (scelta) {
            case 1:
                login();
                break;
            case 2:
                registerCliente();
                break;
            case 3:
                cercaFilmOspite();
                break;
            case 0:
                System.out.println("Grazie per aver utilizzato CineMax! Arrivederci!");
                System.exit(0);
                break;
            default:
                System.out.println("Scelta non valida. Riprovare.");
                break;
        }
    }
    private static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        Utente utente = manager.authenticate(username, password);
        if (utente == null) {
            registraLoginFallito();
            return;
        }

        String codiceRichiesto = getCodiceAccesso(utente.getRuolo());
        if (codiceRichiesto != null) {
            System.out.print("Inserisci il codice di accesso per " + utente.getRuolo() + ": ");
            String codice = sc.nextLine().trim();
            if (!codice.equals(codiceRichiesto)) {
                System.out.println("Codice di accesso non valido. Login annullato.");
                registraLoginFallito();
                return;
            }
        }

        currentUser = utente;
        currentUser.login();
        tentativiLoginFalliti = 0;
    }

    private static void registraLoginFallito() {
        tentativiLoginFalliti++;
        System.out.println("Credenziali non valide. Riprova.");
        if (tentativiLoginFalliti >= 5) {
            System.out.println("Chiusura automatica: multipli tentativi di login fallimentari.");
            System.exit(0);
        }
    }

    private static void registerCliente() {
        System.out.println("--- Registrazione ---");
        String nome = leggiCampoSoloLettere("Nome", "Errore: un nome non puo contenere numeri o simboli, minimo 2 caratteri");
        String cognome = leggiCampoSoloLettere("Cognome", "Errore: un cognome non puo contenere numeri o simboli, minimo 2 caratteri");
        String username = leggiUsernameValido();

        System.out.println("Ruolo: 1=Cliente, 2=Proiezionista, 3=Bigliettaio");
        int ruoloScelta = readInt();
        Ruolo ruolo;
        switch (ruoloScelta) {
            case 2:
                ruolo = Ruolo.PROIEZIONISTA;
                break;
            case 3:
                ruolo = Ruolo.BIGLIETTAIO;
                break;
            default:
                ruolo = Ruolo.CLIENTE;
                break;
        }

        String codiceRichiesto = getCodiceAccesso(ruolo);
        if (codiceRichiesto != null) {
            System.out.print("Inserisci il codice di accesso per " + ruolo + ": ");
            String codice = sc.nextLine().trim();
            if (!codice.equals(codiceRichiesto)) {
                System.out.println("Codice di accesso non valido. Registrazione annullata.");
                return;
            }
        }

        String password = leggiPasswordValida(username, ruolo);
        System.out.print("Data di nascita (gg mm aaaa, opzionale): ");
        String inputData = sc.nextLine().trim();
        String luogo = leggiCampoSoloLettere("Luogo", "Errore: un luogo non puo contenere numeri o simboli, minimo 2 caratteri");

        Date dataNascita;
        if (inputData.isEmpty()) {
            try {
                dataNascita = new Date(1, 1, 1900); // data di default per nascita opzionale
            } catch (DateFormatException e) {
                System.out.println("Errore nella data di default: " + e.getMessage());
                return;
            }
        } else {
            String[] parts = inputData.split("\\s+");
            if (parts.length != 3) {
                System.out.println("Formato data non valido. Usa gg mm aaaa.");
                return;
            }
            try {
                dataNascita = new Date(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            } catch (NumberFormatException | DateFormatException e) {
                System.out.println("Data di nascita non valida: " + e.getMessage());
                return;
            }
        }

        Utente utente;
        switch (ruolo) {
            case PROIEZIONISTA:
                utente = new Proiezionista(nome, cognome, username, password, dataNascita, luogo);
                break;
            case BIGLIETTAIO:
                utente = new Bigliettaio(nome, cognome, username, password, dataNascita, luogo);
                break;
            default:
                utente = new Cliente(nome, cognome, username, password, dataNascita, luogo);
                break;
        }
        utente.signup();
        currentUser = utente;
        currentUser.login();
    }

    private static String getCodiceAccesso(Ruolo ruolo) {
        if (ruolo == Ruolo.PROIEZIONISTA) {
            return "admin1";
        }
        if (ruolo == Ruolo.BIGLIETTAIO) {
            return "admin2";
        }
        return null;
    }

    private static String leggiCampoSoloLettere(String etichetta, String messaggioErrore) {
        while (true) {
            System.out.print(etichetta + ": ");
            String valore = sc.nextLine().trim();
            if (valore.matches("\\p{L}+(\\s+\\p{L}+)*") && contaLettere(valore) >= 2) {
                return valore;
            }
            System.out.println(messaggioErrore);
        }
    }

    private static String leggiUsernameValido() {
        while (true) {
            System.out.print("Username: ");
            String username = sc.nextLine().trim();
            if (username.length() < 6) {
                System.out.println("Errore: lo username deve avere lunghezza minima di 6 caratteri");
                continue;
            }
            if (manager.usernameExists(username)) {
                System.out.println("Errore: username gia in uso. Scegli un altro username.");
                continue;
            }
            return username;
        }
    }

    private static String leggiPasswordValida(String username, Ruolo ruolo) {
        int lunghezzaMinima = getCodiceAccesso(ruolo) == null ? 8 : 14;
        while (true) {
            System.out.print("Password: ");
            String password = sc.nextLine().trim();
            if (password.equals(username)) {
                System.out.println("Errore: username e password non possono essere uguali");
                continue;
            }
            if (password.length() < lunghezzaMinima) {
                System.out.println("Errore: la password deve avere lunghezza minima di " + lunghezzaMinima + " caratteri");
                continue;
            }
            if (contaLettere(password) < 4) {
                System.out.println("Errore: la password deve contenere almeno 4 lettere");
                continue;
            }
            return password;
        }
    }

    private static int contaLettere(String valore) {
        int count = 0;
        for (int i = 0; i < valore.length(); i++) {
            if (Character.isLetter(valore.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    private static void cercaFilmOspite() {
        System.out.println("\n--- Ricerca film come ospite ---\n-- Scegli i filtri di ricerca --");
        System.out.println("1 - Titolo");
        System.out.println("2 - Genere");
        System.out.println("3 - Regista");
        System.out.println("4 - Durata minima (minuti)");
        System.out.println("5 - Durata massima (minuti)");
        System.out.println("6 - Fascia di prezzo");
        System.out.println("7 - Data e ora");
        System.out.println("0 - Torna al menu principale");
        System.out.print("Scelta: ");
        int scelta = readInt();
        LinkedList<Proiezione> risultati = new LinkedList<>();

        switch (scelta) {
            case 1:
                System.out.print("Titolo film da cercare: ");
                String titolo = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 2:
                System.out.print("Genere da cercare: ");
                String genere = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getGenere().toLowerCase().contains(genere.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 3:
                System.out.print("Regista da cercare: ");
                String regista = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getRegista().toLowerCase().contains(regista.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 4:
                System.out.print("Durata minima in minuti: ");
                int durataMin = readInt();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getDurata() >= durataMin) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 5:
                System.out.print("Durata massima in minuti: ");
                int durataMax = readInt();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getDurata() <= durataMax) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 6:
                System.out.print("Prezzo minimo: ");
                double prezzoMin = readDouble();
                System.out.print("Prezzo massimo: ");
                double prezzoMax = readDouble();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    double prezzo = proiezione.getPrezzo();
                    if (prezzo >= prezzoMin && prezzo <= prezzoMax) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 7:
                LinkedList<Proiezione> risultatiDataOra = cercaProiezioniPerDataOra(true);
                if (risultatiDataOra == null) {
                    return;
                }
                risultati = risultatiDataOra;
                break;
            case 0:
                return;
            default:
                System.out.println("Scelta non valida.");
                return;
        }

        mostraRisultatiOspite(risultati);
    }

    private static void mostraRisultatiOspite(LinkedList<Proiezione> risultati) {
        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata con i criteri indicati.");
            return;
        }
        System.out.println("=== Risultati ricerca ospite ===");
        for (int i = 0; i < risultati.size(); i++) {
            stampaRisultatoRicerca(i + 1, risultati.get(i));
        }
        selezionaProiezionePerDettagli(risultati, null, false);
    }

    private static void mostraDettagliProiezione(Proiezione proiezione, Cliente cliente, boolean mostraIncasso) {
        if (proiezione == null) {
            return;
        }
        System.out.println("\n=== Dettagli proiezione ===");
        System.out.println("Titolo: " + proiezione.getFilm().getTitolo());
        System.out.println("Genere: " + proiezione.getFilm().getGenere());
        System.out.println("Regista: " + proiezione.getFilm().getRegista());
        System.out.println("Anno: " + proiezione.getFilm().getAnno());
        System.out.println("Durata: " + proiezione.getFilm().getDurata() + " minuti");
        System.out.println("Età minima: " + proiezione.getFilm().getEtaMinima());
        System.out.println("Data: " + proiezione.getDataProiezione());
        System.out.println("Ora: " + proiezione.getOraProiezione());
        System.out.println("Prezzo: EUR " + proiezione.getPrezzo());

        boolean[][] occupancy = manager.getOccupancyMap(proiezione);
        int postiOccupati = 0;
        int totalePosti = occupancy.length * occupancy[0].length;
        for (boolean[] rowSeats : occupancy) {
            for (boolean seat : rowSeats) {
                if (seat) {
                    postiOccupati++;
                }
            }
        }
        double percentuale = totalePosti == 0 ? 0 : 100.0 * postiOccupati / totalePosti;
        double incasso = postiOccupati * proiezione.getPrezzo();
        System.out.println("\n=== Statistiche proiezione ===");
        System.out.println("Posti occupati: " + postiOccupati + "/" + totalePosti + " (" + String.format("%.2f", percentuale) + "%)");
        if (mostraIncasso) {
            System.out.println("Incasso totale: EUR " + String.format("%.2f", incasso));
        }
        System.out.println("\nMappa sala (X = occupato):");
        System.out.println(renderSalaWithUserSeats(proiezione, null));

        if (cliente != null) {
            while (true) {
                System.out.println("\nOpzioni:");
                System.out.println("1 - Prenota questa proiezione");
                System.out.println("0 - Torna indietro");
                System.out.print("Scelta: ");
                int scelta = readInt();
                if (scelta == 1) {
                    LinkedList<Posto> seats = inputSeats();
                    if (seats.isEmpty()) {
                        System.out.println("Nessun posto valido inserito.");
                        continue;
                    }
                    if (cliente.prenota(proiezione, seats)) {
                        System.out.println("Prenotazione completata.");
                        break; // dopo prenotazione, torna
                    }
                } else if (scelta == 0) {
                    break;
                } else {
                    System.out.println("Scelta non valida.");
                }
            }
        } else {
            System.out.print("Premi invio per tornare...");
            sc.nextLine();
        }
    }

    private static boolean isFutureProiezione(Proiezione proiezione) {
        if (proiezione == null) {
            return false;
        }
        try {
            Date oggi = Date.today();
            if (proiezione.getDataProiezione().isAfter(oggi)) {
                return true;
            }
            if (proiezione.getDataProiezione().equals(oggi)) {
                Time now = Time.now();
                return now.isBefore(proiezione.getOraProiezione());
            }
        } catch (DateFormatException | TimeFormatException e) {
            System.out.println("Errore durante il controllo della data della proiezione: " + e.getMessage());
        }
        return false;
    }

    private static LinkedList<Proiezione> cercaProiezioniPerDataOra(boolean soloFuture) {
        Date data = leggiDataRicerca();
        if (data == null) {
            return null;
        }
        Time ora = leggiOraOpzionaleRicerca();
        if (ora == null && lastOptionalTimeInputInvalid) {
            return null;
        }

        LinkedList<Proiezione> risultati = new LinkedList<>();
        for (Proiezione proiezione : manager.getProiezioni()) {
            if (soloFuture && !isFutureProiezione(proiezione)) {
                continue;
            }
            if (!proiezione.getDataProiezione().equals(data)) {
                continue;
            }
            if (ora == null || proiezione.getOraProiezione().equals(ora)) {
                risultati.add(proiezione);
            }
        }
        return risultati;
    }

    private static Date leggiDataRicerca() {
        System.out.print("Data (gg mm aaaa): ");
        String[] dateParts = sc.nextLine().trim().split("\\s+");
        if (dateParts.length != 3) {
            System.out.println("Data non valida.");
            return null;
        }
        try {
            return new Date(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
        } catch (NumberFormatException | DateFormatException e) {
            System.out.println("Data non valida: " + e.getMessage());
            return null;
        }
    }

    private static boolean lastOptionalTimeInputInvalid = false;

    private static Time leggiOraOpzionaleRicerca() {
        lastOptionalTimeInputInvalid = false;
        System.out.print("Ora (hh:mm, invio per qualsiasi ora): ");
        String oraInput = sc.nextLine().trim();
        if (oraInput.isEmpty()) {
            return null;
        }
        String[] timeParts = oraInput.split(":");
        if (timeParts.length != 2) {
            System.out.println("Ora non valida.");
            lastOptionalTimeInputInvalid = true;
            return null;
        }
        try {
            return new Time(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
        } catch (NumberFormatException | TimeFormatException e) {
            System.out.println("Ora non valida: " + e.getMessage());
            lastOptionalTimeInputInvalid = true;
            return null;
        }
    }

    private static void cercaProiezioniCliente(Cliente cliente) {
        System.out.println("\n--- Cerca proiezioni cliente ---\n-- Scegli i filtri di ricerca --");
        System.out.println("1 - Titolo");
        System.out.println("2 - Genere");
        System.out.println("3 - Regista");
        System.out.println("4 - Durata minima (minuti)");
        System.out.println("5 - Durata massima (minuti)");
        System.out.println("6 - Fascia di prezzo");
        System.out.println("7 - Data e ora");
        System.out.println("0 - Torna al menu precedente");
        System.out.print("Scelta: ");
        int scelta = readInt();
        LinkedList<Proiezione> risultati = new LinkedList<>();

        switch (scelta) {
            case 1:
                System.out.print("Titolo film da cercare: ");
                String titolo = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 2:
                System.out.print("Genere da cercare: ");
                String genere = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getGenere().toLowerCase().contains(genere.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 3:
                System.out.print("Regista da cercare: ");
                String regista = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getRegista().toLowerCase().contains(regista.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 4:
                System.out.print("Durata minima in minuti: ");
                int durataMin = readInt();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getDurata() >= durataMin) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 5:
                System.out.print("Durata massima in minuti: ");
                int durataMax = readInt();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    if (proiezione.getFilm().getDurata() <= durataMax) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 6:
                System.out.print("Prezzo minimo: ");
                double prezzoMin = readDouble();
                System.out.print("Prezzo massimo: ");
                double prezzoMax = readDouble();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (!isFutureProiezione(proiezione)) {
                        continue;
                    }
                    double prezzo = proiezione.getPrezzo();
                    if (prezzo >= prezzoMin && prezzo <= prezzoMax) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 7:
                LinkedList<Proiezione> risultatiDataOra = cercaProiezioniPerDataOra(true);
                if (risultatiDataOra == null) {
                    return;
                }
                risultati = risultatiDataOra;
                break;
            case 0:
                return;
            default:
                System.out.println("Scelta non valida.");
                return;
        }

        mostraRisultatiCliente(cliente, risultati);
    }

    private static void mostraRisultatiCliente(Cliente cliente, LinkedList<Proiezione> risultati) {
        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata con i criteri indicati.");
            return;
        }
        System.out.println("=== Risultati ricerca cliente ===");
        for (int i = 0; i < risultati.size(); i++) {
            stampaRisultatoRicerca(i + 1, risultati.get(i));
        }
        selezionaProiezionePerDettagli(risultati, cliente, false);
    }

    private static void stampaRisultatoRicerca(int indice, Proiezione proiezione) {
        System.out.println(indice + " - " + proiezione.getFilm().getTitolo()
                + " | " + proiezione.getFilm().getGenere()
                + " | " + proiezione.getFilm().getRegista()
                + " | " + proiezione.getFilm().getDurata() + " min"
                + " | Data: " + proiezione.getDataProiezione()
                + " | Ora: " + proiezione.getOraProiezione()
                + " | EUR " + proiezione.getPrezzo());
    }

    private static void selezionaProiezionePerDettagli(LinkedList<Proiezione> risultati, Cliente cliente, boolean mostraIncasso) {
        if (risultati == null || risultati.isEmpty()) {
            return;
        }
        System.out.print("Inserisci il numero della proiezione per vedere i dettagli e le statistiche (0 per tornare): ");
        int scelta = readInt();
        if (scelta <= 0 || scelta > risultati.size()) {
            return;
        }
        mostraDettagliProiezione(risultati.get(scelta - 1), cliente, mostraIncasso);
    }

    private static void mostraDettagliPrenotazione(Prenotazione prenotazione) {
        if (prenotazione == null || prenotazione.getProiezione() == null) {
            return;
        }
        Proiezione proiezione = prenotazione.getProiezione();
        System.out.println("\n=== Dettagli prenotazione ===");
        System.out.println("ID Prenotazione: " + prenotazione.getId());
        System.out.println("Titolo: " + proiezione.getFilm().getTitolo());
        System.out.println("Genere: " + proiezione.getFilm().getGenere());
        System.out.println("Regista: " + proiezione.getFilm().getRegista());
        System.out.println("Anno: " + proiezione.getFilm().getAnno());
        System.out.println("Durata: " + proiezione.getFilm().getDurata() + " minuti");
        System.out.println("Età minima: " + proiezione.getFilm().getEtaMinima());
        System.out.println("Data: " + proiezione.getDataProiezione());
        System.out.println("Ora: " + proiezione.getOraProiezione());
        System.out.println("Prezzo unitario: EUR " + proiezione.getPrezzo());
        System.out.println("Posti prenotati: " + prenotazione.getSeats());
        System.out.println("Prezzo totale: EUR " + prenotazione.getSpesa());
        System.out.println("\nMappa sala (X = occupato):");
        System.out.println(renderSalaWithUserSeats(proiezione, prenotazione.getSeats()));
        System.out.println("Posti prenotati dal cliente: " + prenotazione.getSeats());
    }

    private static String renderSalaWithUserSeats(Proiezione proiezione, LinkedList<Posto> userSeats) {
        boolean[][] occupancy = manager.getOccupancyMap(proiezione);
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int col = 1; col <= 20; col++) {
            sb.append(String.format("%4d", col));
        }
        sb.append("\n");
        for (int row = 1; row <= 10; row++) {
            sb.append((char) ('A' + row - 1)).append("  ");
            for (int col = 1; col <= 20; col++) {
                boolean isOccupied = occupancy[row - 1][col - 1];
                if (isOccupied) {
                    sb.append(" [X]");
                } else {
                    sb.append(" [ ]");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void gestisciStoricoCliente(Cliente cliente) {
        LinkedList<Prenotazione> storico = cliente.storico();
        if (storico.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata.");
            return;
        }
        System.out.println("=== Storico prenotazioni ===");
        for (int i = 0; i < storico.size(); i++) {
            Prenotazione prenotazione = storico.get(i);
            System.out.println((i + 1) + " - ID: " + prenotazione.getId() + " | " + prenotazione.getProiezione().getFilm().getTitolo() + " | Data: " + prenotazione.getProiezione().getDataProiezione() + " | Ora: " + prenotazione.getProiezione().getOraProiezione() + " | Posti: " + prenotazione.getSeats() + " | Prezzo totale: EUR " + prenotazione.getSpesa());
        }
        System.out.print("Seleziona prenotazione da gestire (0 per tornare): ");
        int scelta = readInt();
        if (scelta <= 0 || scelta > storico.size()) {
            return;
        }
        Prenotazione selezionata = storico.get(scelta - 1);
        mostraDettagliPrenotazione(selezionata);
        System.out.println("\nOpzioni:");
        System.out.println("1 - Modifica data e ora");
        System.out.println("2 - Modifica posti");
        System.out.println("3 - Annulla prenotazione");
        System.out.println("0 - Torna indietro");
        System.out.print("Scelta: ");
        int azione = readInt();
        switch (azione) {
            case 1:
                modificaDataOra(cliente, selezionata);
                break;
            case 2:
                modificaPosti(cliente, selezionata);
                break;
            case 3:
                if (cliente.cancel(selezionata)) {
                    System.out.println("Prenotazione annullata con successo.");
                }
                break;
            case 0:
                return;
            default:
                System.out.println("Scelta non valida.");
        }
    }

    private static void modificaDataOra(Cliente cliente, Prenotazione prenotazione) {
        String titolo = prenotazione.getProiezione().getFilm().getTitolo();
        LinkedList<Proiezione> altreProiezioni = new LinkedList<>();
        for (Proiezione p : manager.getProiezioni()) {
            if (p.getFilm().getTitolo().equalsIgnoreCase(titolo) && !p.equals(prenotazione.getProiezione()) && isFutureProiezione(p)) {
                altreProiezioni.add(p);
            }
        }
        if (altreProiezioni.isEmpty()) {
            System.out.println("Nessuna altra proiezione disponibile per questo film.");
            return;
        }
        System.out.println("Altre proiezioni disponibili per '" + titolo + "':");
        for (int i = 0; i < altreProiezioni.size(); i++) {
            Proiezione p = altreProiezioni.get(i);
            System.out.println((i + 1) + " - Data: " + p.getDataProiezione() + " | Ora: " + p.getOraProiezione() + " | Prezzo: EUR " + p.getPrezzo());
        }
        System.out.print("Seleziona nuova proiezione (0 per annullare): ");
        int scelta = readInt();
        if (scelta <= 0 || scelta > altreProiezioni.size()) {
            return;
        }
        Proiezione nuova = altreProiezioni.get(scelta - 1);
        if (cliente.changeProiezione(prenotazione, nuova)) {
            System.out.println("Data e ora modificati con successo.");
        }
    }

    private static void modificaPosti(Cliente cliente, Prenotazione prenotazione) {
        System.out.println("Mappa attuale (X = occupato):");
        System.out.println(renderSalaWithUserSeats(prenotazione.getProiezione(), prenotazione.getSeats()));
        System.out.println("Posti attualmente prenotati: " + prenotazione.getSeats());
        while (true) {
            System.out.println("Inserisci i nuovi posti (lascia vuoto per annullare):");
            LinkedList<Posto> nuoviPosti = inputSeats();
            if (nuoviPosti.isEmpty()) {
                System.out.println("Modifica annullata.");
                return;
            }
            if (cliente.areSeatsInvalid(prenotazione.getProiezione(), nuoviPosti)) {
                System.out.println("Alcuni dei posti selezionati non esistono. Riprovare.");
                continue;
            }
            if (cliente.areSeatsOccupied(prenotazione.getProiezione(), nuoviPosti)) {
                System.out.println("Uno o piu posti selezionati sono occupati. Riprovare.");
                continue;
            }
            if (cliente.edit(prenotazione, nuoviPosti)) {
                System.out.println("Posti modificati con successo.");
            }
            return;
        }
    }

    private static LinkedList<Posto> inputSeats() {
        LinkedList<Posto> seats = new LinkedList<>();
        System.out.print("Inserisci i posti separati da ';' (es. A-1;A-2): ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            return seats;
        }
        String[] tokens = input.split(";");
        for (String token : tokens) {
            String[] parts = token.trim().split("-");
            if (parts.length == 2) {
                try {
                    String fila = parts[0].trim().toUpperCase();
                    int numero = Integer.parseInt(parts[1].trim());
                    seats.add(new Posto(fila, numero));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return seats;
    }

    private static void customerMenu(Cliente cliente) {
        while (cliente.isSessionValid()) {
            System.out.println("\n--- Menu Cliente ---");
            System.out.println("1 - Cerca proiezioni");
            System.out.println("2 - Visualizza storico prenotazioni");
            System.out.println("0 - Logout");
            System.out.print("Scelta: ");
            int scelta = readInt();
            switch (scelta) {
                case 1:
                    cercaProiezioniCliente(cliente);
                    break;
                case 2:
                    gestisciStoricoCliente(cliente);
                    break;
                case 0:
                    cliente.logout();
                    currentUser = null;
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
        System.out.println("Sessione scaduta. Effettua il login nuovamente.");
        currentUser = null;
    }

    private static void proiezionistaMenu(Proiezionista proiezionista) {
        while (proiezionista.isSessionValid()) {
            System.out.println("\n--- Menu Proiezionista ---");
            System.out.println("1 - Visualizza tutte le proiezioni");
            System.out.println("2 - Cerca proiezione");
            System.out.println("3 - Aggiungi proiezione");
            System.out.println("4 - Rimuovi proiezione");
            System.out.println("5 - Modifica proiezione");
            System.out.println("0 - Logout");
            System.out.print("Scelta: ");
            int scelta = readInt();
            switch (scelta) {
                case 1:
                    LinkedList<Proiezione> proiezioni = manager.getProiezioni();
                    for (int i = 0; i < proiezioni.size(); i++) {
                        System.out.println((i + 1) + " - " + proiezioni.get(i));
                        System.out.println("-------------------------");
                    }
                    selezionaProiezionePerDettagli(proiezioni, null, true);
                    break;
                case 2:
                    cercaProiezioneProiezionista(proiezionista);
                    break;
                case 3:
                    aggiungiProiezione(proiezionista);
                    break;
                case 4:
                    rimuoviProiezione(proiezionista);
                    break;
                case 5:
                    modificaProiezione(proiezionista);
                    break;
                case 0:
                    proiezionista.logout();
                    currentUser = null;
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
        System.out.println("Sessione scaduta. Effettua il login nuovamente.");
        currentUser = null;
    }

    private static void aggiungiProiezione(Proiezionista proiezionista) {
        System.out.println("--- Aggiungi nuova proiezione ---");
        System.out.print("Titolo film: ");
        String titolo = sc.nextLine().trim();
        if (titolo.isEmpty()) {
            System.out.println("Titolo non valido. Operazione annullata.");
            return;
        }
        System.out.print("Durata film (minuti): ");
        int durata = readInt();
        if (durata <= 0) {
            System.out.println("Durata non valida. Operazione annullata.");
            return;
        }
        System.out.print("Data proiezione (gg mm aaaa): ");
        String[] dateParts = sc.nextLine().trim().split("\\s+");
        if (dateParts.length != 3) {
            System.out.println("Data non valida. Operazione annullata.");
            return;
        }
        System.out.print("Ora proiezione (hh:mm): ");
        String oraInput = sc.nextLine().trim();
        if (oraInput.isEmpty()) {
            System.out.println("Ora non valida. Operazione annullata.");
            return;
        }
        Date data;
        Time ora;
        try {
            data = new Date(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
            String[] hms = oraInput.split(":");
            ora = new Time(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]));
        } catch (NumberFormatException | DateFormatException | TimeFormatException e) {
            System.out.println("Data/ora non valida: " + e.getMessage() + ". Operazione annullata.");
            return;
        }

        Film filmTemp = new Film(titolo, "", "", 0, durata, 0);
        Proiezione proiezioneTemp = new Proiezione(filmTemp, data, ora, 0);
        if (manager.isOverlapping(proiezioneTemp)) {
            System.out.println("Impossibile aggiungere la proiezione: intervallo orario sovrapposto a una proiezione esistente.");
            return;
        }

        Proiezione nuova = chiediProiezioneDaInput(titolo, durata, data, ora);
        if (nuova == null) {
            return;
        }
        proiezionista.aggiungiProiezione(nuova);
    }

    private static void cercaProiezioneProiezionista(Proiezionista proiezionista) {
        System.out.println("--- Cerca proiezione ---");
        Proiezione proiezione = selezionaProiezioneConFiltri();
        if (proiezione == null) {
            return;
        }
        mostraDettagliProiezione(proiezione, null, true);
    }

    private static Proiezione selezionaProiezioneConFiltri() {
        System.out.println("--- Seleziona proiezione ---");
        System.out.println("1 - Cerca per titolo");
        System.out.println("2 - Cerca per genere");
        System.out.println("3 - Cerca per regista");
        System.out.println("4 - Cerca per durata minima");
        System.out.println("5 - Cerca per durata massima");
        System.out.println("6 - Cerca per data e ora");
        System.out.println("7 - Vedi lista intera");
        System.out.println("0 - Annulla");
        System.out.print("Scelta: ");
        int scelta = readInt();
        LinkedList<Proiezione> risultati = new LinkedList<>();
        switch (scelta) {
            case 1:
                System.out.print("Titolo o parte: ");
                String titolo = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (isFutureProiezione(proiezione) && proiezione.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 2:
                System.out.print("Genere: ");
                String genere = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (isFutureProiezione(proiezione) && proiezione.getFilm().getGenere().toLowerCase().contains(genere.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 3:
                System.out.print("Regista: ");
                String regista = sc.nextLine().trim();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (isFutureProiezione(proiezione) && proiezione.getFilm().getRegista().toLowerCase().contains(regista.toLowerCase())) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 4:
                System.out.print("Durata minima (minuti): ");
                int durataMin = readInt();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (isFutureProiezione(proiezione) && proiezione.getFilm().getDurata() >= durataMin) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 5:
                System.out.print("Durata massima (minuti): ");
                int durataMax = readInt();
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (isFutureProiezione(proiezione) && proiezione.getFilm().getDurata() <= durataMax) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 6:
                LinkedList<Proiezione> risultatiDataOra = cercaProiezioniPerDataOra(true);
                if (risultatiDataOra == null) {
                    return null;
                }
                risultati = risultatiDataOra;
                break;
            case 7:
                for (Proiezione proiezione : manager.getProiezioni()) {
                    if (isFutureProiezione(proiezione)) {
                        risultati.add(proiezione);
                    }
                }
                break;
            case 0:
                return null;
            default:
                System.out.println("Scelta non valida.");
                return null;
        }
        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata.");
            return null;
        }
        System.out.println("=== Seleziona proiezione ===");
        for (int i = 0; i < risultati.size(); i++) {
            stampaAnteprimaProiezione(i + 1, risultati.get(i));
        }
        System.out.print("Numero proiezione (0 per annullare): ");
        int sceltaFinale = readInt();
        if (sceltaFinale <= 0 || sceltaFinale > risultati.size()) {
            return null;
        }
        return risultati.get(sceltaFinale - 1);
    }

    private static void stampaAnteprimaProiezione(int indice, Proiezione proiezione) {
        System.out.println(indice + " - " + proiezione);
        boolean[][] occupancy = manager.getOccupancyMap(proiezione);
        int postiOccupati = 0;
        int totalePosti = occupancy.length * occupancy[0].length;
        for (boolean[] rowSeats : occupancy) {
            for (boolean seat : rowSeats) {
                if (seat) {
                    postiOccupati++;
                }
            }
        }
        System.out.println("Posti occupati: " + postiOccupati + " su " + totalePosti);
        System.out.println("-------------------------");
    }

    private static void rimuoviProiezione(Proiezionista proiezionista) {
        System.out.println("--- Rimuovi proiezione ---");
        Proiezione proiezione = selezionaProiezioneConFiltri();
        if (proiezione == null) {
            return;
        }
        proiezionista.rimuoviProiezione(proiezione);
    }

    private static void modificaProiezione(Proiezionista proiezionista) {
        System.out.println("--- Modifica proiezione ---");
        Proiezione originale = selezionaProiezioneConFiltri();
        if (originale == null) {
            return;
        }
        System.out.println("Inserisci i nuovi dati per la proiezione (lascia vuoto titolo per annullare):");
        Proiezione aggiornata = chiediProiezioneDaInput();
        if (aggiornata == null) {
            return;
        }
        proiezionista.modificaProiezione(originale, aggiornata);
    }

    private static void visualizzaStatisticheProiezione(Proiezionista proiezionista) {
        System.out.println("--- Statistiche sala proiezione ---");
        Proiezione proiezione = selezionaProiezioneConFiltri();
        if (proiezione == null) {
            return;
        }
        proiezionista.visualizzaStatistiche(proiezione);
    }

    private static Proiezione chiediProiezioneDaInput() {
        System.out.print("Titolo film: ");
        String titolo = sc.nextLine().trim();
        if (titolo.isEmpty()) {
            System.out.println("Operazione annullata.");
            return null;
        }
        System.out.print("Durata film (minuti): ");
        int durata = readInt();
        System.out.print("Data proiezione (gg mm aaaa): ");
        String[] dateParts = sc.nextLine().trim().split("\\s+");
        System.out.print("Ora proiezione (hh:mm): ");
        String oraInput = sc.nextLine().trim();
        if (titolo.isEmpty() || durata <= 0 || dateParts.length != 3 || oraInput.isEmpty()) {
            System.out.println("Dati mancanti o non validi. Operazione annullata.");
            return null;
        }
        try {
            Date data = new Date(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
            String[] hms = oraInput.split(":");
            Time ora = new Time(Integer.parseInt(hms[0]), Integer.parseInt(hms[1]));
            return chiediProiezioneDaInput(titolo, durata, data, ora);
        } catch (NumberFormatException | DateFormatException | TimeFormatException e) {
            System.out.println("Dati di proiezione non validi: " + e.getMessage());
            return null;
        }
    }

    private static Proiezione chiediProiezioneDaInput(String titolo, int durata, Date data, Time ora) {
        System.out.println("Titolo film: " + titolo);
        System.out.print("Genere: ");
        String genere = sc.nextLine().trim();
        System.out.print("Regista: ");
        String regista = sc.nextLine().trim();
        System.out.print("Anno film: ");
        int anno = readInt();
        System.out.print("Età minima: ");
        int etaMinima = readInt();
        System.out.print("Prezzo: ");
        double prezzo = readDouble();
        if (titolo.isEmpty() || genere.isEmpty() || regista.isEmpty() || anno <= 0 || etaMinima < 0 || prezzo < 0) {
            System.out.println("Dati mancanti o non validi. Operazione annullata.");
            return null;
        }
        Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
        return new Proiezione(film, data, ora, prezzo);
    }

    private static void mostraReportIncassi() {
        try {
            Date oggi = Date.today();
            System.out.println("=== Report incassi ===");
            System.out.println("Incassi giornalieri: EUR " + String.format("%.2f", manager.getDailyRevenue(oggi)));
            System.out.println("Incassi settimanali: EUR " + String.format("%.2f", manager.getWeeklyRevenue(oggi)));
            System.out.println("Incassi mensili: EUR " + String.format("%.2f", manager.getMonthlyRevenue(oggi)));
            System.out.println("Incassi annui: EUR " + String.format("%.2f", manager.getYearlyRevenue(oggi)));
        } catch (DateFormatException e) {
            System.out.println("Errore nel recupero della data odierna: " + e.getMessage());
        }
    }

    private static void bigliettaioMenu(Bigliettaio bigliettaio) {
        while (bigliettaio.isSessionValid()) {
            System.out.println("\n--- Menu Bigliettaio ---");
            System.out.println("1 - Vedi proiezioni giornaliere");
            System.out.println("2 - Cerca prenotazioni oggi");
            System.out.println("3 - Cerca prenotazione per ID");
            System.out.println("4 - Cerca prenotazioni per cliente");
            System.out.println("5 - Report incassi");
            System.out.println("6 - Cerca e visualizza proiezioni nel dettaglio");
            System.out.println("0 - Logout");
            System.out.print("Scelta: ");
            int scelta = readInt();
            switch (scelta) {
                case 1:
                    bigliettaio.vediProiezioniGiornaliere();
                    break;
                case 2:
                    bigliettaio.searchPrenotazioni();
                    break;
                case 3:
                    System.out.print("ID prenotazione: ");
                    int id = readInt();
                    bigliettaio.searchPrenotazioni(id);
                    break;
                case 4:
                    System.out.print("Nome, cognome o username cliente: ");
                    String query = sc.nextLine().trim();
                    bigliettaio.searchPrenotazioni(query);
                    break;
                case 5:
                    mostraReportIncassi();
                    break;
                case 6:
                    Proiezione proiezioneDettaglio = selezionaProiezioneConFiltri();
                    if (proiezioneDettaglio != null) {
                        mostraDettagliProiezione(proiezioneDettaglio, null, true);
                    }
                    break;
                case 0:
                    bigliettaio.logout();
                    currentUser = null;
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
        System.out.println("Sessione scaduta. Effettua il login nuovamente.");
        currentUser = null;
    }

    private static int readInt() {
        try {
            String line = sc.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            String line = sc.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
