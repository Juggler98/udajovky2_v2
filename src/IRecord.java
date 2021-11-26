public interface IRecord {

    public byte[] toByteArray();
    public void fromByteArray(byte[] array);
    public int getSize();
    public void setValid(boolean valid);

}
