package models;

import java.util.Date;

public class PCRTest {

    protected final Date datum;
    private final String rodCisloPacienta;
    protected final String kodTestu;
    private final int kodPracoviska;
    private final int kodOkresu;
    private final int kodKraja;
    private boolean vysledok;
    private String poznamka;
    private final Osoba osoba;

    public PCRTest(String kodTestu, String rodCisloPacienta, int kodPracoviska, int kodOkresu, int kodKraja, boolean vysledok, String poznamka, Osoba osoba, Date datum) {
        if (datum == null) {
            this.datum = new Date(System.currentTimeMillis());
        } else {
            this.datum = datum;
        }
        this.rodCisloPacienta = rodCisloPacienta;
        this.kodTestu = kodTestu;
        this.kodPracoviska = kodPracoviska;
        this.kodOkresu = kodOkresu;
        this.kodKraja = kodKraja;
        this.vysledok = vysledok;
        this.poznamka = poznamka;
        this.osoba = osoba;
        //System.out.println(kodTestu);
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public Date getDatum() {
        return datum;
    }

    public String getRodCisloPacienta() {
        return rodCisloPacienta;
    }

    public String getKodTestu() {
        return kodTestu;
    }

    public int getKodPracoviska() {
        return kodPracoviska;
    }

    public int getKodOkresu() {
        return kodOkresu;
    }

    public int getKodKraja() {
        return kodKraja;
    }

    public boolean isVysledok() {
        return vysledok;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setVysledok(boolean vysledok) {
        this.vysledok = vysledok;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }
}

