package models;


public class PCRTestCode implements IData<PCRTestCode>  {

    private long data = -1;
    private String kodTestu = "";
    private boolean valid = true;

    public PCRTestCode(long data, String kodTestu) {
        this.data = data;
        this.kodTestu = kodTestu;
    }

    public PCRTestCode(String kodTestu) {
        this.kodTestu = kodTestu;
    }

    public PCRTestCode() {
    }

    public long getData() {
        return data;
    }

    @Override
    public int compareTo(PCRTestCode o) {
        return kodTestu.compareTo(o.kodTestu);
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] array) {

    }

    @Override
    public int getSize() {
        return 0;
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
                "data=" + data +
                ", kodTestu='" + kodTestu + '\'' +
                ", valid=" + valid +
                '}';
    }
}
