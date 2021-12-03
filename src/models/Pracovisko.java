package models;

public class Pracovisko extends UzemnaJednotka {

    private final int kodKraja;

    public Pracovisko(int kod, int kodKraja, String nazov) {
        super(kod, nazov);
        this.kodKraja = kodKraja;
    }

    public int getKodKraja() {
        return kodKraja;
    }
}
