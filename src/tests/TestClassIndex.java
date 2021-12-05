package tests;

import models.IData;

import java.io.*;

public class TestClassIndex implements IData<TestClassIndex> {

    private long dataPosition;
    private Integer value;
    private boolean valid = true;

    public TestClassIndex(int value, long dataPosition) {
        this.value = value;
        this.dataPosition = dataPosition;
    }

    public TestClassIndex(int value) {
        this.value = value;
    }

    public TestClassIndex() {
        value = -1;
        valid = false;
        dataPosition = -1;
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

    public long getDataPosition() {
        return dataPosition;
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
            dataOutputStream.writeLong(this.dataPosition);
            dataOutputStream.writeInt(this.value);
            dataOutputStream.writeBoolean(this.valid);
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
            dataPosition = dataInputStream.readLong();
            value = dataInputStream.readInt();
            valid = dataInputStream.readBoolean();
        } catch (Exception e) {
            throw new IllegalStateException("FromByteArray");
        }
    }

    @Override
    public int getSize() {
        return Long.BYTES + Integer.BYTES + 1;
    }

    @Override
    public TestClassIndex createClass() {
        return new TestClassIndex();
    }

    @Override
    public int compareTo(TestClassIndex object) {
        return this.value.compareTo(object.value);
    }

    @Override
    public String toString() {
        return "tests.TestClass{" +
                "value=" + value +
                ", dataPosition=" + dataPosition +
                ", valid=" + valid +
                '}';
    }
}
