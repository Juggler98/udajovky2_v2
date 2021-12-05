package models;

import java.util.Date;

public class PCRTestDate implements IData<PCRTestDate> {

    private long data = -1;
    private Date datum;
    private String kod = "";
    private boolean valid = true;

    public PCRTestDate(long data, Date datum, String kod) {
        this.data = data;
        this.datum = datum;
        this.kod = kod;
    }

    public PCRTestDate(Date datum) {
        this.datum = datum;
    }

    public PCRTestDate() {
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
    public PCRTestDate createClass() {
        return new PCRTestDate();
    }

    public long getData() {
        return data;
    }

    @Override
    public int compareTo(PCRTestDate o) {
        if (this.datum.compareTo(o.datum) == 0) {
            return this.kod.compareTo(o.kod);
        } else {
            return this.datum.compareTo(o.datum);
        }
    }


}
