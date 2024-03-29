package models;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PCRTestCode implements IData<PCRTestCode>  {

    private long dataPosition;
    private String testCode;
    private boolean valid = true;

    private static final char emptyChar = '*';
    private static final int KOD_LENGTH = 36;

    public PCRTestCode(String kodTestu, long dataPosition) {
        this.dataPosition = dataPosition;
        this.testCode = kodTestu;
    }

    public PCRTestCode(String kodTestu) {
        this.testCode = kodTestu;
        this.dataPosition = -1;
    }

    public PCRTestCode() {
        testCode = "";
        valid = false;
        dataPosition = -1;
    }

    public long getDataPosition() {
        return dataPosition;
    }

    @Override
    public int compareTo(PCRTestCode o) {
        return testCode.compareTo(o.testCode);
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
        return Long.BYTES + 1 + KOD_LENGTH * Character.BYTES;
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
    public PCRTestCode createClass() {
        return new PCRTestCode();
    }

    @Override
    public String toString() {
        return "PCRTestCode{" +
                "data=" + dataPosition +
                ", kodTestu='" + testCode + '\'' +
                ", valid=" + valid +
                '}';
    }
}
