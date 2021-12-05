package models;

import twoThreeTree.TTTree;
import twoThreeTree.TTTreeNode;

import java.util.Date;

public abstract class UzemnaJednotka implements IData<UzemnaJednotka> {

    private Integer kod;
    private String nazov;

    private TTTree<PCRTestDate> testy;
    private TTTree<PCRTestDate> pozitivneTesty;

    UzemnaJednotka(int kod, String nazov) {
        this.kod = kod;
        this.nazov = nazov;
        testy = new TTTree<>(nazov, new TTTreeNode<>(new PCRTestDate()));
        pozitivneTesty = new TTTree<>(nazov + "Positive", new TTTreeNode<>(new PCRTestDate()));
    }

    public UzemnaJednotka() {

    }

    public UzemnaJednotka(int kod) {
        this.kod = kod;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] array) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void setValid(boolean valid) {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public UzemnaJednotka createClass() {
        return null;
    }

    @Override
    public int compareTo(UzemnaJednotka u) {
        return kod.compareTo(u.kod);
    }

    public TTTree<PCRTestDate> getTesty() {
        return testy;
    }

    public TTTree<PCRTestDate> getPozitivneTesty() {
        return this.pozitivneTesty;
    }

    public Integer getKod() {
        return kod;
    }

    public String getNazov() {
        return nazov;
    }
}
