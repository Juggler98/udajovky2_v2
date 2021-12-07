package models;

public class OkresPocetPozitivnych extends UzemnaJednotka {

    private Integer pocetPozitivnych;

    public OkresPocetPozitivnych(int kod, String nazov) {
        super(kod, nazov);
    }

    public OkresPocetPozitivnych() {

    }

    @Override
    public int compareTo(UzemnaJednotka u) {
        OkresPocetPozitivnych okresPocetPozitivnych = (OkresPocetPozitivnych) u;
        if (pocetPozitivnych.compareTo(okresPocetPozitivnych.pocetPozitivnych) == 0) {
            return this.getKod().compareTo(u.getKod());
        }
        return pocetPozitivnych.compareTo(okresPocetPozitivnych.pocetPozitivnych);
    }

    @Override
    public UzemnaJednotka createClass() {
        return new OkresPocetPozitivnych();
    }

    public Integer getPocetPozitivnych() {
        return pocetPozitivnych;
    }

    public void setPocetPozitivnych(Integer pocetPozitivnych) {
        this.pocetPozitivnych = pocetPozitivnych;
    }

    @Override
    public String toString() {
        return "OkresPocetPozitivnych{" +
                "pocetPozitivnych=" + pocetPozitivnych +
                '}';
    }
}
