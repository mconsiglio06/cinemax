package cinemax;

import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;

/**
 * Classe di servizio centrale dell'applicazione CineMax.
 * Gestisce autenticazione, persistenza su file CSV, proiezioni, prenotazioni,
 * mappe di occupazione e calcolo degli incassi.
 */
public class Manager {
    // Questa classe gestirà la registrazione, il login e la gestione degli utenti
    // Per ora è solo una struttura vuota, ma in futuro potrà essere implementata con funzionalità reali

    private static final String PROIEZIONI_FILE = "data/proiezioni.csv";
    private static final String UTENTI_FILE = "data/utenti.csv";
    private static final String PRENOTAZIONI_FILE = "data/prenotazioni.csv";

    LinkedList<Proiezione> proiezioni = new LinkedList<>();

    /**
     * Inizializza il manager e allinea il contatore degli ID prenotazione.
     */
    public Manager() {
        initializePrenotazioneIdCounter();
    }

    private void initializePrenotazioneIdCounter() {
        File file = new File(PRENOTAZIONI_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int id = parsePrenotazioneId(line);
                Prenotazione.ensureIdCounterAtLeast(id);
            }
        } catch (IOException e) {
            System.out.println("Errore durante l'inizializzazione degli ID delle prenotazioni: " + e.getMessage());
        }
    }

    /**
     * Salva un utente nel file utenti.csv con la password cifrata
     * @param utente l'utente da salvare
     */
    /**
     * Salva un utente nel file CSV degli utenti.
     *
     * @param utente utente da salvare.
     */
    public void saveUtente(Utente utente) {
        try {
            ensureUtentiHeader();
            try (FileWriter fw = new FileWriter(UTENTI_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String passwordCifrata = SecurityUtils.hashPassword(utente.getPassword());
                String line = quote(utente.getNome()) + "," + 
                             quote(utente.getCognome()) + "," + 
                             utente.getUsername() + "," + 
                             passwordCifrata + "," + 
                             utente.getDataNascita().getGiorno() + "," + 
                             utente.getDataNascita().getMese() + "," + 
                             utente.getDataNascita().getAnno() + "," + 
                             quote(utente.getLuogo()) + "," + 
                             utente.getRuolo();
                bw.write(line);
                bw.newLine();
                System.out.println("Utente salvato con successo: " + utente.getUsername());
            }
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio dell'utente: " + e.getMessage());
        }
    }

    private void ensureUtentiHeader() throws IOException {
        File file = new File(UTENTI_FILE);
        if (!file.exists() || file.length() == 0) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("nome,cognome,username,password,giorno,mese,anno,luogo,ruolo");
                bw.newLine();
            }
        }
    }

    /**
     * Verifica se un username è già registrato
     * @param username l'username da verificare
     * @return true se esiste, false altrimenti
     */
    /**
     * Verifica se uno username e gia presente nel file utenti.
     *
     * @param username username da controllare.
     * @return true se lo username esiste.
     */
    public boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(UTENTI_FILE))) {
            String line = reader.readLine();
            if (line == null) {
                return false;
            }
            if (!isHeaderLine(line)) {
                String[] field = splitCsv(line);
                if (field.length > 2 && field[2].equals(username)) {
                    return true;
                }
            }
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] field = splitCsv(line);
                if (field.length > 2 && field[2].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la verifica dell'username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Autentica un utente confrontando username e password cifrata.
     *
     * @param username username inserito.
     * @param password password in chiaro inserita.
     * @return utente autenticato, oppure null.
     */
    public Utente authenticate(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(UTENTI_FILE))) {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            if (isHeaderLine(line)) {
                line = reader.readLine();
            }
            while (line != null) {
                if (line.trim().isEmpty()) {
                    line = reader.readLine();
                    continue;
                }
                String[] field = splitCsv(line);
                if (field.length < 9) {
                    line = reader.readLine();
                    continue;
                }
                String savedUsername = field[2];
                String savedHash = field[3];
                if (!savedUsername.equals(username)) {
                    line = reader.readLine();
                    continue;
                }
                String requestedHash = SecurityUtils.hashPassword(password);
                if (!requestedHash.equals(savedHash)) {
                    return null;
                }
                String nome = unquote(field[0]);
                String cognome = unquote(field[1]);
                int giorno = Integer.parseInt(field[4]);
                int mese = Integer.parseInt(field[5]);
                int anno = Integer.parseInt(field[6]);
                String luogo = unquote(field[7]);
                Ruolo ruolo = Ruolo.valueOf(field[8]);
                Date dataNascita = new Date(giorno, mese, anno);
                return buildUtente(nome, cognome, savedUsername, password, dataNascita, luogo, ruolo);
            }
        } catch (IOException | DateFormatException e) {
            System.out.println("Errore durante l'autenticazione: " + e.getMessage());
        }
        return null;
    }

    private boolean isHeaderLine(String line) {
        String[] field = splitCsv(line);
        return field.length > 2 && field[2].equalsIgnoreCase("username");
    }

    private Utente buildUtente(String nome, String cognome, String username, String password, Date dataNascita, String luogo, Ruolo ruolo) {
        switch (ruolo) {
            case CLIENTE:
                return new Cliente(nome, cognome, username, password, dataNascita, luogo);
            case PROIEZIONISTA:
                return new Proiezionista(nome, cognome, username, password, dataNascita, luogo);
            case BIGLIETTAIO:
                return new Bigliettaio(nome, cognome, username, password, dataNascita, luogo);
            default:
                return null;
        }
    }

    /**
     * Salva una prenotazione nel file CSV.
     *
     * @param cliente cliente proprietario della prenotazione.
     * @param prenotazione prenotazione da salvare.
     */
    public void savePrenotazione(Cliente cliente, Prenotazione prenotazione) {
        try {
            ensurePrenotazioniHeader();
            try (FileWriter fw = new FileWriter(PRENOTAZIONI_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                String seatString = encodeSeats(prenotazione.getSeats());
                String line = prenotazione.getId() + "," +
                              quote(cliente.getNome()) + "," +
                              quote(cliente.getCognome()) + "," +
                              quote(cliente.getUsername()) + "," +
                              quote(prenotazione.getProiezione().getFilm().getTitolo()) + "," +
                              prenotazione.getProiezione().getDataProiezione().getGiorno() + "," +
                              prenotazione.getProiezione().getDataProiezione().getMese() + "," +
                              prenotazione.getProiezione().getDataProiezione().getAnno() + "," +
                              prenotazione.getProiezione().getOraProiezione() + "," +
                              prenotazione.getNumPosti() + "," +
                              quote(seatString);
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Prenotazione salvata per: " + cliente.getUsername());
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della prenotazione: " + e.getMessage());
        }
    }

    /**
     * Calcola il primo ID prenotazione disponibile.
     *
     * @return ID libero piu basso.
     */
    public int getNextPrenotazioneId() {
        boolean[] used = new boolean[10000];
        File file = new File(PRENOTAZIONI_FILE);
        if (!file.exists()) {
            return 1;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int id = parsePrenotazioneId(line);
                if (id > 0 && id < used.length) {
                    used[id] = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante il calcolo del prossimo ID prenotazione: " + e.getMessage());
        }
        for (int id = 1; id < used.length; id++) {
            if (!used[id]) {
                return id;
            }
        }
        return used.length;
    }

    /**
     * Restituisce le prenotazioni associate a uno username.
     *
     * @param username username del cliente.
     * @return lista delle prenotazioni trovate.
     */
    public LinkedList<Prenotazione> getPrenotazioniByUser(String username) {
        LinkedList<Prenotazione> elenco = new LinkedList<>();
        try {
            ensurePrenotazioniHeader();
        } catch (IOException e) {
            System.out.println("Errore durante l'inizializzazione del file prenotazioni: " + e.getMessage());
            return elenco;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(PRENOTAZIONI_FILE))) {
            reader.readLine(); // intestazione
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Prenotazione p = parsePrenotazioneFromCsv(line);
                if (p != null && p.getProiezione() != null && p.getSpesa() >= 0) {
                    String[] fields = splitCsv(line);
                    if (fields.length > 3 && fields[3].equals(username)) {
                        elenco.add(p);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento delle prenotazioni: " + e.getMessage());
        }
        return elenco;
    }

    /**
     * Restituisce le prenotazioni associate a una proiezione.
     *
     * @param proiezione proiezione da cercare.
     * @return lista delle prenotazioni trovate.
     */
    public LinkedList<Prenotazione> getPrenotazioniByProiezione(Proiezione proiezione) {
        LinkedList<Prenotazione> elenco = new LinkedList<>();
        if (proiezione == null) {
            return elenco;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(PRENOTAZIONI_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Prenotazione p = parsePrenotazioneFromCsv(line);
                if (p != null && p.getProiezione() != null && sameProiezione(p.getProiezione(), proiezione)) {
                    elenco.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento delle prenotazioni per la proiezione: " + e.getMessage());
        }
        return elenco;
    }

    /**
     * Verifica se una proiezione ha prenotazioni associate.
     *
     * @param proiezione proiezione da controllare.
     * @return true se esiste almeno una prenotazione.
     */
    public boolean hasPrenotazioni(Proiezione proiezione) {
        return !getPrenotazioniByProiezione(proiezione).isEmpty();
    }

    /**
     * Restituisce le proiezioni programmate in una data.
     *
     * @param data data di filtro.
     * @return lista delle proiezioni trovate.
     */
    public LinkedList<Proiezione> getProiezioniByDate(Date data) {
        LinkedList<Proiezione> elenco = new LinkedList<>();
        if (data == null) {
            return elenco;
        }
        for (Proiezione proiezione : getProiezioni()) {
            if (proiezione.getDataProiezione().equals(data)) {
                elenco.add(proiezione);
            }
        }
        return elenco;
    }

    /**
     * Restituisce le prenotazioni relative a una data.
     *
     * @param data data di filtro.
     * @return lista delle prenotazioni trovate.
     */
    public LinkedList<Prenotazione> getPrenotazioniByDate(Date data) {
        LinkedList<Prenotazione> elenco = new LinkedList<>();
        if (data == null) {
            return elenco;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(PRENOTAZIONI_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Prenotazione p = parsePrenotazioneFromCsv(line);
                if (p != null && p.getProiezione() != null && p.getProiezione().getDataProiezione().equals(data)) {
                    elenco.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento delle prenotazioni per la data: " + e.getMessage());
        }
        return elenco;
    }

    /**
     * Restituisce tutte le prenotazioni presenti nel CSV.
     *
     * @return lista completa delle prenotazioni valide.
     */
    public LinkedList<Prenotazione> getPrenotazioni() {
        LinkedList<Prenotazione> elenco = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRENOTAZIONI_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Prenotazione p = parsePrenotazioneFromCsv(line);
                if (p != null && p.getProiezione() != null) {
                    elenco.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento delle prenotazioni: " + e.getMessage());
        }
        return elenco;
    }

    /**
     * Cerca una prenotazione per ID.
     *
     * @param id identificativo della prenotazione.
     * @return prenotazione trovata, oppure null.
     */
    public Prenotazione getPrenotazioneById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRENOTAZIONI_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int currentId = parsePrenotazioneId(line);
                if (currentId == id) {
                    return parsePrenotazioneFromCsv(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la ricerca della prenotazione per ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cerca prenotazioni per nome, cognome o username cliente.
     *
     * @param cliente testo di ricerca.
     * @return lista delle prenotazioni compatibili.
     */
    public LinkedList<Prenotazione> searchPrenotazioniByCliente(String cliente) {
        LinkedList<Prenotazione> elenco = new LinkedList<>();
        if (cliente == null || cliente.isEmpty()) {
            return elenco;
        }
        String query = cliente.toLowerCase();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRENOTAZIONI_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = splitCsv(line);
                if (fields.length > 3) {
                    String nome = unquote(fields[1]).toLowerCase();
                    String cognome = unquote(fields[2]).toLowerCase();
                    String username = unquote(fields[3]).toLowerCase();
                    if (nome.contains(query) || cognome.contains(query) || username.contains(query)) {
                        Prenotazione p = parsePrenotazioneFromCsv(line);
                        if (p != null) {
                            elenco.add(p);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la ricerca delle prenotazioni per cliente: " + e.getMessage());
        }
        return elenco;
    }

    /**
     * Calcola l'incasso di una giornata.
     *
     * @param data data richiesta.
     * @return totale incassato.
     */
    public double getDailyRevenue(Date data) {
        double totale = 0;
        for (Prenotazione prenotazione : getPrenotazioniByDate(data)) {
            totale += prenotazione.getSpesa();
        }
        return totale;
    }

    /**
     * Calcola l'incasso della settimana contenente la data indicata.
     *
     * @param data data di riferimento.
     * @return totale settimanale.
     */
    public double getWeeklyRevenue(Date data) {
        if (data == null) {
            return 0;
        }
        LocalDate reference = toLocalDate(data);
        LocalDate startOfWeek = reference.minusDays(reference.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        double totale = 0;
        for (Prenotazione prenotazione : getPrenotazioni()) {
            LocalDate dataPrenotazione = toLocalDate(prenotazione.getProiezione().getDataProiezione());
            if (!dataPrenotazione.isBefore(startOfWeek) && !dataPrenotazione.isAfter(endOfWeek)) {
                totale += prenotazione.getSpesa();
            }
        }
        return totale;
    }

    /**
     * Calcola l'incasso del mese contenente la data indicata.
     *
     * @param data data di riferimento.
     * @return totale mensile.
     */
    public double getMonthlyRevenue(Date data) {
        if (data == null) {
            return 0;
        }
        double totale = 0;
        for (Prenotazione prenotazione : getPrenotazioni()) {
            Date dataPrenotazione = prenotazione.getProiezione().getDataProiezione();
            if (dataPrenotazione.getMese() == data.getMese() && dataPrenotazione.getAnno() == data.getAnno()) {
                totale += prenotazione.getSpesa();
            }
        }
        return totale;
    }

    /**
     * Calcola l'incasso dell'anno contenente la data indicata.
     *
     * @param data data di riferimento.
     * @return totale annuo.
     */
    public double getYearlyRevenue(Date data) {
        if (data == null) {
            return 0;
        }
        double totale = 0;
        for (Prenotazione prenotazione : getPrenotazioni()) {
            if (prenotazione.getProiezione().getDataProiezione().getAnno() == data.getAnno()) {
                totale += prenotazione.getSpesa();
            }
        }
        return totale;
    }

    private LocalDate toLocalDate(Date data) {
        return LocalDate.of(data.getAnno(), data.getMese(), data.getGiorno());
    }

    /**
     * Costruisce la mappa di occupazione reale leggendo le prenotazioni salvate.
     *
     * @param proiezione proiezione da analizzare.
     * @return matrice true/false dei posti occupati.
     */
    public boolean[][] getOccupancyMap(Proiezione proiezione) {
        boolean[][] map = new boolean[10][20];
        if (proiezione == null) {
            return map;
        }
        for (Prenotazione prenotazione : getPrenotazioniByProiezione(proiezione)) {
            for (Posto seat : prenotazione.getSeats()) {
                int row = seat.getRowIndex();
                int col = seat.getNumero();
                if (row >= 1 && row <= map.length && col >= 1 && col <= map[0].length) {
                    map[row - 1][col - 1] = true;
                }
            }
        }
        return map;
    }

    /**
     * Aggiunge una proiezione al file CSV controllando le sovrapposizioni.
     *
     * @param proiezione proiezione da aggiungere.
     * @return true se l'inserimento riesce.
     */
    public boolean addProiezione(Proiezione proiezione) {
        if (proiezione == null) {
            System.out.println("Proiezione non valida.");
            return false;
        }
        if (isOverlapping(proiezione)) {
            System.out.println("Non è possibile aggiungere la proiezione: intervallo orario sovrapposto a una proiezione esistente.");
            return false;
        }
        try {
            ensureProiezioniHeader();
            try (FileWriter fw = new FileWriter(PROIEZIONI_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(buildProiezioneLine(proiezione));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della proiezione: " + e.getMessage());
            return false;
        }
    }

    /**
     * Aggiorna una proiezione esistente nel CSV.
     *
     * @param originale proiezione da sostituire.
     * @param aggiornato nuova proiezione.
     * @return true se la modifica riesce.
     */
    public boolean updateProiezione(Proiezione originale, Proiezione aggiornato) {
        if (originale == null || aggiornato == null) {
            System.out.println("Proiezione non valida.");
            return false;
        }
        if (hasPrenotazioni(originale)) {
            System.out.println("Impossibile modificare una proiezione già prenotata.");
            return false;
        }
        if (isOverlappingExcluding(aggiornato, originale)) {
            System.out.println("La modifica non è possibile: l'orario finisce in sovrapposizione con una proiezione esistente.");
            return false;
        }
        File file = new File(PROIEZIONI_FILE);
        File tempFile = new File(PROIEZIONI_FILE + ".tmp");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String header = reader.readLine();
            if (header != null) {
                writer.write(header);
                writer.newLine();
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Proiezione esistente = parseProiezioneFromCsv(line);
                if (esistente != null && sameProiezione(esistente, originale)) {
                    writer.write(buildProiezioneLine(aggiornato));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante l'aggiornamento della proiezione: " + e.getMessage());
            return false;
        }
        if (!file.delete() || !tempFile.renameTo(file)) {
            System.out.println("Impossibile aggiornare il file delle proiezioni dopo la modifica.");
            return false;
        }
        return true;
    }

    /**
     * Rimuove una proiezione dal CSV se possibile.
     *
     * @param proiezione proiezione da rimuovere.
     * @return true se la rimozione riesce.
     */
    public boolean removeProiezione(Proiezione proiezione) {
        if (proiezione == null) {
            System.out.println("Proiezione non valida.");
            return false;
        }
        if (hasPrenotazioni(proiezione)) {
            System.out.println("Impossibile rimuovere la proiezione: è già stata prenotata.");
            return false;
        }
        File file = new File(PROIEZIONI_FILE);
        File tempFile = new File(PROIEZIONI_FILE + ".tmp");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String header = reader.readLine();
            if (header != null) {
                writer.write(header);
                writer.newLine();
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Proiezione esistente = parseProiezioneFromCsv(line);
                if (esistente != null && sameProiezione(esistente, proiezione)) {
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la rimozione della proiezione: " + e.getMessage());
            return false;
        }
        if (!file.delete() || !tempFile.renameTo(file)) {
            System.out.println("Impossibile aggiornare il file delle proiezioni dopo la rimozione.");
            return false;
        }
        return true;
    }

    /**
     * Verifica se una nuova proiezione si sovrappone a quelle gia esistenti.
     *
     * @param nuovaProiezione proiezione da verificare.
     * @return true se esiste una sovrapposizione.
     */
    public boolean isOverlapping(Proiezione nuovaProiezione) {
        return isOverlappingInternal(nuovaProiezione);
    }

    private boolean isOverlappingInternal(Proiezione nuovaProiezione) {
        for (Proiezione esistente : getProiezioni()) {
            if (sameProiezione(esistente, nuovaProiezione)) {
                return true;
            }
            if (esistente.getDataProiezione().equals(nuovaProiezione.getDataProiezione()) && scheduleOverlap(esistente, nuovaProiezione)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlappingExcluding(Proiezione nuovaProiezione, Proiezione esclusa) {
        for (Proiezione esistente : getProiezioni()) {
            if (sameProiezione(esistente, esclusa)) {
                continue;
            }
            if (esistente.getDataProiezione().equals(nuovaProiezione.getDataProiezione()) && scheduleOverlap(esistente, nuovaProiezione)) {
                return true;
            }
        }
        return false;
    }

    private boolean scheduleOverlap(Proiezione p1, Proiezione p2) {
        int start1 = p1.getOraProiezione().toMinutes();
        int end1 = start1 + p1.getFilm().getDurata();
        int start2 = p2.getOraProiezione().toMinutes();
        int end2 = start2 + p2.getFilm().getDurata();
        return start1 < end2 && start2 < end1;
    }

    private boolean sameProiezione(Proiezione a, Proiezione b) {
        if (a == null || b == null) {
            return false;
        }
        return a.getFilm().getTitolo().equalsIgnoreCase(b.getFilm().getTitolo()) &&
               a.getDataProiezione().equals(b.getDataProiezione()) &&
               a.getOraProiezione().equals(b.getOraProiezione());
    }

    private String buildProiezioneLine(Proiezione proiezione) {
        return quote(formatFileDate(proiezione.getDataProiezione()) + " " + proiezione.getOraProiezione()) + "," +
               quote(proiezione.getFilm().getTitolo()) + "," +
               quote(proiezione.getFilm().getGenere()) + "," +
               quote(proiezione.getFilm().getRegista()) + "," +
               proiezione.getFilm().getAnno() + "," +
               proiezione.getFilm().getDurata() + "," +
               proiezione.getFilm().getEtaMinima() + "," +
               proiezione.getPrezzo();
    }

    private String formatFileDate(Date data) {
        return String.format("%04d-%02d-%02d", data.getAnno(), data.getMese(), data.getGiorno());
    }

    private void ensureProiezioniHeader() throws IOException {
        File file = new File(PROIEZIONI_FILE);
        if (!file.exists() || file.length() == 0) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("dataOra,titolo,genere,regista,anno,durata,etaMinima,prezzo");
                bw.newLine();
            }
        }
    }

    private void ensurePrenotazioniHeader() throws IOException {
        File file = new File(PRENOTAZIONI_FILE);
        if (!file.exists() || file.length() == 0) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write("id,nome,cognome,username,film,gd,md,ad,ora,qta,posti");
                bw.newLine();
            }
        }
    }

    private Prenotazione parsePrenotazioneFromCsv(String line) {
        try {
            String[] fields = splitCsv(line);
            if (fields.length < 10) {
                return null;
            }
            String titolo = unquote(fields[4]);
            int giorno = Integer.parseInt(fields[5]);
            int mese = Integer.parseInt(fields[6]);
            int anno = Integer.parseInt(fields[7]);
            String[] oreMin = fields[8].split(":");
            Time ora = new Time(Integer.parseInt(oreMin[0]), Integer.parseInt(oreMin[1]));
            Date data = new Date(giorno, mese, anno);
            Proiezione proiezione = findProiezione(titolo, data, ora);
            LinkedList<Posto> seats = new LinkedList<>();
            if (fields.length > 10) {
                seats = parseSeats(unquote(fields[10]));
            }
            int id = Integer.parseInt(fields[0]);
            double prezzo = 0;
            if (proiezione != null) {
                prezzo = proiezione.getPrezzo() * (fields.length > 9 ? Integer.parseInt(fields[9]) : 0);
            }
            return new Prenotazione(id, proiezione, seats, prezzo);
        } catch (DateFormatException | TimeFormatException | NumberFormatException e) {
            System.out.println("Errore durante il parsing della prenotazione: " + e.getMessage());
            return null;
        }
    }

    private LinkedList<Posto> parseSeats(String seatString) {
        LinkedList<Posto> seats = new LinkedList<>();
        if (seatString == null || seatString.isEmpty()) {
            return seats;
        }
        for (String token : seatString.split(";")) {
            String[] parts = token.split("-");
            if (parts.length == 2) {
                try {
                    String rowPart = parts[0].trim();
                    int col = Integer.parseInt(parts[1].trim());
                    seats.add(new Posto(rowPart, col));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return seats;
    }

    private String encodeSeats(LinkedList<Posto> seats) {
        if (seats == null || seats.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            sb.append(seats.get(i).toString());
            if (i < seats.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    /**
     * Rimuove una prenotazione dal CSV.
     *
     * @param cliente cliente proprietario, se disponibile.
     * @param prenotazione prenotazione da rimuovere.
     */
    public void removePrenotazione(Cliente cliente, Prenotazione prenotazione) {
        File file = new File(PRENOTAZIONI_FILE);
        File tempFile = new File(PRENOTAZIONI_FILE + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String header = reader.readLine();
            if (header != null) {
                writer.write(header);
                writer.newLine();
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int currentId = parsePrenotazioneId(line);
                if (currentId == prenotazione.getId()) {
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la rimozione della prenotazione: " + e.getMessage());
            return;
        }

        if (!file.delete() || !tempFile.renameTo(file)) {
            System.out.println("Impossibile aggiornare il file delle prenotazioni dopo la cancellazione.");
        }
    }

    /**
     * Aggiorna una prenotazione nel CSV.
     *
     * @param cliente cliente proprietario.
     * @param prenotazione prenotazione aggiornata.
     */
    public void updatePrenotazione(Cliente cliente, Prenotazione prenotazione) {
        File file = new File(PRENOTAZIONI_FILE);
        File tempFile = new File(PRENOTAZIONI_FILE + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String header = reader.readLine();
            if (header != null) {
                writer.write(header);
                writer.newLine();
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int currentId = parsePrenotazioneId(line);
                if (currentId == prenotazione.getId()) {
                    writer.write(buildPrenotazioneLine(cliente, prenotazione));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante l'aggiornamento della prenotazione: " + e.getMessage());
            return;
        }

        if (!file.delete() || !tempFile.renameTo(file)) {
            System.out.println("Impossibile aggiornare il file delle prenotazioni dopo la modifica.");
        }
    }

    private int parsePrenotazioneId(String line) {
        String[] fields = splitCsv(line);
        try {
            return Integer.parseInt(fields[0]);
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    private String buildPrenotazioneLine(Cliente cliente, Prenotazione prenotazione) {
        String seatString = encodeSeats(prenotazione.getSeats());
        return prenotazione.getId() + "," +
               quote(cliente.getNome()) + "," +
               quote(cliente.getCognome()) + "," +
               quote(cliente.getUsername()) + "," +
               quote(prenotazione.getProiezione().getFilm().getTitolo()) + "," +
               prenotazione.getProiezione().getDataProiezione().getGiorno() + "," +
               prenotazione.getProiezione().getDataProiezione().getMese() + "," +
               prenotazione.getProiezione().getDataProiezione().getAnno() + "," +
               prenotazione.getProiezione().getOraProiezione() + "," +
               prenotazione.getNumPosti() + "," +
               quote(seatString);
    }

    private String quote(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unquote(String value) {
        if (value == null) {
            return "";
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }

    /**
     * Cerca una proiezione per titolo, data e ora.
     *
     * @param titolo titolo del film.
     * @param data data della proiezione.
     * @param ora ora della proiezione.
     * @return proiezione trovata, oppure null.
     */
    public Proiezione findProiezione(String titolo, Date data, Time ora) {
        LinkedList<Proiezione> elenco = getProiezioni();
        for (Proiezione p : elenco) {
            if (p.getFilm().getTitolo().equalsIgnoreCase(titolo) &&
                    p.getDataProiezione().equals(data) &&
                    p.getOraProiezione().equals(ora)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Metodo segnaposto mantenuto per compatibilita con versioni precedenti.
     *
     * @param utente utente di riferimento.
     * @return lista vuota.
     */
    public LinkedList<Prenotazione> proiezioniByGenre(Utente utente) {
        // Implementazione per ottenere le prenotazioni di un utente
        return new LinkedList<>();
    }

    /**
     * Legge il file proiezioni.csv e restituisce la lista completa delle proiezioni.
     * Ogni riga viene convertita in un oggetto Proiezione contenente tutte le informazioni.
     */
    /**
     * Legge tutte le proiezioni presenti nel file CSV.
     *
     * @return lista completa delle proiezioni valide.
     */
    public LinkedList<Proiezione> getProiezioni() {
        proiezioni.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROIEZIONI_FILE))) {
            reader.readLine(); // intestazione, salta
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Proiezione p = parseProiezioneFromCsv(line);
                if (p != null) {
                    proiezioni.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Impossibile leggere il file delle proiezioni: " + e.getMessage());
        }

        return proiezioni;
    }

    private Proiezione parseProiezioneFromCsv(String line) {
        try {
            String[] field = splitCsv(line);
            if (field.length < 8) {
                return null;
            }

            String dataOra = field[0];
            String titolo = field[1];
            String genere = field[2];
            String regista = field[3];
            int anno = Integer.parseInt(field[4]);
            int durata = Integer.parseInt(field[5]);
            int etaMinima = Integer.parseInt(field[6]);
            double prezzo = Double.parseDouble(field[7]);

            String[] dataOraToSplit = dataOra.split(" ");
            String[] dateParts = dataOraToSplit[0].split("-");
            String[] timeParts = dataOraToSplit[1].split(":");

            Date data = new Date(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
            Time ora = new Time(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
            Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);

            return new Proiezione(film, data, ora, prezzo);
        } catch (DateFormatException | TimeFormatException | NumberFormatException e) {
            System.out.println("Errore durante il parsing della riga delle proiezioni: " + e.getMessage());
            return null;
        }
    }

    private String[] splitCsv(String line) {
        LinkedList<String> field = new LinkedList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                field.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        field.add(current.toString());
        String[] array = new String[field.size()];
        field.toArray(array);
        return array;
    }
}
