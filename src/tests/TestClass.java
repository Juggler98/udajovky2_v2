package tests;

import models.IData;

import java.io.*;

public class TestClass implements IData<TestClass> {

    private Integer value;
    private boolean valid = true;
    private String retazec;
    private static final int RETAZEC_LENGTH = 20;

    public TestClass(int value, String retazec) {
        if (retazec.length() > RETAZEC_LENGTH) {
            throw new IllegalStateException("retazec.length() > RETAZEC_LENGTH");
        }
        this.value = value;
        this.retazec = retazec;
        if (this.retazec.length() < RETAZEC_LENGTH) {
            for (int i = this.retazec.length(); i < RETAZEC_LENGTH; i++) {
                this.retazec += "*";
            }
        }
    }

    public TestClass() {
        value = -1;
        retazec = "";
        for (int i = 0; i < RETAZEC_LENGTH; i++) {
            retazec += "*";
        }
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeInt(this.value);
            dataOutputStream.writeBoolean(this.valid);
            dataOutputStream.writeChars(this.retazec);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("ToByteArray");
        }
    }

    @Override
    public void fromByteArray(byte[] array) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            value = dataInputStream.readInt();
            valid = dataInputStream.readBoolean();
            retazec = "";
            for (int i = 0; i < RETAZEC_LENGTH; i++) {
                retazec += dataInputStream.readChar();
            }
        } catch (Exception e) {
            throw new IllegalStateException("FromByteArray");
        }
    }

    @Override
    public int getSize() {
        return Integer.BYTES + 1 + RETAZEC_LENGTH * Character.BYTES;
    }

    @Override
    public TestClass createClass() {
        return new TestClass();
    }

    @Override
    public int compareTo(TestClass object) {
        if (this.value.compareTo(object.value) == 0) {
            return this.retazec.compareTo(object.retazec);
        }
        return this.value.compareTo(object.value);
    }

    @Override
    public String toString() {
        return "tests.TestClass{" +
                "value=" + value +
                " retazec=" + retazec +
                ", valid=" + valid +
                '}';
    }
}
