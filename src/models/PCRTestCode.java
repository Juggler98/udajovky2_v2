package models;

import universalTree.TreeKey;

public class PCRTestCode implements Comparable<PCRTestCode>, TreeKey<String>  {

    private final PCRTest data;

    public PCRTestCode(PCRTest data) {
        this.data = data;
    }

    public PCRTest getData() {
        return data;
    }

    @Override
    public int compareTo(PCRTestCode o) {
        return data.kodTestu.compareTo(o.data.kodTestu);
    }

    @Override
    public String getKey() {
        return this.data.kodTestu;
    }

}
