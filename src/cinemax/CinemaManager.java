package cinemax;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class CinemaManager {
    private LinkedList<Utente> utentiRegistrati;
    private Utente utenteCorrente;

    public CinemaManager() {
        this.utentiRegistrati = new LinkedList<>();
        this.utenteCorrente = null;
        // Carichiamo gli utenti (inclusi i 5 bigliettai e 2 proiezionisti) dal file
        caricaUtentiDaFile("utenti.csv");
    }

    private void caricaUtentiDaFile(String nomeFile) {
        try {
            Scanner fileScanner = new Scanner(new File(nomeFile));
            
            // Saltiamo l'intestazione del CSV
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
            }

            while (fileScanner.hasNextLine()) {
                String riga = fileScanner.nextLine();
                // Usiamo uno Scanner sulla singola riga per leggere i campi separati da virgola
                Scanner sc = new Scanner(riga);
                sc.useDelimiter(",");

                String nome = sc.next();
                String cognome = sc.next();
                String username = sc.next();
                String password = sc.next();
                int g = sc.nextInt();
                int m = sc.nextInt();
                int a = sc.nextInt();
                String luogo = sc.next();
                String ruoloStr = sc.next();

                DataFilm dataNascita = new DataFilm(g, m, a);
                
                // Creiamo l'oggetto specifico in base al ruolo letto (Polimorfismo)
                if (ruoloStr.equalsIgnoreCase("BIGLIETTAIO")) {
                    utentiRegistrati.add(new Bigliettaio(nome, cognome, username, password, dataNascita, luogo));
                } else if (ruoloStr.equalsIgnoreCase("PROIEZIONISTA")) {
                    utentiRegistrati.add(new Proiezionista(nome, cognome, username, password, dataNascita, luogo));
                } else if (ruoloStr.equalsIgnoreCase("CLIENTE")) {
                    utentiRegistrati.add(new Cliente(nome, cognome, username, password, dataNascita, luogo));
                }
                
                sc.close();
            }
            fileScanner.close();
            System.out.println("Dati utenti caricati con successo.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Errore: file utenti.csv non trovato.");
        } catch (DateFormatException e) {
            System.err.println("Errore nel formato data di un utente nel file.");
        }
    }

    public boolean login(String username, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                this.utenteCorrente = u;
                return true;
            }
        }
        return false;
    }

    public void registraNuovoCliente(String nome, String cognome, String username, String password, 
                                int g, int m, int a, String luogo) {
        try {
            // 1. Logica in memoria
            DataFilm data = new DataFilm(g, m, a);
            Cliente nuovo = new Cliente(nome, cognome, username, password, data, luogo);
            utentiRegistrati.add(nuovo);

            // 2. Logica su file con BufferedWriter
            // FileWriter con 'true' attiva la modalità append
            FileWriter fw = new FileWriter("data/utenti.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            // Costruiamo la stringa dei dati
            String riga = nome + "," + cognome + "," + username + "," + password + "," + 
                        g + "," + m + "," + a + "," + luogo + ",CLIENTE";
            
            
            bw.write(riga); // Metodo specifico di BufferedWriter per andare a capo
            bw.newLine();
            
            // Chiudiamo il buffer (che chiuderà anche il FileWriter)
            bw.close();
            
            System.out.println("Registrazione completata e salvata su file!");
        } catch (DateFormatException e) {
            System.err.println("Data non valida!");
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura su file.");
        }
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }
}