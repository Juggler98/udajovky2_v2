public interface IData<T> {

    public byte[] toByteArray();
    public void fromByteArray(byte[] array);
    public int getSize();
    public void setValid(boolean valid);
    public boolean isValid();
    public T createClass();
    public int compareTo(T object);

}
