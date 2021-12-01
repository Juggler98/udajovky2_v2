import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class DataFile<T extends IRecord> {


    private final RandomAccessFile file;
    private final RandomAccessFile emptyPositions;


    public DataFile() {
        try {
            file = new RandomAccessFile("data.txt", "rw");
            emptyPositions = new RandomAccessFile("emptyPositions.txt", "rw");

            file.setLength(0);
            emptyPositions.setLength(0);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public long write(T data) {
        try {

            //System.out.println("filePointer: " + outputFile.getFilePointer());
            //System.out.println("length: " + outputFile.length() + " pointer: " + outputFile.getFilePointer());

            long writePosition;
            if (emptyPositions.length() > 0) {
                System.out.println("emptyPosition: " + getEmptyPosition());
                writePosition = getEmptyPosition();
            } else {
                writePosition = file.length();
            }

            file.seek(writePosition);
            file.write(data.toByteArray());
            //System.out.println("byte length: " + data.toByteArray().length);
            //file.close();

            //System.out.println("filePointer: " + outputFile.getFilePointer());
            System.out.println("writePosition: " + writePosition);

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
            emptyPositions.seek(emptyPositions.length() - EmptyPosition.SIZE);
            emptyPositions.read(b);
            emptyPosition.fromByteArray(b);

            return emptyPosition.getPosition();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] read(long position, int size) {
        try {

            //System.out.println("filePointer: " + outputFile.getFilePointer());
            //System.out.println("length: " + outputFile.length() + " pointer: " + outputFile.getFilePointer());

            byte[] b = new byte[size];
            file.seek(position);
            file.read(b);

            //file.close();

            return b;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public ArrayList<T> getAllData(T data) {
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

    public ArrayList<EmptyPosition> getAllEmptyPositions(EmptyPosition data) {
        ArrayList<EmptyPosition> list = new ArrayList<>();
        try {
            byte[] b = new byte[data.getSize()];
            emptyPositions.seek(0);
            while (emptyPositions.read(b) != -1) {
                EmptyPosition newData = (EmptyPosition) data.createClass();
                newData.fromByteArray(b);
                list.add(newData);
                emptyPositions.seek(emptyPositions.getFilePointer());
            }
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] delete(long position, T data) {
        try {
            data.setValid(false);
            file.seek(position);
            file.write(data.toByteArray());

            //System.out.println("filePointer" + file.getFilePointer());
            if (file.getFilePointer() == file.length()) {
                file.setLength(file.length() - data.getSize());
            } else {
                EmptyPosition emptyPosition = new EmptyPosition(position);
                emptyPositions.seek(emptyPositions.length());
                emptyPositions.write(emptyPosition.toByteArray());
            }

            //file.close();
            return data.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
