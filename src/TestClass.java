import java.io.*;

public class TestClass implements IRecord<TestClass> {

    private Integer value;
    private boolean valid = true;

    public TestClass(int value) {
        this.value = value;
    }

    public TestClass() {
        value = -1;
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
        } catch (Exception e) {
            throw new IllegalStateException("FromByteArray");
        }
    }

    @Override
    public int getSize() {
        return Integer.BYTES + 1;
    }

    @Override
    public TestClass createClass() {
        return new TestClass();
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "value=" + value +
                ", valid=" + valid +
                '}';
    }
}
