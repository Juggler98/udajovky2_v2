package models;

public class Okres extends UzemnaJednotka {

    private int kodKraja;

    public Okres(Integer kod, int kodKraja, String nazov) {
        super(kod, nazov);
        this.kodKraja = kodKraja;
    }

    public Okres() {

    }

    @Override
    public UzemnaJednotka createClass() {
        return new Okres();
    }

    public int getKodKraja() {
        return kodKraja;
    }
}
