package models;

public class KrajPocetPozitivnych extends UzemnaJednotka {

    private Integer pocetPozitivnych;

    public KrajPocetPozitivnych(int kod, String nazov) {
        super(kod, nazov);
    }

    @Override
    public int compareTo(UzemnaJednotka u) {
        KrajPocetPozitivnych krajPocetPozitivnych = (KrajPocetPozitivnych) u;
        if (pocetPozitivnych.compareTo(krajPocetPozitivnych.pocetPozitivnych) == 0) {
            return this.getKod().compareTo(u.getKod());
        }
        return pocetPozitivnych.compareTo(krajPocetPozitivnych.pocetPozitivnych);
    }

    public Integer getPocetPozitivnych() {
        return pocetPozitivnych;
    }

    public void setPocetPozitivnych(Integer pocetPozitivnych) {
        this.pocetPozitivnych = pocetPozitivnych;
    }

}
