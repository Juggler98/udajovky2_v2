package models;

import twoThreeTree.TTTree;
import universalTree.TreeKey;

import java.util.Date;

public abstract class UzemnaJednotka implements Comparable<UzemnaJednotka>, TreeKey<Integer> {

    private final Integer kod;
    private final String nazov;

    private final TTTree<Date, PCRTestDate> testy = new TTTree<>();
    private final TTTree<Date, PCRTestDate> pozitivneTesty = new TTTree<>();

    UzemnaJednotka(int kod, String nazov) {
        this.kod = kod;
        this.nazov = nazov;
    }

    @Override
    public int compareTo(UzemnaJednotka u) {
        return kod.compareTo(u.kod);
    }

    @Override
    public Integer getKey() {
        return kod;
    }

    public TTTree<Date, PCRTestDate> getTesty() {
        return testy;
    }

    public TTTree<Date, PCRTestDate> getPozitivneTesty() {
        return this.pozitivneTesty;
    }

    public Integer getKod() {
        return kod;
    }

    public String getNazov() {
        return nazov;
    }
}
