package models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class PCRTestDate implements IData<PCRTestDate> {

    private long dataPosition = -1;
    private Date date;
    private String testCode = "";
    private boolean valid = true;

    private static final char emptyChar = '*';
    private static final int KOD_LENGTH = 36;

    public PCRTestDate(Date datum, String kod, long dataPosition) {
        this.dataPosition = dataPosition;
        this.testCode = kod;
        this.date = datum;
    }

    public PCRTestDate(Date datum) {
        this.date = datum;
    }

    public PCRTestDate(Date datum, String kod) {
        this.date = datum;
        this.testCode = kod;
    }

    public PCRTestDate() {
        valid = false;
        date = new Date();
    }

    public long getDataPosition() {
        return dataPosition;
    }

    public String getTestCode() {
        return testCode;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            String kodTestu = this.testCode;
            if (kodTestu.length() <= KOD_LENGTH) {
                StringBuilder stringBuilder = new StringBuilder(kodTestu);
                for (int i = kodTestu.length(); i < KOD_LENGTH; i++)
                    stringBuilder.append(emptyChar);
                kodTestu = stringBuilder.toString();
            } else {
                kodTestu = kodTestu.substring(0, KOD_LENGTH);
            }

            dataOutputStream.writeLong(dataPosition);
            dataOutputStream.writeLong(date.getTime());
            dataOutputStream.writeChars(kodTestu);
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
            dataPosition = dataInputStream.readLong();

            date.setTime(dataInputStream.readLong());

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < KOD_LENGTH; i++)
                stringBuilder.append(dataInputStream.readChar());
            testCode = stringBuilder.toString();

            valid = dataInputStream.readBoolean();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getSize() {
        return 2 * Long.BYTES + 1 + KOD_LENGTH * Character.BYTES;
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
    public PCRTestDate createClass() {
        return new PCRTestDate();
    }

    @Override
    public int compareTo(PCRTestDate o) {
        if (this.date.compareTo(o.date) == 0) {
            return this.testCode.compareTo(o.testCode);
        } else {
            return this.date.compareTo(o.date);
        }
    }

    @Override
    public String toString() {
        return "PCRTestDate{" +
                "dataPosition=" + dataPosition +
                ", datum=" + date +
                ", kod='" + testCode + '\'' +
                ", valid=" + valid +
                '}';
    }
}
