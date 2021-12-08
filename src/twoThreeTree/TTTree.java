package twoThreeTree;

import data.EmptyPosition;
import models.IData;

import java.io.RandomAccessFile;
import java.util.ArrayList;

public class TTTree<T extends IData<T>> {

    private int size = 0;
    private int height = 0;

    private final RandomAccessFile file;
    private final RandomAccessFile emptyPositions;
    private final TTTreeNode<T> node;
    private final long startAddress = 16;

    ArrayList<TTTreeNode<T>> editedNodes = new ArrayList<>();

    public TTTree(String filename, TTTreeNode<T> node) {
        try {
            file = new RandomAccessFile(filename + ".txt", "rw");
            emptyPositions = new RandomAccessFile(filename + "EmptyPositions.txt", "rw");
            this.clearData(); // TODO: Remove it
            this.node = node;
            if (file.length() == 0) {
                writeInfoData(-1);
            } else {
                file.seek(8);
                size = file.readInt();
                file.seek(12);
                height = file.readInt();
            }
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

    private TTTreeNode<T> getFromAddress(long address) {
        if (address == -1) {
            return null;
        }
        try {
            byte[] b = new byte[node.getSize()];
            file.seek(address);
            file.read(b);
            TTTreeNode<T> node = (TTTreeNode<T>) this.node.createClass();
            node.setMyPosition(address);
            node.fromByteArray(b);
            return node;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public TTTreeNode<T> getRoot() {
        try {
            if (file.length() == 0)
                return null;
            file.seek(0);
            return getFromAddress(file.readLong());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void writeNode(TTTreeNode<T> node) {
        try {
            file.seek(node.getMyPosition());
            file.write(node.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private long getEmptyPosition() {
        try {
            if (emptyPositions.length() == 0) {
                //System.out.println("No empty positions, write to: " + file.length());
                return file.length();
            }
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

            while (getFromAddress(emptyPosition.getPosition()).hasDataL()) {
                emptyPositions.setLength(emptyPositions.length() - emptyPosition.getSize());
                if (emptyPositions.length() > 0) {
                    emptyPositions.seek(emptyPositions.length() - emptyPosition.getSize());
                    emptyPositions.read(b);
                    emptyPosition.fromByteArray(b);
                } else {
                    return file.length();
                }
            }

            //System.out.println("write to Empty position: " + emptyPosition.getPosition());
            return emptyPosition.getPosition();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void inspectEmptyPositions() {
        //System.out.println("---------ADDING-EMPTY-POSITIONS--------");
        ArrayList<Long> addedPosition = new ArrayList<>();
        for (TTTreeNode<T> node : editedNodes) {
            //System.out.println(node);
            if (!node.hasDataL()) {
                if (!addedPosition.contains(node.getMyPosition())) {
                    //System.out.println("new empty position: " + node.getMyPosition());
                    try {
                        EmptyPosition emptyPosition = new EmptyPosition(node.getMyPosition());
                        emptyPositions.seek(emptyPositions.length());
                        emptyPositions.write(emptyPosition.toByteArray());
                        addedPosition.add(emptyPosition.getPosition());
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }

    private void writeInfoData(long rootPosition) {
        try {
            file.seek(0);
            file.writeLong(rootPosition); //root position
            file.seek(8);
            file.writeInt(size);  //size
            file.seek(12);
            file.writeInt(height);  //height
            if (size == 0 && rootPosition == -1) {
                file.setLength(16);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void writeInfoData() {
        try {
            file.seek(8);
            file.writeInt(size);  //size
            file.seek(12);
            file.writeInt(height);  //height
            if (size == 0 && height == 0) {
                file.setLength(16);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void printInfo() {
        try {
            System.out.print("rootPosition, size, height: ");
            file.seek(0);
            System.out.print(file.readLong() + " ");
            file.seek(8);
            System.out.print(file.readInt() + " ");
            file.seek(12);
            System.out.println(file.readInt());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public String getInfo() {
        try {
            String info = "rootPosition, size, height: ";
            file.seek(0);
            info += file.readLong() + " ";
            file.seek(8);
            info += file.readInt() + " ";
            file.seek(12);
            info += file.readInt();
            return info;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public ArrayList<TTTreeNode<T>> getAllData() {
        ArrayList<TTTreeNode<T>> list = new ArrayList<>();
        try {
            byte[] b = new byte[node.getSize()];
            file.seek(16);
            while (file.read(b) != -1) {
                TTTreeNode<T> newNodeData = (TTTreeNode<T>) node.createClass();
                newNodeData.fromByteArray(b);
                newNodeData.setMyPosition(file.getFilePointer() - node.getSize());
                list.add(newNodeData);
                file.seek(file.getFilePointer());
            }
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /*
    Implementovane podla slovneho popisu z prednaskoveho dokumentu.
     */
    public boolean add(T newData) {
        if (!tryToAdd(newData)) {
            System.out.println("Nepodarilo sa vlozit kluc: " + newData); //TODO: Remove it
            return false;
        }
        ++this.size;
        writeInfoData();
        return true;
    }

    private boolean tryToAdd(T newData) {
        TTTreeNode<T> root = getRoot();
        if (root == null) {
            root = new TTTreeNode<>(startAddress, newData);
            ++height;
            writeNode(root);
            writeInfoData(root.getMyPosition());
            return true;
        }

        TTTreeNode<T> leaf = this.findLeaf(newData);

//        System.out.println("leaf: " + leaf);

        if (leaf == null)
            return false;

        TTTreeNode<T> min = null;
        TTTreeNode<T> max = null;
        TTTreeNode<T> middle = null;
        while (true) {
            if (!leaf.isThreeNode()) {
                if (leaf.getDataL().compareTo(newData) < 0) {
                    leaf.setDataR(newData);
                } else {
                    T tempLeftData = leaf.getDataL();
                    leaf.setDataL(newData);
                    leaf.setDataR(tempLeftData);
                }
                writeNode(leaf);
                //leaf.setParent(leafParentMap.get("parent"));
                return true;
            }
            TTTreeNode<T> tempMin = min;
            TTTreeNode<T> tempMax = max;
            if (newData.compareTo(leaf.getDataL()) < 0) {
                min = new TTTreeNode<>(getEmptyPosition(), newData);
                writeNode(min);
                max = new TTTreeNode<>(getEmptyPosition(), leaf.getDataR());
                middle = new TTTreeNode<>(leaf.getMyPosition(), leaf.getDataL());
                //min.setLeftSon(leaf.getLeftSon());
            } else if (newData.compareTo(leaf.getDataR()) > 0) {
                //leaf.setKeyR(node.getKeyL());
                //leaf.setDataR(node.getDataL());
                min = new TTTreeNode<>(leaf.getMyPosition(), leaf.getDataL());
                max = new TTTreeNode<>(getEmptyPosition(), newData);
                writeNode(max);
                middle = new TTTreeNode<>(getEmptyPosition(), leaf.getDataR());
            } else {
                min = new TTTreeNode<>(leaf.getMyPosition(), leaf.getDataL());
                max = new TTTreeNode<>(getEmptyPosition(), leaf.getDataR());
                writeNode(max);
                middle = new TTTreeNode<>(getEmptyPosition(), newData);
            }
            if (tempMin != null && tempMax != null) {
                TTTreeNode<T> a;
                TTTreeNode<T> b;
                if (tempMin.getDataL().compareTo(leaf.getDataL()) < 0) {
                    min.setLeftSon(tempMin.getMyPosition());
                    min.setRightSon(tempMax.getMyPosition());
                    max.setLeftSon(leaf.getMiddleSon());
                    max.setRightSon(leaf.getRightSon());
                    tempMin.setParent(min.getMyPosition());
                    tempMax.setParent(min.getMyPosition());
                    a = getFromAddress(leaf.getMiddleSon());
                    a.setParent(max.getMyPosition());
                    b = getFromAddress(leaf.getRightSon());
                    b.setParent(max.getMyPosition());
                } else if (tempMin.getDataL().compareTo(leaf.getDataR()) > 0) {
                    min.setLeftSon(leaf.getLeftSon());
                    min.setRightSon(leaf.getMiddleSon());
                    max.setLeftSon(tempMin.getMyPosition());
                    max.setRightSon(tempMax.getMyPosition());
                    tempMin.setParent(max.getMyPosition());
                    tempMax.setParent(max.getMyPosition());
                    a = getFromAddress(leaf.getLeftSon());
                    a.setParent(min.getMyPosition());
                    b = getFromAddress(leaf.getMiddleSon());
                    b.setParent(min.getMyPosition());
                } else {
                    min.setLeftSon(leaf.getLeftSon());
                    min.setRightSon(tempMin.getMyPosition());
                    max.setLeftSon(tempMax.getMyPosition());
                    max.setRightSon(leaf.getRightSon());
                    tempMin.setParent(min.getMyPosition());
                    tempMax.setParent(max.getMyPosition());
                    a = getFromAddress(leaf.getLeftSon());
                    a.setParent(min.getMyPosition());
                    b = getFromAddress(leaf.getRightSon());
                    b.setParent(max.getMyPosition());
                }
                writeNode(min);
                writeNode(max);
                writeNode(tempMin);
                writeNode(tempMax);
                writeNode(a);
                writeNode(b);
            }
            if (leaf.hasParent()) {
                TTTreeNode<T> leafParent = getFromAddress(leaf.getParent());
                if (!leafParent.isThreeNode()) {
                    //System.out.println(leafParent.getDataL().getKey());
                    if (leafParent.getDataL().compareTo(middle.getDataL()) < 0) {
                        leafParent.setDataR(middle.getDataL());
                        min.setParent(leafParent.getMyPosition());
                        max.setParent(leafParent.getMyPosition());
                        leafParent.setMiddleSon(min.getMyPosition());
                        leafParent.setRightSon(max.getMyPosition());
                    } else {
                        T tempLeftData = leafParent.getDataL();
                        leafParent.setDataL(middle.getDataL());
                        leafParent.setDataR(tempLeftData);
                        min.setParent(leafParent.getMyPosition());
                        max.setParent(leafParent.getMyPosition());
                        leafParent.setLeftSon(min.getMyPosition());
                        leafParent.setMiddleSon(max.getMyPosition());
                    }
                    writeNode(leafParent);
                    writeNode(min);
                    writeNode(max);
                    return true;
                } else {
                    min.setParent(leafParent.getMyPosition());
                    max.setParent(leafParent.getMyPosition());
                    //leafParent.setLeftSon(min);
                    //leafParent.setRightSon(max);
                    leaf = leafParent;
                    newData = middle.getDataL();
                    writeNode(min);
                    writeNode(max);
                }
            } else {
                //TTTreeNode<K, T> newRoot = middle;
                middle.setLeftSon(min.getMyPosition());
                middle.setRightSon(max.getMyPosition());
                min.setParent(middle.getMyPosition());
                max.setParent(middle.getMyPosition());

                writeNode(min);
                writeNode(max);
                writeNode(middle);
                root = middle;
                ++height;
                writeInfoData(root.getMyPosition());
                return true;
            }
        }
    }

    /*
    Intervale vyhladavanie.
    Implementovane podla vlastneho navrhu.
     */
    public ArrayList<T> getIntervalData(T start, T end) {
        TTTreeNode<T> leaf = getRoot();
        if (leaf == null) {
            return getInOrderDataInterval(leaf, start, end, true);
        }
        while (true) {
            if (leaf.isThreeNode()) {
                if (start.compareTo(leaf.getDataL()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = getFromAddress(leaf.getLeftSon());
                    else
                        break;
                } else if (start.compareTo(leaf.getDataR()) > 0) {
                    if (leaf.hasRightSon())
                        leaf = getFromAddress(leaf.getRightSon());
                    else
                        break;
                } else if (start.compareTo(leaf.getDataR()) == 0 || start.compareTo(leaf.getDataL()) == 0) {
                    break;
                } else {
                    if (leaf.hasMiddleSon())
                        leaf = getFromAddress(leaf.getMiddleSon());
                    else
                        break;
                }
            } else {
                if (start.compareTo(leaf.getDataL()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = getFromAddress(leaf.getLeftSon());
                    else
                        break;
                } else if (start.compareTo(leaf.getDataL()) == 0) {
                    break;
                } else {
                    if (leaf.hasRightSon())
                        leaf = getFromAddress(leaf.getRightSon());
                    else
                        break;
                }
            }
        }
        boolean left = true;
        while (leaf != null) {
            if (leaf.getDataL().compareTo(start) >= 0 && leaf.getDataL().compareTo(end) <= 0) {
                left = true;
                break;
            }
            if (leaf.isThreeNode()) {
                if (leaf.getDataR().compareTo(start) >= 0 && leaf.getDataR().compareTo(end) <= 0) {
                    left = false;
                    break;
                }
            }
            leaf = getFromAddress(leaf.getParent());
        }
        return getInOrderDataInterval(leaf, start, end, left);
    }

    private ArrayList<T> getInOrderDataInterval(TTTreeNode<T> node, T start, T end, boolean left) {
        ArrayList<T> data = new ArrayList<>();
        if (node == null) {
            return data;
        }
        TTTreeNode<T> current = node;
        T actualData;

        if (!current.isLeaf()) {
            if (left) {
                actualData = current.getDataL();
            } else {
                actualData = current.getDataR();
            }
            data.add(actualData);
            if (left && current.isThreeNode()) {
                current = getFromAddress(current.getMiddleSon());
            } else {
                current = getFromAddress(current.getRightSon());
            }
        }
        boolean isParent;
        while (current != null) {
            while (current.hasLeftSon()) {
                current = getFromAddress(current.getLeftSon());
            }
            actualData = current.getDataL();
            if (actualData.compareTo(end) > 0) {
                break;
            }
            if (actualData.compareTo(start) >= 0) {
                data.add(actualData);
            }
            if (current.isThreeNode()) {
                actualData = current.getDataR();
                if (actualData.compareTo(end) > 0) {
                    break;
                }
                if (actualData.compareTo(start) >= 0) {
                    data.add(actualData);
                }
            }
            current = getFromAddress(current.getParent());
            isParent = true;
            while (isParent && current != null) {
                if (current.isThreeNode()) {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        if (actualData.compareTo(end) > 0) {
                            current = null;
                            break;
                        }
                        data.add(actualData);
                        current = getFromAddress(current.getMiddleSon());
                        isParent = false;
                    } else if (actualData.compareTo(current.getDataR()) > 0) {
                        current = getFromAddress(current.getParent());
                        isParent = true;
                    } else {
                        actualData = current.getDataR();
                        if (actualData.compareTo(end) > 0) {
                            current = null;
                            break;
                        }
                        data.add(actualData);
                        current = getFromAddress(current.getRightSon());
                        isParent = false;
                    }
                } else {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        if (actualData.compareTo(end) > 0) {
                            current = null;
                            break;
                        }
                        data.add(actualData);
                        current = getFromAddress(current.getRightSon());
                        isParent = false;
                    } else {
                        current = getFromAddress(current.getParent());
                        isParent = true;
                    }
                }
            }
        }
        return data;
    }

    /*
    Inorder bez rekurzie.
    Implementovane podla vlastneho navrhu.
     */
    public ArrayList<T> getInOrderData() {
        TTTreeNode<T> current = getRoot();
        ArrayList<T> data = new ArrayList<>();
        if (current == null) {
            return data;
        }
        T actualData;
        boolean isParent;
        while (current != null) {
            while (current.hasLeftSon()) {
                current = getFromAddress(current.getLeftSon());
            }
            actualData = current.getDataL();
            data.add(actualData);
            if (current.isThreeNode()) {
                actualData = current.getDataR();
                data.add(actualData);
            }
            current = getFromAddress(current.getParent());
            isParent = true;
            while (isParent && current != null) {
                if (current.isThreeNode()) {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        data.add(actualData);
                        current = getFromAddress(current.getMiddleSon());
                        isParent = false;
                    } else if (actualData.compareTo(current.getDataR()) > 0) {
                        current = getFromAddress(current.getParent());
                        isParent = true;
                    } else {
                        actualData = current.getDataR();
                        data.add(actualData);
                        current = getFromAddress(current.getRightSon());
                        isParent = false;
                    }
                } else {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        data.add(actualData);
                        current = getFromAddress(current.getRightSon());
                        isParent = false;
                    } else {
                        current = getFromAddress(current.getParent());
                        isParent = true;
                    }
                }
            }
        }
        return data;
    }

    /*
    Len pre testove ucely.
     */
    public void inOrderRecursive(TTTreeNode<T> node) {
        if (node == null)
            return;
        inOrderRecursive(getFromAddress(node.getLeftSon()));
        node.vypis();
        inOrderRecursive(getFromAddress(node.getMiddleSon()));
        inOrderRecursive(getFromAddress(node.getRightSon()));
    }

    /*
    Len pre testove ucely.
     */
    public void preorder(TTTreeNode<T> node) {
        if (node == null)
            return;
        node.vypis();
        preorder(getFromAddress(node.getLeftSon()));
        preorder(getFromAddress(node.getMiddleSon()));
        preorder(getFromAddress(node.getRightSon()));
    }

    /*
    Len pre testove ucely. Vypis hlbky kazdeho listu pomocou rekurzie.
    Implementacia inspirovana podla toho, co som pocul na cviceni.
     */
    public void deepOfLeaf(TTTreeNode<T> node) {
        if (node == null) {
            //System.out.println("null");
            return;
        }
        if (node.isLeaf()) {
            TTTreeNode<T> leaf = node;
            int deep = 1;
            while (true) {
                if (leaf.hasParent()) {
                    leaf = getFromAddress(leaf.getParent());
                    deep++;
                } else {
                    break;
                }
            }
            System.out.println("Leaf deep: " + deep);
        }
        deepOfLeaf(getFromAddress(node.getLeftSon()));
        deepOfLeaf(getFromAddress(node.getMiddleSon()));
        deepOfLeaf(getFromAddress(node.getRightSon()));
    }

    private TTTreeNode<T> findLeaf(T data) {
        TTTreeNode<T> leaf = getRoot();
        while (true) {
            if (leaf.isThreeNode()) {
                if (data.compareTo(leaf.getDataL()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = getFromAddress(leaf.getLeftSon());
                    else
                        break;
                } else if (data.compareTo(leaf.getDataR()) > 0) {
                    if (leaf.hasRightSon())
                        leaf = getFromAddress(leaf.getRightSon());
                    else
                        break;
                } else if (data.compareTo(leaf.getDataR()) == 0 || data.compareTo(leaf.getDataL()) == 0) {
                    break;
                } else {
                    if (leaf.hasMiddleSon())
                        leaf = getFromAddress(leaf.getMiddleSon());
                    else
                        break;
                }
            } else {
                if (data.compareTo(leaf.getDataL()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = getFromAddress(leaf.getLeftSon());
                    else
                        break;
                } else if (data.compareTo(leaf.getDataL()) == 0) {
                    break;
                } else {
                    if (leaf.hasRightSon())
                        leaf = getFromAddress(leaf.getRightSon());
                    else
                        break;
                }
            }
        }
        if (leaf.isThreeNode()) {
            if (data.compareTo(leaf.getDataL()) != 0 && data.compareTo(leaf.getDataR()) != 0) {
                return leaf;
            }
        } else {
            if (data.compareTo(leaf.getDataL()) != 0) {
                return leaf;
            }
        }
        return null;
    }

    /*
    Implementovane podla slovneho popisu z prednaskoveho dokumentu.
     */
    public T search(T data) {
        TTTreeNode<T> result = searchNode(data);
        if (result == null) {
            return null;
        }
        if (data.compareTo(result.getDataL()) == 0) {
            return result.getDataL();
        } else if (result.isThreeNode() && data.compareTo(result.getDataR()) == 0) {
            return result.getDataR();
        }
        return null;
    }

    private TTTreeNode<T> searchNode(T data) {
        TTTreeNode<T> result = getRoot();
        if (result == null) {
            return null;
        }
        while (data.compareTo(result.getDataL()) != 0) {
            if (result.hasDataR() && data.compareTo(result.getDataR()) == 0) {
                break;
            }
            if (result.isThreeNode()) {
                if (data.compareTo(result.getDataL()) < 0) {
                    if (result.hasLeftSon())
                        result = getFromAddress(result.getLeftSon());
                    else
                        break;
                } else if (data.compareTo(result.getDataR()) > 0) {
                    if (result.hasRightSon())
                        result = getFromAddress(result.getRightSon());
                    else
                        break;
                } else {
                    if (result.hasMiddleSon())
                        result = getFromAddress(result.getMiddleSon());
                    else
                        break;
                }
            } else {
                if (data.compareTo(result.getDataL()) < 0) {
                    if (result.hasLeftSon())
                        result = getFromAddress(result.getLeftSon());
                    else
                        break;
                } else {
                    if (result.hasRightSon())
                        result = getFromAddress(result.getRightSon());
                    else
                        break;
                }
            }
        }
        if (result.isThreeNode()) {
            return (data.compareTo(result.getDataL()) == 0 || data.compareTo(result.getDataR()) == 0) ? result : null;
        }
        return data.compareTo(result.getDataL()) == 0 ? result : null;
    }

    /*
        Implementovane podla slovneho popisu z prednaskoveho dokumentu.
    */
    public T remove(T data) {
        TTTreeNode<T> node = searchNode(data);
        if (node == null) {
            System.out.println("Mazanie, prvok neexistuje: " + data);
            return null;
        }
        T deletedData;
        boolean left;
        if (data.compareTo(node.getDataL()) == 0) {
            deletedData = node.getDataL();
            left = true;
        } else {
            deletedData = node.getDataR();
            left = false;
        }
        editedNodes.clear();
        if (!tryToRemove(node, left)) {
            System.out.println("Mazanie sa nepodarilo, kluc: " + deletedData);
            return null;
        }
        inspectEmptyPositions();
        size--;
        writeInfoData();
        return deletedData;
    }

    private boolean tryToRemove(TTTreeNode<T> node, boolean leftToDelete) {
        //System.out.println("---------------tryToRemove------------------");
        //System.out.println("key to delete: " + (leftToDelete ? node.getDataL().getKey() : node.getDataR().getKey()));
        //System.out.println("root: " + root.getDataL().getKey());
        //System.out.println("root left son: " + root.getLeftSon().getKeyL());
        TTTreeNode<T> root = getRoot();
        if (node.isLeaf()) {
            if (!node.isThreeNode()) {
                if (node.compareTo(root) == 0) {
                    root.setDataL(root.getDataL().createClass());
                    editedNodes.add(root);
                    height--;
                    writeInfoData(-1);
                    return true;
                } else {
                    node.setDataL(node.getDataL().createClass());
                    editedNodes.add(node);
                    writeNode(node);
                }
            } else {
                if (leftToDelete) {
                    node.setDataL(node.getDataR());
                }
                node.setDataR(node.getDataL().createClass());
                editedNodes.add(node);
                writeNode(node);
                return true;
            }
        }
        TTTreeNode<T> inOrderLeaf = node;
        if (!node.isLeaf()) {
            inOrderLeaf = findInOrderLeaf(node, leftToDelete);
            //System.out.println("replacing: " + inOrderLeaf.getDataL().getKey());
        }

        //if (inOrderLeaf.hasDataL())
        //inOrderLeaf.vypis();

        //System.out.println("inOrderLeaf Parent leftSon keyL" + inOrderLeaf.getParent().getLeftSon().getDataL().getKey());
        //System.out.println("inOrderLeaf Parent key" + inOrderLeaf.getParent().getDataL().getKey());

        if (!node.isLeaf()) {
            if (leftToDelete) {
                if (node.isThreeNode()) {
                    if (node.getDataR().compareTo(inOrderLeaf.getDataL()) > 0) {
                        node.setDataL(inOrderLeaf.getDataL());
                    } else {
                        T tempLeftData = node.getDataL();
                        node.setDataL(inOrderLeaf.getDataL());
                        node.setDataR(tempLeftData);
                    }
                } else {
                    //System.out.println("yes");
                    node.setDataL(inOrderLeaf.getDataL());
                }
            } else {
                if (node.getDataL().compareTo(inOrderLeaf.getDataL()) < 0) {
                    node.setDataR(inOrderLeaf.getDataL());
                } else {
                    T tempLeftData = node.getDataR();
                    node.setDataR(inOrderLeaf.getDataL());
                    node.setDataL(tempLeftData);
                }
            }
            editedNodes.add(node);
            writeNode(node);
            if (inOrderLeaf.isThreeNode()) {
                inOrderLeaf.setDataL(inOrderLeaf.getDataR());
                inOrderLeaf.setDataR(inOrderLeaf.getDataL().createClass());
            } else {
                inOrderLeaf.setDataL(inOrderLeaf.getDataL().createClass());
            }
            editedNodes.add(inOrderLeaf);
            writeNode(inOrderLeaf);
        }

        //System.out.println("node == inOrderLeaf: " + (node == inOrderLeaf));


        while (true) {
            //System.out.println("while");
            if (inOrderLeaf.hasDataL()) {
                return true;
            }

            if (inOrderLeaf.getMyPosition() == root.getMyPosition()) {
                //System.out.println("I am root");
                if (inOrderLeaf.hasLeftSon() && getFromAddress(inOrderLeaf.getLeftSon()).hasDataL()) {
                    root = getFromAddress(inOrderLeaf.getLeftSon());
                } else if (inOrderLeaf.hasMiddleSon() && getFromAddress(inOrderLeaf.getMiddleSon()).hasDataL()) {
                    root = getFromAddress(inOrderLeaf.getMiddleSon());
                } else if (inOrderLeaf.hasRightSon() && getFromAddress(inOrderLeaf.getRightSon()).hasDataL()) {
                    root = getFromAddress(inOrderLeaf.getRightSon());
                } else {
                    root = null;
                }
                if (root != null) {
                    root.setParent(-1);
                }
                if (root == null) {
                    writeInfoData(-1);
                } else {
                    editedNodes.add(root);
                    writeNode(root);
                    writeInfoData(root.getMyPosition());
                }
                height--;
                return true;
            }

            TTTreeSonType inOrderLeafSonType = getSonType(inOrderLeaf);
            TTTreeNode<T> parent = getFromAddress(inOrderLeaf.getParent());
            //System.out.println(parent.getLeftSon().getKeyL());
            //System.out.println(inOrderLeafSonType);
            if (inOrderLeafSonType == TTTreeSonType.LEFT) {
                //System.out.println("SonType.LEFT");
                TTTreeNode<T> a;
                TTTreeNode<T> b = null;
                TTTreeNode<T> c = null;
                a = getFromAddress(parent.getMiddleSon());
                if ((parent.isThreeNode() && a.isThreeNode()) || (!parent.isThreeNode() && getFromAddress(parent.getRightSon()).isThreeNode())) {
                    if (parent.isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(a.getDataL());
                        a.setDataL(a.getDataR());
                        a.setDataR(a.getDataL().createClass());
                        if (!inOrderLeaf.isLeaf() && !a.isLeaf()) {
                            if (!getFromAddress(inOrderLeaf.getLeftSon()).hasDataL()) {
                                inOrderLeaf.setLeftSon(inOrderLeaf.getRightSon());
                            }
                            inOrderLeaf.setRightSon(a.getLeftSon());
                            a.setLeftSon(a.getMiddleSon());
                            a.setMiddleSon(-1);
                            b = getFromAddress(inOrderLeaf.getRightSon());
                            b.setParent(inOrderLeaf.getMyPosition());
                        }
                    } else {
                        inOrderLeaf.setDataL(parent.getDataL());
                        a = getFromAddress(parent.getRightSon());
                        parent.setDataL(a.getDataL());
                        a.setDataL(a.getDataR());
                        a.setDataR(a.getDataL().createClass());
                        if (!inOrderLeaf.isLeaf() && !a.isLeaf()) {
                            if (!getFromAddress(inOrderLeaf.getLeftSon()).hasDataL()) {
                                inOrderLeaf.setLeftSon(inOrderLeaf.getRightSon());
                            }
                            inOrderLeaf.setRightSon(a.getLeftSon());
                            a.setLeftSon(a.getMiddleSon());
                            a.setMiddleSon(-1);
                            b = getFromAddress(inOrderLeaf.getRightSon());
                            b.setParent(inOrderLeaf.getMyPosition());
                        }
                    }
                    editedNodes.add(inOrderLeaf);
                    editedNodes.add(a);
                    writeNode(inOrderLeaf);
                    writeNode(a);
                    if (b != null) {
                        editedNodes.add(b);
                        writeNode(b);
                    }
                    editedNodes.add(parent);
                    writeNode(parent);
                    return true;
                } else {
                    if (parent.isThreeNode()) {
                        a = getFromAddress(parent.getMiddleSon());
                        b = getFromAddress(parent.getLeftSon());
                        T tempLeftData = a.getDataL();
                        a.setDataL(parent.getDataL());
                        a.setDataR(tempLeftData);

                        a.setMiddleSon(a.getLeftSon());

                        if (!b.isLeaf()) {
                            if (getFromAddress(b.getRightSon()).hasDataL()) {
                                //System.out.println("0");
                                a.setLeftSon(b.getRightSon());
                                c = getFromAddress(b.getRightSon());
                                c.setParent(parent.getMiddleSon());
                            } else {
                                //System.out.println("1");
                                a.setLeftSon(b.getLeftSon());
                                c = getFromAddress(b.getLeftSon());
                                c.setParent(parent.getMiddleSon());
                            }
                        }

                        //parent.getMiddleSon().setLeftSon(parent.getLeftSon().getLeftSon());
                        parent.setLeftSon(parent.getMiddleSon());
                        parent.setMiddleSon(-1);

                        parent.setDataL(parent.getDataR());
                        parent.setDataR(parent.getDataL().createClass());
                    } else {
                        //System.out.println("here");
                        a = getFromAddress(parent.getRightSon());

                        T tempLeftData = a.getDataL();

                        a.setDataL(parent.getDataL());
                        a.setDataR(tempLeftData);
                        parent.setDataL(parent.getDataL().createClass());

                        a.setMiddleSon(a.getLeftSon());
                        b = getFromAddress(inOrderLeaf.getLeftSon());
                        c = getFromAddress(inOrderLeaf.getRightSon());
                        if (inOrderLeaf.hasLeftSon() && b.hasDataL()) {
                            a.setLeftSon(inOrderLeaf.getLeftSon());
                            b.setParent(parent.getRightSon());
                        } else if (inOrderLeaf.hasRightSon() && c.hasDataL()) {
                            a.setLeftSon(inOrderLeaf.getRightSon());
                            c.setParent(parent.getRightSon());
                        }
                    }
                    //parent.setLeftSon(null);
                }
                editedNodes.add(a);
                writeNode(a);
                if (b != null) {
                    editedNodes.add(b);
                    writeNode(b);
                }
                if (c != null) {
                    editedNodes.add(c);
                    writeNode(c);
                }
                editedNodes.add(parent);
                writeNode(parent);
            } else if (inOrderLeafSonType == TTTreeSonType.RIGHT) {
                //System.out.println("SonType.RIGHT");
                TTTreeNode<T> a;
                TTTreeNode<T> b = null;
                TTTreeNode<T> c = null;
                a = getFromAddress(parent.getMiddleSon());
                b = getFromAddress(parent.getLeftSon());
                c = getFromAddress(inOrderLeaf.getLeftSon());
                if ((parent.isThreeNode() && a.isThreeNode()) || (!parent.isThreeNode() && b.isThreeNode())) {
                    //System.out.println("brother is ThreeNode");
                    if (parent.isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataR());
                        parent.setDataR(a.getDataR());
                        a.setDataR(a.getDataL().createClass());
                        if (!inOrderLeaf.isLeaf() && !a.isLeaf()) {
                            if (!getFromAddress(inOrderLeaf.getRightSon()).hasDataL()) {
                                inOrderLeaf.setRightSon(inOrderLeaf.getLeftSon());
                            }
                            inOrderLeaf.setLeftSon(a.getRightSon());
                            a.setRightSon(a.getMiddleSon());
                            a.setMiddleSon(-1);
                            c = getFromAddress(inOrderLeaf.getLeftSon());
                            c.setParent(inOrderLeaf.getMyPosition());
                        }
                    } else {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(b.getDataR());
                        if (!inOrderLeaf.isLeaf() && !b.isLeaf()) {
                            if (!getFromAddress(inOrderLeaf.getRightSon()).hasDataL()) {
                                inOrderLeaf.setRightSon(inOrderLeaf.getLeftSon());
                            }
                            inOrderLeaf.setLeftSon(b.getRightSon());
                            b.setRightSon(b.getMiddleSon());
                            b.setMiddleSon(-1);
                            c = getFromAddress(inOrderLeaf.getLeftSon());
                            c.setParent(inOrderLeaf.getMyPosition());
                        }
                        b.setDataR(b.getDataL().createClass());
                    }
                    if (a != null) {
                        editedNodes.add(a);
                        writeNode(a);
                    }
                    editedNodes.add(b);
                    writeNode(b);
                    if (c != null) {
                        editedNodes.add(c);
                        writeNode(c);
                    }
                    editedNodes.add(inOrderLeaf);
                    editedNodes.add(parent);
                    writeNode(inOrderLeaf);
                    writeNode(parent);
                    return true;
                } else {
                    if (parent.isThreeNode()) {
                        //System.out.println("here");
                        a.setDataR(parent.getDataR());

                        a.setMiddleSon(a.getRightSon());

                        b = getFromAddress(parent.getRightSon());
                        if (!b.isLeaf()) {
                            c = getFromAddress(b.getRightSon());
                            if (c.hasDataL()) {
                                a.setRightSon(b.getRightSon());
                                c.setParent(a.getMyPosition());
                            } else {
                                a.setRightSon(b.getLeftSon());
                                c = getFromAddress(b.getLeftSon());
                                c.setParent(a.getMyPosition());
                            }
                        }

                        parent.setRightSon(a.getMyPosition());

//                        System.out.println(parent.getMiddleSon().getKeyL());
//                        System.out.println(parent.getKeyL());
//                        System.out.println(parent.getRightSon().getKeyL());

                        parent.setMiddleSon(-1);

                        parent.setDataR(parent.getDataL().createClass());
                        editedNodes.add(a);
                        editedNodes.add(b);
                        writeNode(a);
                        writeNode(b);
                        if (c != null) {
                            editedNodes.add(c);
                            writeNode(c);
                        }
                        editedNodes.add(parent);
                        writeNode(parent);
                    } else {
                        //System.out.println("right son, parent 2 vrchol");
                        b.setDataR(parent.getDataL());
                        parent.setDataL(parent.getDataL().createClass());

                        b.setMiddleSon(b.getRightSon());
                        // System.out.println(inOrderLeaf.hasRightSon());
                        // System.out.println(inOrderLeaf.hasLeftSon());
                        a = getFromAddress(inOrderLeaf.getRightSon());
                        if (inOrderLeaf.hasLeftSon() && c.hasDataL()) {
                            //System.out.println("test");
                            b.setRightSon(inOrderLeaf.getLeftSon());
                            c.setParent(b.getMyPosition());
                        } else if (inOrderLeaf.hasRightSon() && a.hasDataL()) {
                            //System.out.println("I should be here");
                            b.setRightSon(inOrderLeaf.getRightSon());
                            //System.out.println(inOrderLeaf.getRightSon());
                            a.setParent(b.getMyPosition());
                        }
                        if (a != null) {
                            editedNodes.add(a);
                            writeNode(a);
                        }
                        editedNodes.add(b);
                        writeNode(b);
                        if (c != null) {
                            editedNodes.add(c);
                            writeNode(c);
                        }
                        editedNodes.add(parent);
                        writeNode(parent);
                    }
                    //parent.setRightSon(null);
                }
            } else {
                TTTreeNode<T> a;
                TTTreeNode<T> b = null;
                TTTreeNode<T> c = null;
                TTTreeNode<T> d = null;
                a = getFromAddress(parent.getLeftSon());
                b = getFromAddress(parent.getRightSon());
                c = getFromAddress(inOrderLeaf.getLeftSon());
                d = getFromAddress(inOrderLeaf.getRightSon());
                if (a.isThreeNode() || b.isThreeNode()) {
                    if (a.isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(a.getDataR());
                        a.setDataR(a.getDataL().createClass());
                        if (!inOrderLeaf.isLeaf() && !a.isLeaf()) {
                            if (!d.hasDataL()) {
                                inOrderLeaf.setRightSon(inOrderLeaf.getLeftSon());
                            }
                            inOrderLeaf.setLeftSon(a.getRightSon());
                            a.setRightSon(a.getMiddleSon());
                            a.setMiddleSon(-1);
                            c = getFromAddress(inOrderLeaf.getLeftSon());
                            c.setParent(inOrderLeaf.getMyPosition());
                        }
                        editedNodes.add(a);
                        writeNode(a);
                        if (c != null) {
                            editedNodes.add(c);
                            writeNode(c);
                        }
                        editedNodes.add(parent);
                        editedNodes.add(inOrderLeaf);
                        writeNode(parent);
                        writeNode(inOrderLeaf);
                        return true;
                    } else if (b.isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataR());
                        parent.setDataR(b.getDataL());
                        b.setDataL(b.getDataR());
                        b.setDataR(b.getDataL().createClass());
                        if (!inOrderLeaf.isLeaf() && !b.isLeaf()) {
                            if (!c.hasDataL()) {
                                inOrderLeaf.setLeftSon(inOrderLeaf.getRightSon());
                            }
                            inOrderLeaf.setRightSon(b.getLeftSon());
                            b.setLeftSon(b.getMiddleSon());
                            b.setMiddleSon(-1);
                            d = getFromAddress(inOrderLeaf.getRightSon());
                            d.setParent(inOrderLeaf.getMyPosition());
                        }
                        editedNodes.add(b);
                        writeNode(b);
                        if (d != null) {
                            editedNodes.add(d);
                            writeNode(d);
                        }
                        editedNodes.add(parent);
                        editedNodes.add(inOrderLeaf);
                        writeNode(parent);
                        writeNode(inOrderLeaf);
                        return true;
                    }
                } else {
                    if (!parent.isThreeNode()) {
                        System.out.println("Error: !parent.isThreeNode()");
                    }
                    a.setDataR(parent.getDataL());
                    parent.setDataL(parent.getDataR());

                    a.setMiddleSon(a.getRightSon());
                    b = getFromAddress(parent.getMiddleSon());
                    c = getFromAddress(b.getRightSon());
                    if (!b.isLeaf()) {
                        if (c.hasDataL()) {
                            a.setRightSon(b.getRightSon());
                            c.setParent(parent.getLeftSon());
                        } else {
                            c = getFromAddress(b.getLeftSon());
                            a.setRightSon(b.getLeftSon());
                            c.setParent(parent.getLeftSon());
                        }
                    }

                    parent.setMiddleSon(-1);

                    parent.setDataR(parent.getDataL().createClass());
                    editedNodes.add(a);
                    editedNodes.add(b);
                    writeNode(a);
                    writeNode(b);
                    if (c != null) {
                        editedNodes.add(c);
                        writeNode(c);
                    }
                    editedNodes.add(parent);
                    writeNode(parent);
                }
            }

            inOrderLeaf = getFromAddress(inOrderLeaf.getParent());
        }
        //return false;
    }

    private TTTreeSonType getSonType(TTTreeNode<T> node) {
        if (node != null) {
            TTTreeNode<T> parent = getFromAddress(node.getParent());
            if (parent != null) {
                if (parent.getRightSon() != -1 && parent.getRightSon() == node.getMyPosition()) {
                    return TTTreeSonType.RIGHT;
                }
                if (parent.getMiddleSon() != -1 && parent.getMiddleSon() == node.getMyPosition()) {
                    return TTTreeSonType.MIDDLE;
                }
                if (parent.getLeftSon() != -1 && parent.getLeftSon() == node.getMyPosition()) {
                    return TTTreeSonType.LEFT;
                }
            }
        }
        return null;
    }

    private TTTreeNode<T> findInOrderLeaf(TTTreeNode<T> node, boolean leftToDelete) {
        TTTreeNode<T> result = null;
        if (leftToDelete) {
            if (node.isThreeNode()) {
                if (node.hasMiddleSon()) {
                    result = getFromAddress(node.getMiddleSon());
                } else if (node.hasRightSon()) {
                    result = getFromAddress(node.getRightSon());
                    System.out.println("Error in findInOrderLeaf, this should not happened.");
                }
            } else {
                if (node.hasRightSon()) {
                    result = getFromAddress(node.getRightSon());
                }
            }
        } else {
            if (node.hasRightSon()) {
                result = getFromAddress(node.getRightSon());
            }
        }
        if (result != null) {
            while (true) {
                if (result.hasLeftSon()) {
                    result = getFromAddress(result.getLeftSon());
                } else {
                    break;
                }
            }
            return result;
        }
        System.out.println("Error in findInOrderLeaf, this should not happened.");
        return null;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }
}
