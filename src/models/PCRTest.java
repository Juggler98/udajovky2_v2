package models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class PCRTest implements IData<PCRTest> {

    protected String kodTestu = "";
    private String rodCisloPacienta = "";
    private int kodPracoviska = -1;
    private int kodOkresu = -1;
    private int kodKraja = -1;
    private boolean vysledok = false;
    private String poznamka = "";
    protected Date datum;
    private boolean valid = true;

    private static final char EMPTY_CHAR = '*';
    private static final int KOD_LENGTH = 36;
    private static final int RODCISLO_LENGTH = 10;
    private static final int POZNAMKA_LENGTH = 20;

    public PCRTest(String kodTestu, String rodCisloPacienta, int kodPracoviska, int kodOkresu, int kodKraja, boolean vysledok, String poznamka, Date datum) {
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
        //System.out.println(kodTestu);
    }

    public PCRTest() {
        valid = false;
        datum = new Date();
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {

            String kodTestu = this.kodTestu;
            if (kodTestu.length() <= KOD_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(kodTestu);
                for (int i = kodTestu.length(); i < KOD_LENGTH; i++)
                    stringBuilder.append(EMPTY_CHAR);
                kodTestu = stringBuilder.toString();
            } else {
                kodTestu = kodTestu.substring(0, KOD_LENGTH);
            }

            String rodCislo = this.rodCisloPacienta;
            if (rodCislo.length() <= RODCISLO_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(rodCislo);
                for (int i = rodCislo.length(); i < RODCISLO_LENGTH; i++)
                    stringBuilder.append(EMPTY_CHAR);
                rodCislo = stringBuilder.toString();
            } else {
                rodCislo = rodCislo.substring(0, RODCISLO_LENGTH);
            }

            String poznamka = this.poznamka == null ? "" : this.poznamka;
            if (poznamka.length() <= POZNAMKA_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(poznamka);
                for (int i = poznamka.length(); i < POZNAMKA_LENGTH; i++)
                    stringBuilder.append(EMPTY_CHAR);
                poznamka = stringBuilder.toString();
            } else {
                poznamka = poznamka.substring(0, POZNAMKA_LENGTH);
            }

            dataOutputStream.writeChars(kodTestu);
            dataOutputStream.writeChars(rodCislo);
            dataOutputStream.writeInt(kodPracoviska);
            dataOutputStream.writeInt(kodOkresu);
            dataOutputStream.writeInt(kodKraja);
            dataOutputStream.writeBoolean(vysledok);
            dataOutputStream.writeChars(poznamka);
            dataOutputStream.writeLong(datum.getTime());
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
            for (int i = 0; i < KOD_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != EMPTY_CHAR) {
                    stringBuilder.append(c);
                }
            }
            kodTestu = stringBuilder.toString();

            stringBuilder = new StringBuilder();
            for (int i = 0; i < RODCISLO_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != EMPTY_CHAR) {
                    stringBuilder.append(c);
                }
            }
            rodCisloPacienta = stringBuilder.toString();

            kodPracoviska = dataInputStream.readInt();
            kodOkresu = dataInputStream.readInt();
            kodKraja = dataInputStream.readInt();
            vysledok = dataInputStream.readBoolean();

            stringBuilder = new StringBuilder();
            for (int i = 0; i < POZNAMKA_LENGTH; i++) {
                char c = dataInputStream.readChar();
                if (c != EMPTY_CHAR) {
                    stringBuilder.append(c);
                }
            }
            poznamka = stringBuilder.toString();

            datum.setTime(dataInputStream.readLong());
            valid = dataInputStream.readBoolean();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getSize() {
        return 3 * Integer.BYTES + 2 + Long.BYTES + Character.BYTES * (POZNAMKA_LENGTH + RODCISLO_LENGTH + KOD_LENGTH);
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
    public PCRTest createClass() {
        return new PCRTest();
    }

    @Override
    public int compareTo(PCRTest object) {
        return this.kodTestu.compareTo(object.kodTestu);
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

    @Override
    public String toString() {
        return "PCRTest{" +
                "kodTestu='" + kodTestu + '\'' +
                ", rodCisloPacienta='" + rodCisloPacienta + '\'' +
                ", kodPracoviska=" + kodPracoviska +
                ", kodOkresu=" + kodOkresu +
                ", kodKraja=" + kodKraja +
                ", vysledok=" + vysledok +
                ", poznamka='" + poznamka + '\'' +
                ", datum=" + datum +
                ", valid=" + valid +
                '}';
    }
}

