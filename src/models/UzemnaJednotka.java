package models;

import BOrderThreeTree.BOTTree;
import BOrderThreeTree.BOTTreeNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class UzemnaJednotka implements IData<UzemnaJednotka> {

    private Integer kod;
    private String nazov;
    private boolean valid = true;

    private static final char EMPTY_CHAR = '*';
    private static final int NAME_LENGTH = 64;

    private BOTTree<PCRTestDate> testy;
    private BOTTree<PCRTestDate> pozitivneTesty;

    UzemnaJednotka(Integer kod, String nazov) {
        this.kod = kod;
        this.nazov = nazov;
        testy = new BOTTree<>(nazov, new BOTTreeNode<>(new PCRTestDate()));
        pozitivneTesty = new BOTTree<>(nazov + "Positive", new BOTTreeNode<>(new PCRTestDate()));
    }

    public UzemnaJednotka() {
        nazov = "";
        kod = -1;
        valid = false;
    }

    public UzemnaJednotka(int kod) {
        this.kod = kod;
        this.nazov = "";
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            String nazov = this.nazov;
            if (nazov.length() < NAME_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(nazov);
                for (int i = nazov.length(); i < NAME_LENGTH; i++)
                    stringBuilder.append(EMPTY_CHAR);
                nazov = stringBuilder.toString();
            } else if (nazov.length() > NAME_LENGTH) {
                nazov = nazov.substring(0, NAME_LENGTH);
            }

            dataOutputStream.writeChars(nazov);
            dataOutputStream.writeInt(kod);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void fromByteArray(byte[] array) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < NAME_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != EMPTY_CHAR) {
                    stringBuilder.append(c);
                }
            }
            this.nazov = stringBuilder.toString();

            kod = dataInputStream.readInt();

            if (kod == -1) {
                valid = false;
            } else {
                valid = true;
            }

            if (valid) {
                testy = new BOTTree<>(nazov, new BOTTreeNode<>(new PCRTestDate()));
                pozitivneTesty = new BOTTree<>(nazov + "Positive", new BOTTreeNode<>(new PCRTestDate()));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getSize() {
        return Integer.BYTES + NAME_LENGTH * Character.BYTES;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public UzemnaJednotka createClass() {
        System.out.println("null");
        return null;
    }

    @Override
    public int compareTo(UzemnaJednotka u) {
        return kod.compareTo(u.kod);
    }

    public BOTTree<PCRTestDate> getTesty() {
        return testy;
    }

    public BOTTree<PCRTestDate> getPozitivneTesty() {
        return this.pozitivneTesty;
    }

    public Integer getKod() {
        return kod;
    }

    public String getNazov() {
        return nazov;
    }

    @Override
    public String toString() {
        return "UzemnaJednotka{" +
                "kod=" + kod +
                ", nazov='" + nazov + '\'' +
                ", testy=" + testy +
                ", pozitivneTesty=" + pozitivneTesty +
                '}';
    }
}
