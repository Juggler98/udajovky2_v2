package models;

public class Okres extends UzemnaJednotka {

    private final int kodKraja;

    public Okres(Integer kod, int kodKraja, String nazov) {
        super(kod, nazov);
        this.kodKraja = kodKraja;
    }

    public int getKodKraja() {
        return kodKraja;
    }
}
