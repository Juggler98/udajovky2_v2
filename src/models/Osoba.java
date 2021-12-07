package models;

import twoThreeTree.TTTree;
import twoThreeTree.TTTreeNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class Osoba implements IData<Osoba> {

    private String meno = "";
    private String priezvisko = "";
    private final Date datumNarodenia;
    private String rodCislo = "";
    private TTTree<PCRTestDate> testy;
    private long myPosition;
    private boolean valid = true;
    private final char emptyChar = '*';
    private static final int MENO_LENGTH = 15;
    private static final int PRIEZVISKO_LENGTH = 20;
    private static final int RODCISLO_LENGTH = 10;

    public Osoba(String meno, String priezvisko, Date datumNarodenia, String rodCislo) {
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.rodCislo = rodCislo;
        this.datumNarodenia = datumNarodenia;
        testy = new TTTree<>(rodCislo, new TTTreeNode<>(new PCRTestDate()));
    }

    public Osoba() {
        meno = "";
        priezvisko = "";
        rodCislo = "";
        valid = false;
        datumNarodenia = new Date();
    }

    public long getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(long myPosition) {
        this.myPosition = myPosition;
    }

    public TTTree<PCRTestDate> getTesty() {
        return testy;
    }

    @Override
    public int compareTo(Osoba o) {
        return rodCislo.compareTo(o.rodCislo);
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

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            String meno = this.meno;
            if (meno.length() <= MENO_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(meno);
                for (int i = meno.length(); i < MENO_LENGTH; i++)
                    stringBuilder.append(emptyChar);
                meno = stringBuilder.toString();
            } else {
                meno = meno.substring(0, MENO_LENGTH);
            }
            String priezvisko = this.priezvisko;
            if (priezvisko.length() <= PRIEZVISKO_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(priezvisko);
                for (int i = priezvisko.length(); i < PRIEZVISKO_LENGTH; i++)
                    stringBuilder.append(emptyChar);
                priezvisko = stringBuilder.toString();
            } else {
                priezvisko = priezvisko.substring(0, PRIEZVISKO_LENGTH);
            }

            String rodCislo = this.rodCislo;
            if (rodCislo.length() <= RODCISLO_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(rodCislo);
                for (int i = rodCislo.length(); i < RODCISLO_LENGTH; i++)
                    stringBuilder.append(emptyChar);
                rodCislo = stringBuilder.toString();
            } else {
                rodCislo = rodCislo.substring(0, RODCISLO_LENGTH);
            }

            dataOutputStream.writeChars(meno);
            dataOutputStream.writeChars(priezvisko);
            dataOutputStream.writeChars(rodCislo);
            dataOutputStream.writeLong(datumNarodenia.getTime());
            dataOutputStream.writeBoolean(valid);
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
            for (int i = 0; i < MENO_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != emptyChar) {
                    stringBuilder.append(c);
                }
            }
            meno = stringBuilder.toString();

            stringBuilder = new StringBuilder();
            for (int i = 0; i < PRIEZVISKO_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != emptyChar) {
                    stringBuilder.append(c);
                }
            }
            priezvisko = stringBuilder.toString();

            stringBuilder = new StringBuilder();
            for (int i = 0; i < RODCISLO_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != emptyChar) {
                    stringBuilder.append(c);
                }
            }
            rodCislo = stringBuilder.toString();

            datumNarodenia.setTime(dataInputStream.readLong());
            valid = dataInputStream.readBoolean();

            if (valid) {
                testy = new TTTree<>(rodCislo, new TTTreeNode<>(new PCRTestDate()));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getSize() {
        return Long.BYTES + 1 + MENO_LENGTH * Character.BYTES + PRIEZVISKO_LENGTH * Character.BYTES + RODCISLO_LENGTH * Character.BYTES;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public Osoba createClass() {
        return new Osoba();
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "meno='" + meno + '\'' +
                ", priezvisko='" + priezvisko + '\'' +
                ", datumNarodenia=" + datumNarodenia +
                ", rodCislo='" + rodCislo + '\'' +
                ", testy=" + testy +
                ", valid=" + valid +
                '}';
    }
}
