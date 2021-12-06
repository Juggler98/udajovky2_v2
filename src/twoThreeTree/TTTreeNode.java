package twoThreeTree;

import models.IData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class TTTreeNode<T extends IData<T>> implements IData {

    private long parent = -1;
    private long leftSon = -1;
    private long middleSon = -1;
    private long rightSon = -1;

    private long myPosition = -1;
    //private long positionsDataL = -1;
    //private long positionDataR = -1;
    private T dataL;
    private T dataR;

    public TTTreeNode(long myPosition, T data) {
        this.dataL = data;
        this.dataR = dataL.createClass();
        //this.positionsDataL = dataPosition;
        this.myPosition = myPosition;
    }

    public TTTreeNode(T data) {
        dataL = data;
        dataR = dataL.createClass();
    }

//    public TTTreeNode() {
//        dataL = dataL.createClass();
//        dataR = dataL.createClass();
//    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeLong(this.parent);
            dataOutputStream.writeLong(this.leftSon);
            dataOutputStream.writeLong(this.middleSon);
            dataOutputStream.writeLong(this.rightSon);
            dataOutputStream.write(dataL.toByteArray());
            dataOutputStream.write(dataR.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void fromByteArray(byte[] array) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        try {
            parent = dataInputStream.readLong();
            leftSon = dataInputStream.readLong();
            middleSon = dataInputStream.readLong();
            rightSon = dataInputStream.readLong();
            byte[] b = new byte[dataL.getSize()];
            dataInputStream.read(b);
            dataL.fromByteArray(b);
            dataInputStream.read(b);
            dataR.fromByteArray(b);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public long getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(long myPosition) {
        this.myPosition = myPosition;
    }

    //public long getPositionsDataL() {
      //  return positionsDataL;
    //}

    //public long getPositionDataR() {
      //  return positionDataR;
    //}

    @Override
    public int getSize() {
        return Long.BYTES * 4 + 2 * dataL.getSize();
    }

    @Override
    public void setValid(boolean valid) {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Object createClass() {
        return new TTTreeNode<>(dataL.createClass());
    }

    @Override
    public int compareTo(Object object) {
        Long m = myPosition;
        TTTreeNode<T> o = (TTTreeNode<T>) object;
        return m.compareTo(o.myPosition);
    }

    public boolean isThreeNode() {
        return hasDataR();
    }

    public void vypis() {
        if (hasMiddleSon()) {
            System.out.println("hasMIDDLESon");
        }
        if (hasRightSon()) {
            System.out.println("hasRIGHTSon");
        }
        if (hasLeftSon()) {
            System.out.println("hasLEFTSon");
        }
        if (isThreeNode()) {
            System.out.println("3 NODE");
            System.out.println("L: " + dataL);
            System.out.println("R: " + dataR);
        } else {
            System.out.println("2 NODE");
            System.out.println("L: " + dataL);
        }
        System.out.println();
        if (dataL == null) {
            System.out.println("-------------Error-------keyL-or-dataL---------");
        }
    }

    public boolean isLeaf() {
        return !hasMiddleSon() && !hasRightSon() && !hasLeftSon();
    }

    public void setMiddleSon(long middleSon) {
        this.middleSon = middleSon;
    }

    public void setDataL(T dataL) {
        this.dataL = dataL;
        //this.positionsDataL = positionsDataL;
    }

    public void setDataR(T dataR) {
        this.dataR = dataR;
    }

    public long getMiddleSon() {
        return middleSon;
    }

    public T getDataL() {
        return dataL;
    }

    public T getDataR() {
        return dataR;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public void setLeftSon(long leftSon) {
        this.leftSon = leftSon;
    }

    public void setRightSon(long rightSon) {
        this.rightSon = rightSon;
    }

    public long getParent() {
        return parent;
    }

    public long getLeftSon() {
        return leftSon;
    }

    public long getRightSon() {
        return rightSon;
    }

    public boolean hasLeftSon() {
        return this.leftSon != -1;
    }

    public boolean hasRightSon() {
        return this.rightSon != -1;
    }

    public boolean hasParent() {return this.parent != -1;}

    public boolean hasMiddleSon() {
        return this.middleSon != -1;
    }

    public boolean hasDataL() {
        return this.dataL.isValid();
    }

    public boolean hasDataR() {
        return this.dataR.isValid();
    }

    @Override
    public String toString() {
        return "TTTreeNode{" +
                "parent=" + parent +
                ", leftSon=" + leftSon +
                ", middleSon=" + middleSon +
                ", rightSon=" + rightSon +
                ", myPosition=" + myPosition +
                ", dataL=" + dataL +
                ", dataR=" + dataR +
                '}';
    }
}
