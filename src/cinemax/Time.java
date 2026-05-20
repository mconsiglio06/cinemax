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

import java.time.LocalTime;

/**
 * Rappresenta un orario espresso in ore e minuti.
 * Include metodi di conversione in minuti e confronti temporali usati
 * per verificare la validita e la sovrapposizione delle proiezioni.
 */
public class Time {
    private int ore;
    private int minuti;

    /**
     * Restituisce l'orario corrente del sistema.
     *
     * @return orario corrente.
     * @throws TimeFormatException se l'orario corrente non potesse essere convertito.
     */
    public static Time now() throws TimeFormatException {
        LocalTime localTime = LocalTime.now();
        return new Time(localTime.getHour(), localTime.getMinute());
    }

    /**
     * Crea un orario validando ore e minuti.
     *
     * @param ore ora nel formato 0-23.
     * @param minuti minuti nel formato 0-59.
     * @throws TimeFormatException se ore o minuti non sono validi.
     */
    public Time(int ore, int minuti) throws TimeFormatException {
        if (verifyFormat(ore, minuti)) {
            this.ore = ore;
            this.minuti = minuti;
        } else {
            throw new TimeFormatException();
        }
    }

    private boolean verifyFormat(int ore, int minuti) {
        return (ore >= 0 && ore < 24) && (minuti >= 0 && minuti < 60);
    }

    /**
     * Restituisce le ore dell'orario.
     *
     * @return ore dell'orario.
     */
    public int getOre() {
        return ore;
    }

    /**
     * Restituisce i minuti dell'orario.
     *
     * @return minuti dell'orario.
     */
    public int getMinuti() {
        return minuti;
    }

    /**
     * Converte l'orario nel numero di minuti trascorsi da mezzanotte.
     *
     * @return minuti totali da mezzanotte.
     */
    public int toMinutes() {
        return ore * 60 + minuti;
    }

    /**
     * Calcola la distanza in minuti da questo orario a un altro orario.
     *
     * @param altra orario di arrivo.
     * @return differenza in minuti, gestendo anche il cambio giorno.
     */
    public int differenceMinutes(Time altra) {
        int thisMinutes = this.toMinutes();
        int altraMinutes = altra.toMinutes();
        int diff = altraMinutes - thisMinutes;
        if (diff < 0) {
            diff += 24 * 60;
        }
        return diff;
    }

    /**
     * Indica se questo orario e successivo a un altro.
     *
     * @param altra orario da confrontare.
     * @return true se questo orario viene dopo {@code altra}.
     */
    public boolean isAfter(Time altra) {
        return this.differenceMinutes(altra) > 0 && this.differenceMinutes(altra) < 24 * 60;
    }

    /**
     * Indica se questo orario e precedente a un altro.
     *
     * @param altra orario da confrontare.
     * @return true se questo orario viene prima di {@code altra}.
     */
    public boolean isBefore(Time altra) {
        return altra.differenceMinutes(this) > 0 && altra.differenceMinutes(this) < 24 * 60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;
        Time time = (Time) o;
        return ore == time.ore && minuti == time.minuti;
    }

    @Override
    public int hashCode() {
        int result = ore;
        result = 31 * result + minuti;
        return result;
    }

    @Override
    public String toString() {
        String oreStr = (ore < 10 ? "0" : "") + ore;
        String minutiStr = (minuti < 10 ? "0" : "") + minuti;
        return oreStr + ":" + minutiStr;
    }
}
