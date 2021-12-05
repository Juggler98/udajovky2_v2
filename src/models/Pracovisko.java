package models;

public class Pracovisko extends UzemnaJednotka {

    private int kodKraja;

    public Pracovisko(int kod, int kodKraja, String nazov) {
        super(kod, nazov);
        this.kodKraja = kodKraja;
    }

    public Pracovisko() {

    }

    public Pracovisko(int kod) {
        super(kod);
    }

    public int getKodKraja() {
        return kodKraja;
    }
}
