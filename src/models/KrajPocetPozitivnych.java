package models;

public class KrajPocetPozitivnych extends UzemnaJednotka {

    private Integer pocetPozitivnych;

    public KrajPocetPozitivnych(int kod, String nazov) {
        super(kod, nazov);
    }

    public KrajPocetPozitivnych() {

    }

    @Override
    public int compareTo(UzemnaJednotka u) {
        KrajPocetPozitivnych krajPocetPozitivnych = (KrajPocetPozitivnych) u;
        if (pocetPozitivnych.compareTo(krajPocetPozitivnych.pocetPozitivnych) == 0) {
            return this.getKod().compareTo(u.getKod());
        }
        return pocetPozitivnych.compareTo(krajPocetPozitivnych.pocetPozitivnych);
    }

    @Override
    public UzemnaJednotka createClass() {
        return new KrajPocetPozitivnych();
    }

    public Integer getPocetPozitivnych() {
        return pocetPozitivnych;
    }

    public void setPocetPozitivnych(Integer pocetPozitivnych) {
        this.pocetPozitivnych = pocetPozitivnych;
    }

}
