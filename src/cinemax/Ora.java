package cinemax;

public class Ora {
    private int h;
    private int m;

    public Ora(int h, int m) throws TimeFormatException {
        if (h < 0 || h > 23 || m < 0 || m > 59) {
            throw new TimeFormatException();
        }
        this.h = h;
        this.m = m;
    }
}
