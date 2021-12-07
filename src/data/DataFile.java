package data;

import models.IData;

import java.io.RandomAccessFile;
import java.util.ArrayList;

public class DataFile<T extends IData<T>> {

    private final RandomAccessFile file;
    private final RandomAccessFile emptyPositions;
    private final T data;

    public DataFile(String filename, T data) {
        try {
            file = new RandomAccessFile(filename + ".txt", "rw");
            emptyPositions = new RandomAccessFile(filename + "EmptyPositions.txt", "rw");
            //clearData(); // TODO: REMOVE IT
            this.data = data;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void clearData() {
        try {
            file.setLength(0);
            emptyPositions.setLength(0);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public long write(T data) {
        try {
            long writePosition;
            if (emptyPositions.length() > 0) {
                writePosition = getEmptyPosition();
                //System.out.println("emptyPosition: " + writePosition);
            } else {
                writePosition = file.length();
                //System.out.println("newPosition: " + writePosition);
            }
            file.seek(writePosition);
            file.write(data.toByteArray());
            if (emptyPositions.length() > 0)
                emptyPositions.setLength(emptyPositions.length() - EmptyPosition.SIZE);
            return writePosition;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public long getEmptyPosition() {
        try {
            EmptyPosition emptyPosition = new EmptyPosition();
            byte[] b = new byte[emptyPosition.getSize()];
            emptyPositions.seek(emptyPositions.length() - emptyPosition.getSize());
            emptyPositions.read(b);
            emptyPosition.fromByteArray(b);

            while (emptyPosition.getPosition() > file.length()) {
                emptyPositions.setLength(emptyPositions.length() - emptyPosition.getSize());
                if (emptyPositions.length() > 0) {
                    emptyPositions.seek(emptyPositions.length() - emptyPosition.getSize());
                    emptyPositions.read(b);
                    emptyPosition.fromByteArray(b);
                } else {
                    return file.length();
                }
            }

            return emptyPosition.getPosition();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] read(long position) {
        int size = this.data.getSize();
        try {
            byte[] b = new byte[size];
            file.seek(position);
            file.read(b);
            return b;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public ArrayList<T> getAllData() {
        ArrayList<T> list = new ArrayList<>();
        try {
            byte[] b = new byte[data.getSize()];
            file.seek(0);
            while (file.read(b) != -1) {
                T newData = (T) data.createClass();
                newData.fromByteArray(b);
                list.add(newData);
                file.seek(file.getFilePointer());
            }
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public ArrayList<EmptyPosition> getAllEmptyPositions() {
        EmptyPosition data = new EmptyPosition();
        ArrayList<EmptyPosition> list = new ArrayList<>();
        try {
            byte[] b = new byte[data.getSize()];
            emptyPositions.seek(0);
            while (emptyPositions.read(b) != -1) {
                EmptyPosition newData = new EmptyPosition();
                newData.fromByteArray(b);
                list.add(newData);
                emptyPositions.seek(emptyPositions.getFilePointer());
            }
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] delete(long position) {
        T data = this.data.createClass();
        try {
            data.setValid(false);
            file.seek(position);
            file.write(data.toByteArray());
            if (file.getFilePointer() == file.length()) {
                file.setLength(file.length() - data.getSize());

                while (file.length() > 0) {
                    T tempData = (T) data.createClass();
                    tempData.fromByteArray(this.read(file.length() - data.getSize()));
                    if (!tempData.isValid()) {
                        file.setLength(file.length() - data.getSize());
                    } else {
                        break;
                    }
                }
            } else {
                EmptyPosition emptyPosition = new EmptyPosition(position);
                emptyPositions.seek(emptyPositions.length());
                emptyPositions.write(emptyPosition.toByteArray());
            }
            return data.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
