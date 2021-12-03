package models;

import universalTree.TreeKey;

import java.util.Date;

public class PCRTestDate implements Comparable<PCRTestDate>, TreeKey<Date> {

    private final PCRTest data;

    public PCRTestDate(PCRTest data) {
        this.data = data;
    }

    public PCRTest getData() {
        return data;
    }

    @Override
    public int compareTo(PCRTestDate o) {
        if (this.data.datum.compareTo(o.data.datum) == 0) {
            return this.data.kodTestu.compareTo(o.data.kodTestu);
        } else {
            return this.data.datum.compareTo(o.data.datum);
        }
    }

    @Override
    public Date getKey() {
        return this.data.datum;
    }

}
