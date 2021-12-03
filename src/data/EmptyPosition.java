package data;

import models.IData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class EmptyPosition implements IData<EmptyPosition> {

    private long position;
    public static final int SIZE = Long.BYTES;

    public EmptyPosition(long position) {
        this.position = position;
    }

    public EmptyPosition() {
        position = -1;
    }

    @Override
    public void setValid(boolean valid) {
        throw new IllegalStateException("Should not call this!");
    }

    @Override
    public boolean isValid() {
        throw new IllegalStateException("Should not call this!");
    }

    public long getPosition() {
        return position;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeLong(this.position);
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
            position = dataInputStream.readLong();
        } catch (Exception e) {
            throw new IllegalStateException("FromByteArray");
        }
    }

    @Override
    public int getSize() {
        return Long.BYTES;
    }

    @Override
    public EmptyPosition createClass() {
        return new EmptyPosition();
    }

    @Override
    public int compareTo(EmptyPosition object) {
        Long position = this.position;
        return position.compareTo(object.position);
    }

    @Override
    public String toString() {
        return "data.EmptyPosition{" +
                "position=" + position +
                '}';
    }
}
