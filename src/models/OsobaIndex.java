//package models;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//
//public class OsobaIndex implements IData<OsobaIndex> {
//
//    private long dataPosition;
//    private String rodCislo;
//    private boolean valid = true;
//    private static final char emptyChar = '*';
//    private static final int RODCISLO_LENGTH = 10;
//
//    public OsobaIndex(String rodCislo, long dataPosition) {
//        this.rodCislo = rodCislo;
//        this.dataPosition = dataPosition;
//    }
//
//    public OsobaIndex(String rodCislo) {
//        this.rodCislo = rodCislo;
//        this.dataPosition = -1;
//    }
//
//    public OsobaIndex() {
//        rodCislo = "";
//        valid = false;
//        dataPosition = -1;
//    }
//
//    @Override
//    public int compareTo(OsobaIndex o) {
//        return rodCislo.compareTo(o.rodCislo);
//    }
//
//    public String getRodCislo() {
//        return rodCislo;
//    }
//
//    public long getDataPosition() {
//        return dataPosition;
//    }
//
//    @Override
//    public byte[] toByteArray() {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
//        try {
//            String rodCislo = this.rodCislo;
//            if (rodCislo.length() <= RODCISLO_LENGTH) {
//                StringBuilder stringBuilder = new StringBuilder(rodCislo);
//                for (int i = rodCislo.length(); i < RODCISLO_LENGTH; i++)
//                    stringBuilder.append(emptyChar);
//                rodCislo = stringBuilder.toString();
//            } else {
//                rodCislo = rodCislo.substring(0, RODCISLO_LENGTH);
//            }
//
//            dataOutputStream.writeLong(this.dataPosition);
//            dataOutputStream.writeChars(rodCislo);
//            dataOutputStream.writeBoolean(valid);
//            return byteArrayOutputStream.toByteArray();
//        } catch (Exception e) {
//            throw new IllegalStateException(e);
//        }
//    }
//
//    @Override
//    public void fromByteArray(byte[] array) {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
//        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
//        try {
//            dataPosition = dataInputStream.readLong();
//
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < RODCISLO_LENGTH; i++) {
//                char c = dataInputStream.readChar();
//                if (c != emptyChar) {
//                    stringBuilder.append(c);
//                }
//            }
//            rodCislo = stringBuilder.toString();
//
//            valid = dataInputStream.readBoolean();
//        } catch (Exception e) {
//            throw new IllegalStateException(e);
//        }
//    }
//
//    @Override
//    public int getSize() {
//        return Long.BYTES + 1 + RODCISLO_LENGTH * Character.BYTES;
//    }
//
//    @Override
//    public void setValid(boolean valid) {
//        this.valid = valid;
//    }
//
//    @Override
//    public boolean isValid() {
//        return this.valid;
//    }
//
//    @Override
//    public OsobaIndex createClass() {
//        return new OsobaIndex();
//    }
//
//    @Override
//    public String toString() {
//        return "OsobaIndex{" +
//                "dataPosition=" + dataPosition +
//                ", rodCislo='" + rodCislo + '\'' +
//                ", valid=" + valid +
//                '}';
//    }
//}
