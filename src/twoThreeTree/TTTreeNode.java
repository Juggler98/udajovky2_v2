package twoThreeTree;


import universalTree.TreeKey;
import universalTree.TreeNode;

public class TTTreeNode<K extends Comparable<K>, T extends Comparable<T> & TreeKey<K>> implements TreeNode {

    private TTTreeNode<K, T> parent;
    private TTTreeNode<K, T> leftSon;
    private TTTreeNode<K, T> middleSon;
    private TTTreeNode<K, T> rightSon;

    private T dataL;
    private T dataR;

    TTTreeNode(T data) {
        this.dataL = data;
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
            System.out.println("L: " + dataL.getKey()); //TODO: I should use here toString() and don't extends TreeKey<> !!!
            System.out.println("R: " + dataR.getKey());
        } else {
            System.out.println("2 NODE");
            System.out.println("L: " + dataL.getKey());
        }
        System.out.println();
        if (dataL.getKey() == null || dataL == null) {
            System.out.println("-------------Error-------keyL-or-dataL---------");
        }
    }

    public boolean isLeaf() {
        return !hasMiddleSon() && !hasRightSon() && !hasLeftSon();
    }

    public void setMiddleSon(TTTreeNode<K, T> middleSon) {
        this.middleSon = middleSon;
    }

    public void setDataL(T dataL) {
        this.dataL = dataL;
    }

    public void setDataR(T dataR) {
        this.dataR = dataR;
    }

    public TTTreeNode<K, T> getMiddleSon() {
        return middleSon;
    }

    public T getDataL() {
        return dataL;
    }

    public T getDataR() {
        return dataR;
    }

    public void setParent(TTTreeNode<K, T> parent) {
        this.parent = parent;
    }

    public void setLeftSon(TTTreeNode<K, T> leftSon) {
        this.leftSon = leftSon;
    }

    public void setRightSon(TTTreeNode<K, T> rightSon) {
        this.rightSon = rightSon;
    }

    public TTTreeNode<K, T> getParent() {
        return parent;
    }

    public TTTreeNode<K, T> getLeftSon() {
        return leftSon;
    }

    public TTTreeNode<K, T> getRightSon() {
        return rightSon;
    }

    public boolean hasLeftSon() {
        return this.leftSon != null;
    }

    public boolean hasRightSon() {
        return this.rightSon != null;
    }

    public boolean hasParent() {return this.parent != null;}

    public boolean hasMiddleSon() {
        return this.middleSon != null;
    }

    public boolean hasDataL() {
        return this.dataL != null;
    }

    public boolean hasDataR() {
        return this.dataR != null;
    }

}
