package models;

import twoThreeTree.TTTree;
import universalTree.TreeKey;

import java.util.Date;
import java.lang.Comparable;

public class Osoba implements Comparable<Osoba>, TreeKey<String> {

    private final String meno;
    private final String priezvisko;
    private final Date datumNarodenia;
    private final String rodCislo;
    private final TTTree<Date, PCRTestDate> testy = new TTTree<>();

    public Osoba(String meno, String priezvisko, Date datumNarodenia, String rodCislo) {
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.datumNarodenia = datumNarodenia;
        this.rodCislo = rodCislo;
    }

    public TTTree<Date, PCRTestDate> getTesty() {
        return testy;
    }

    @Override
    public int compareTo(Osoba o) {
        return rodCislo.compareTo(o.rodCislo);
    }

    @Override
    public String getKey() {
        return this.rodCislo;
    }

    public String getMeno() {
        return meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public Date getDatumNarodenia() {
        return datumNarodenia;
    }

    public String getRodCislo() {
        return rodCislo;
    }
}
