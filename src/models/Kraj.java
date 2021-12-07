package models;

public class Kraj extends UzemnaJednotka {

    public Kraj(Integer kod, String nazov) {
        super(kod, nazov);
    }

    public Kraj() {

    }

    @Override
    public UzemnaJednotka createClass() {
        return new Kraj();
    }
}
