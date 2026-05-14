package cinemax;

import java.time.LocalTime;

public class Time {
    private int ore;
    private int minuti;

    public static Time now() throws TimeFormatException {
        LocalTime localTime = LocalTime.now();
        return new Time(localTime.getHour(), localTime.getMinute());
    }

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

    public int getOre() {
        return ore;
    }

    public int getMinuti() {
        return minuti;
    }

    public int toMinutes() {
        return ore * 60 + minuti;
    }

    public int differenceMinutes(Time altra) {
        int thisMinutes = this.toMinutes();
        int altraMinutes = altra.toMinutes();
        int diff = altraMinutes - thisMinutes;
        if (diff < 0) {
            diff += 24 * 60;
        }
        return diff;
    }

    public boolean isAfter(Time altra) {
        return this.differenceMinutes(altra) > 0 && this.differenceMinutes(altra) < 24 * 60;
    }

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
