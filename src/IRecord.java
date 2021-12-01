public interface IRecord<T> {

    public byte[] toByteArray();
    public void fromByteArray(byte[] array);
    public int getSize();
    public void setValid(boolean valid);
    public T createClass();

}
