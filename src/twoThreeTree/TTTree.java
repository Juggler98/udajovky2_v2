package twoThreeTree;

import universalTree.Tree;
import universalTree.TreeKey;

import java.util.ArrayList;

public class TTTree<K extends Comparable<K>, T extends Comparable<T> & TreeKey<K>> extends Tree<K, T> {

    //private TTTreeNode<K, T> root;
    //private int size = 0;
    //private ArrayList<T> data = new ArrayList<>();

    private int height = 0;

    public TTTree() {

    }

    /*
    Implementovane podla slovneho popisu z prednaskoveho dokumentu.
     */
    @Override
    public boolean add(T newData) {
        if (!tryToAdd(newData)) {
            System.out.println("Nepodarilo sa vlozit kluc: " + newData.getKey()); //TODO: Remove it
            return false;
        }
        ++this.size;
        return true;
    }

    private boolean tryToAdd(T newData) {
        if (this.root == null) {
            this.root = new TTTreeNode<>(newData);
            ++height;
            return true;
        }

        TTTreeNode<K, T> leaf = this.findLeaf(newData);

        if (leaf == null)
            return false;

        TTTreeNode<K, T> min = null;
        TTTreeNode<K, T> max = null;
        TTTreeNode<K, T> middle = null;
        while (true) {
            if (!leaf.isThreeNode()) {
                if (leaf.getDataL().compareTo(newData) < 0) {
                    leaf.setDataR(newData);
                } else {
                    T tempLeftData = leaf.getDataL();
                    leaf.setDataL(newData);
                    leaf.setDataR(tempLeftData);
                }
                //leaf.setParent(leafParentMap.get("parent"));
                return true;
            }
            TTTreeNode<K, T> tempMin = min;
            TTTreeNode<K, T> tempMax = max;
            if (newData.compareTo(leaf.getDataL()) < 0) {
                min = new TTTreeNode<>(newData);
                max = new TTTreeNode<>(leaf.getDataR());
                middle = new TTTreeNode<>(leaf.getDataL());
                //min.setLeftSon(leaf.getLeftSon());
            } else if (newData.compareTo(leaf.getDataR()) > 0) {
                //leaf.setKeyR(node.getKeyL());
                //leaf.setDataR(node.getDataL());
                min = new TTTreeNode<>(leaf.getDataL());
                max = new TTTreeNode<>(newData);
                middle = new TTTreeNode<>(leaf.getDataR());
            } else {
                min = new TTTreeNode<>(leaf.getDataL());
                max = new TTTreeNode<>(leaf.getDataR());
                middle = new TTTreeNode<>(newData);
            }
            if (tempMin != null && tempMax != null) {
                if (tempMin.getDataL().compareTo(leaf.getDataL()) < 0) {
                    min.setLeftSon(tempMin);
                    min.setRightSon(tempMax);
                    max.setLeftSon(leaf.getMiddleSon());
                    max.setRightSon(leaf.getRightSon());
                    tempMin.setParent(min);
                    tempMax.setParent(min);
                    leaf.getMiddleSon().setParent(max);
                    leaf.getRightSon().setParent(max);
                } else if (tempMin.getDataL().compareTo(leaf.getDataR()) > 0) {
                    min.setLeftSon(leaf.getLeftSon());
                    min.setRightSon(leaf.getMiddleSon());
                    max.setLeftSon(tempMin);
                    max.setRightSon(tempMax);
                    tempMin.setParent(max);
                    tempMax.setParent(max);
                    leaf.getLeftSon().setParent(min);
                    leaf.getMiddleSon().setParent(min);
                } else {
                    min.setLeftSon(leaf.getLeftSon());
                    min.setRightSon(tempMin);
                    max.setLeftSon(tempMax);
                    max.setRightSon(leaf.getRightSon());
                    tempMin.setParent(min);
                    tempMax.setParent(max);
                    leaf.getLeftSon().setParent(min);
                    leaf.getRightSon().setParent(max);
                }
            }
            if (leaf.hasParent()) {
                TTTreeNode<K, T> leafParent = leaf.getParent();
                if (!leafParent.isThreeNode()) {
                    //System.out.println(leafParent.getDataL().getKey());
                    if (leafParent.getDataL().compareTo(middle.getDataL()) < 0) {
                        leafParent.setDataR(middle.getDataL());
                        min.setParent(leafParent);
                        max.setParent(leafParent);
                        leafParent.setMiddleSon(min);
                        leafParent.setRightSon(max);
                    } else {
                        T tempLeftData = leafParent.getDataL();
                        leafParent.setDataL(middle.getDataL());
                        leafParent.setDataR(tempLeftData);
                        min.setParent(leafParent);
                        max.setParent(leafParent);
                        leafParent.setLeftSon(min);
                        leafParent.setMiddleSon(max);
                    }
                    return true;
                } else {
                    min.setParent(leafParent);
                    max.setParent(leafParent);
                    //leafParent.setLeftSon(min);
                    //leafParent.setRightSon(max);
                    leaf = leafParent;
                    newData = middle.getDataL();
                }
            } else {
                //TTTreeNode<K, T> newRoot = middle;
                middle.setLeftSon(min);
                middle.setRightSon(max);
                min.setParent(middle);
                max.setParent(middle);
                this.root = middle;
                ++height;
                return true;
            }
        }
    }

    /*
    Intervale vyhladavanie.
    Implementovane podla vlastneho navrhu.
     */
    public ArrayList<T> getIntervalData(K start, K end) {
        TTTreeNode<K, T> leaf = (TTTreeNode<K, T>) this.root;
        if (leaf == null) {
            return getInOrderDataInterval(leaf, start, end, true);
        }
        while (true) {
            if (leaf.isThreeNode()) {
                if (start.compareTo(leaf.getDataL().getKey()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = leaf.getLeftSon();
                    else
                        break;
                } else if (start.compareTo(leaf.getDataR().getKey()) > 0) {
                    if (leaf.hasRightSon())
                        leaf = leaf.getRightSon();
                    else
                        break;
                } else if (start.compareTo(leaf.getDataR().getKey()) == 0 || start.compareTo(leaf.getDataL().getKey()) == 0) {
                    break;
                } else {
                    if (leaf.hasMiddleSon())
                        leaf = leaf.getMiddleSon();
                    else
                        break;
                }
            } else {
                if (start.compareTo(leaf.getDataL().getKey()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = leaf.getLeftSon();
                    else
                        break;
                } else if (start.compareTo(leaf.getDataL().getKey()) == 0) {
                    break;
                } else {
                    if (leaf.hasRightSon())
                        leaf = leaf.getRightSon();
                    else
                        break;
                }
            }
        }
        boolean left = true;
        while (leaf != null) {
            if (leaf.getDataL().getKey().compareTo(start) >= 0 && leaf.getDataL().getKey().compareTo(end) <= 0) {
                left = true;
                break;
            }
            if (leaf.isThreeNode()) {
                if (leaf.getDataR().getKey().compareTo(start) >= 0 && leaf.getDataR().getKey().compareTo(end) <= 0) {
                    left = false;
                    break;
                }
            }
            leaf = leaf.getParent();
        }
        return getInOrderDataInterval(leaf, start, end, left);
    }

    private ArrayList<T> getInOrderDataInterval(TTTreeNode<K, T> node, K start, K end, boolean left) {
        ArrayList<T> data = new ArrayList<>();
        if (node == null) {
            return data;
        }
        TTTreeNode<K, T> current = node;
        T actualData;

        if (!current.isLeaf()) {
            if (left) {
                actualData = current.getDataL();
            } else {
                actualData = current.getDataR();
            }
            data.add(actualData);
            if (left && current.isThreeNode()) {
                current = current.getMiddleSon();
            } else {
                current = current.getRightSon();
            }
        }
        boolean isParent;
        while (current != null) {
            while (current.hasLeftSon()) {
                current = current.getLeftSon();
            }
            actualData = current.getDataL();
            if (actualData.getKey().compareTo(end) > 0) {
                break;
            }
            if (actualData.getKey().compareTo(start) >= 0) {
                data.add(actualData);
            }
            if (current.isThreeNode()) {
                actualData = current.getDataR();
                if (actualData.getKey().compareTo(end) > 0) {
                    break;
                }
                if (actualData.getKey().compareTo(start) >= 0) {
                    data.add(actualData);
                }
            }
            current = current.getParent();
            isParent = true;
            while (isParent && current != null) {
                if (current.isThreeNode()) {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        if (actualData.getKey().compareTo(end) > 0) {
                            current = null;
                            break;
                        }
                        data.add(actualData);
                        current = current.getMiddleSon();
                        isParent = false;
                    } else if (actualData.compareTo(current.getDataR()) > 0) {
                        current = current.getParent();
                        isParent = true;
                    } else {
                        actualData = current.getDataR();
                        if (actualData.getKey().compareTo(end) > 0) {
                            current = null;
                            break;
                        }
                        data.add(actualData);
                        current = current.getRightSon();
                        isParent = false;
                    }
                } else {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        if (actualData.getKey().compareTo(end) > 0) {
                            current = null;
                            break;
                        }
                        data.add(actualData);
                        current = current.getRightSon();
                        isParent = false;
                    } else {
                        current = current.getParent();
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
        TTTreeNode<K, T> current = (TTTreeNode<K, T>) this.root;
        ArrayList<T> data = new ArrayList<>();
        if (current == null) {
            return data;
        }
        T actualData;
        boolean isParent;
        while (current != null) {
            while (current.hasLeftSon()) {
                current = current.getLeftSon();
            }
            actualData = current.getDataL();
            data.add(actualData);
            if (current.isThreeNode()) {
                actualData = current.getDataR();
                data.add(actualData);
            }
            current = current.getParent();
            isParent = true;
            while (isParent && current != null) {
                if (current.isThreeNode()) {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        data.add(actualData);
                        current = current.getMiddleSon();
                        isParent = false;
                    } else if (actualData.compareTo(current.getDataR()) > 0) {
                        current = current.getParent();
                        isParent = true;
                    } else {
                        actualData = current.getDataR();
                        data.add(actualData);
                        current = current.getRightSon();
                        isParent = false;
                    }
                } else {
                    if (actualData.compareTo(current.getDataL()) < 0) {
                        actualData = current.getDataL();
                        data.add(actualData);
                        current = current.getRightSon();
                        isParent = false;
                    } else {
                        current = current.getParent();
                        isParent = true;
                    }
                }
            }
        }
        return data;
    }

//    public ArrayList<T> getData() {
//        return data;
//    }

//    public void clearData() {
//        data.clear();
//    }

//    public void setInterval(TTTreeNode<K, T> node, K start, K end) {
//        if (node == null) {
//            return;
//        }
//        if (start.compareTo(node.getDataL().getKey()) < 0 || end.compareTo(node.getDataL().getKey()) < 0) {
//            setInterval(node.getLeftSon(), start, end);
//        }
//        if (node.getDataL().getKey().compareTo(start) >= 0 && node.getDataL().getKey().compareTo(end) <= 0) {
//            data.add(node.getDataL());
//        }
//        if (node.isThreeNode()) {
//            if (node.getDataR().getKey().compareTo(start) >= 0 && node.getDataR().getKey().compareTo(end) <= 0) {
//                data.add(node.getDataR());
//            }
//        }
//        if (node.isThreeNode()) {
//            if (!((start.compareTo(node.getDataL().getKey()) < 0 && end.compareTo(node.getDataR().getKey()) < 0) || (start.compareTo(node.getDataL().getKey()) > 0 && end.compareTo(node.getDataR().getKey()) > 0))) {
//                setInterval(node.getMiddleSon(), start, end);
//            }
//            if (start.compareTo(node.getDataR().getKey()) > 0 || end.compareTo(node.getDataR().getKey()) > 0) {
//                setInterval(node.getRightSon(), start, end);
//            }
//        } else {
//            if (start.compareTo(node.getDataL().getKey()) > 0 || end.compareTo(node.getDataL().getKey()) > 0) {
//                setInterval(node.getRightSon(), start, end);
//            }
//        }
//    }

    /*
    Len pre testove ucely.
     */
    public void inOrderRecursive(TTTreeNode<K, T> node) {
        if (node == null)
            return;
        inOrderRecursive(node.getLeftSon());
        node.vypis();
        inOrderRecursive(node.getMiddleSon());
        inOrderRecursive(node.getRightSon());
    }

    /*
    Len pre testove ucely.
     */
    public void preorder(TTTreeNode<K, T> node) {
        if (node == null)
            return;
        node.vypis();
        preorder(node.getLeftSon());
        preorder(node.getMiddleSon());
        preorder(node.getRightSon());
    }

    /*
    Len pre testove ucely. Vypis hlbky kazdeho listu pomocou rekurzie.
    Implementacia inspirovana podla toho, co som pocul na cviceni.
     */
    public void deepOfLeaf(TTTreeNode<K, T> node) {
        if (node == null) {
            //System.out.println("null");
            return;
        }
        if (node.isLeaf()) {
            TTTreeNode<K, T> leaf = node;
            int deep = 1;
            while (true) {
                if (leaf.hasParent()) {
                    leaf = leaf.getParent();
                    deep++;
                } else {
                    break;
                }
            }
            System.out.println("Leaf deep: " + deep);
        }
        deepOfLeaf(node.getLeftSon());
        deepOfLeaf(node.getMiddleSon());
        deepOfLeaf(node.getRightSon());
    }

    private ArrayList<TTTreeNode<K, T>> getALl() {
        return null;
    }

    private TTTreeNode<K, T> findLeaf(T data) {
        TTTreeNode<K, T> leaf = (TTTreeNode<K, T>) this.root;
        while (true) {
            if (leaf.isThreeNode()) {
                if (data.compareTo(leaf.getDataL()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = leaf.getLeftSon();
                    else
                        break;
                } else if (data.compareTo(leaf.getDataR()) > 0) {
                    if (leaf.hasRightSon())
                        leaf = leaf.getRightSon();
                    else
                        break;
                } else if (data.compareTo(leaf.getDataR()) == 0 || data.compareTo(leaf.getDataL()) == 0) {
                    break;
                } else {
                    if (leaf.hasMiddleSon())
                        leaf = leaf.getMiddleSon();
                    else
                        break;
                }
            } else {
                if (data.compareTo(leaf.getDataL()) < 0) {
                    if (leaf.hasLeftSon())
                        leaf = leaf.getLeftSon();
                    else
                        break;
                } else if (data.compareTo(leaf.getDataL()) == 0) {
                    break;
                } else {
                    if (leaf.hasRightSon())
                        leaf = leaf.getRightSon();
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
    @Override
    public T search(K key) {
        TTTreeNode<K, T> result = searchNode(key);
        if (result == null) {
            return null;
        }
        if (key.compareTo(result.getDataL().getKey()) == 0) {
            return result.getDataL();
        } else if (result.isThreeNode() && key.compareTo(result.getDataR().getKey()) == 0) {
            return result.getDataR();
        }
        return null;
    }

    private TTTreeNode<K, T> searchNode(K key) {
        TTTreeNode<K, T> result = (TTTreeNode<K, T>) this.root;
        if (result == null) {
            return null;
        }
        while (key.compareTo(result.getDataL().getKey()) != 0) {
            if (result.hasDataR() && key.compareTo(result.getDataR().getKey()) == 0) {
                break;
            }
            if (result.isThreeNode()) {
                if (key.compareTo(result.getDataL().getKey()) < 0) {
                    if (result.hasLeftSon())
                        result = result.getLeftSon();
                    else
                        break;
                } else if (key.compareTo(result.getDataR().getKey()) > 0) {
                    if (result.hasRightSon())
                        result = result.getRightSon();
                    else
                        break;
                } else {
                    if (result.hasMiddleSon())
                        result = result.getMiddleSon();
                    else
                        break;
                }
            } else {
                if (key.compareTo(result.getDataL().getKey()) < 0) {
                    if (result.hasLeftSon())
                        result = result.getLeftSon();
                    else
                        break;
                } else {
                    if (result.hasRightSon())
                        result = result.getRightSon();
                    else
                        break;
                }
            }
        }
        if (result.isThreeNode()) {
            return (key.compareTo(result.getDataL().getKey()) == 0 || key.compareTo(result.getDataR().getKey()) == 0) ? result : null;
        }
        return key.compareTo(result.getDataL().getKey()) == 0 ? result : null;
    }

    private TTTreeNode<K, T> searchNodeWithData(T data) {
        TTTreeNode<K, T> result = (TTTreeNode<K, T>) this.root;
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
                        result = result.getLeftSon();
                    else
                        break;
                } else if (data.compareTo(result.getDataR()) > 0) {
                    if (result.hasRightSon())
                        result = result.getRightSon();
                    else
                        break;
                } else {
                    if (result.hasMiddleSon())
                        result = result.getMiddleSon();
                    else
                        break;
                }
            } else {
                if (data.compareTo(result.getDataL()) < 0) {
                    if (result.hasLeftSon())
                        result = result.getLeftSon();
                    else
                        break;
                } else {
                    if (result.hasRightSon())
                        result = result.getRightSon();
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

    //public TTTreeNode<K, T> getRoot() {
    //return root;
    //}

    //public int getSize() {
    //return size;
    //}

    //    public dvaTriStrom.TreeNode search(dvaTriStrom.TreeNode node) {
//        return null;
//    }

    /*
        Implementovane podla slovneho popisu z prednaskoveho dokumentu.
    */
    @Override
    public T remove(K key) {
        TTTreeNode<K, T> node = searchNode(key);
        if (node == null) {
            System.out.println("Mazanie, prvok neexistuje: " + key);
            return null;
        }
        T deletedData;
        boolean left;
        if (key.compareTo(node.getDataL().getKey()) == 0) {
            deletedData = node.getDataL();
            left = true;
        } else {
            deletedData = node.getDataR();
            left = false;
        }
        if (!tryToRemove(node, left)) {
            System.out.println("Mazanie sa nepodarilo, kluc: " + deletedData.getKey());
            return null;
        }
        size--;
        return deletedData;
    }

    /*
    Metoda removeData maze prvok rovnako ako metoda remove, ale mazany prvok hlada pomocou dat T a nie len podla kluca K.
     */
    public T removeData(T data) {
        TTTreeNode<K, T> node = searchNodeWithData(data);
        if (node == null) {
            System.out.println("Mazanie, prvok neexistuje: " + data.getKey());
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
        if (!tryToRemove(node, left)) {
            System.out.println("Mazanie sa nepodarilo, kluc: " + deletedData.getKey());
            return null;
        }
        size--;
        return deletedData;
    }

    private boolean tryToRemove(TTTreeNode<K, T> node, boolean leftToDelete) {
        //System.out.println("---------------tryToRemove------------------");
        //System.out.println("key to delete: " + (leftToDelete ? node.getDataL().getKey() : node.getDataR().getKey()));
        //System.out.println("root: " + root.getDataL().getKey());
        //System.out.println("root left son: " + root.getLeftSon().getKeyL());
        if (node.isLeaf()) {
            if (!node.isThreeNode()) {
                if (node == this.root) {
                    this.root = null;
                    height--;
                    return true;
                } else {
//                    if (node.getParent().getMiddleSon() == node) {
//                        node.getParent().setMiddleSon(null);
//                    } else if (node.getParent().getLeftSon() == node) {
//                        node.getParent().setLeftSon(null);
//                    } else {
//                        node.getParent().setRightSon(null);
//                    }
                    node.setDataL(null);
                    //return node.getDataL();
                    //node.setParent(null);
                }
            } else {
                if (leftToDelete) {
                    node.setDataL(node.getDataR());
                }
                node.setDataR(null);
                return true;
            }
        }
        TTTreeNode<K, T> inOrderLeaf = node;
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
            if (inOrderLeaf.isThreeNode()) {
                inOrderLeaf.setDataL(inOrderLeaf.getDataR());
                inOrderLeaf.setDataR(null);
            } else {
                inOrderLeaf.setDataL(null);
            }
        }

        //System.out.println("node == inOrderLeaf: " + (node == inOrderLeaf));


        while (true) {
            //System.out.println("while");
            if (inOrderLeaf.hasDataL()) {
                return true;
            }

            if (inOrderLeaf == root) {
                //System.out.println("I am root");
                if (inOrderLeaf.hasLeftSon() && inOrderLeaf.getLeftSon().hasDataL()) {
                    root = inOrderLeaf.getLeftSon();
                } else if (inOrderLeaf.hasMiddleSon() && inOrderLeaf.getMiddleSon().hasDataL()) {
                    root = inOrderLeaf.getMiddleSon();
                } else if (inOrderLeaf.hasRightSon() && inOrderLeaf.getRightSon().hasDataL()) {
                    root = inOrderLeaf.getRightSon();
                } else {
                    root = null;
                }
                if (root != null) {
                    ((TTTreeNode<?, ?>) root).setParent(null);
                }
//                if (inOrderLeaf.hasMiddleSon() && inOrderLeaf.getMiddleSon().hasKeyL()) {
//                    root = inOrderLeaf.getMiddleSon();
//                }
//                if (inOrderLeaf.hasRightSon() && inOrderLeaf.getRightSon().hasKeyL()) {
//                    root = inOrderLeaf.getRightSon();
//                }
//                if (!inOrderLeaf.hasLeftSon() && !inOrderLeaf.hasMiddleSon() && !inOrderLeaf.hasRightSon()) {
//                    root = null;
//                }
//                if (inOrderLeaf.hasLeftSon() && inOrderLeaf.hasRightSon()) {
//                    System.out.println("Possible error: inOrderLeaf.hasLeftSon() && inOrderLeaf.hasRightSon()");
//                }
//                if (inOrderLeaf.hasLeftSon() && inOrderLeaf.hasMiddleSon()) {
//                    System.out.println("Possible error: inOrderLeaf.hasLeftSon() && inOrderLeaf.hasMiddleSon()");
//                }
//                if (inOrderLeaf.hasRightSon() && inOrderLeaf.hasMiddleSon()) {
//                    System.out.println("Possible error: inOrderLeaf.hasRightSon() && inOrderLeaf.hasMiddleSon()");
//                }
                height--;
                return true;
            }

            TTTreeSonType inOrderLeafSonType = getSonType(inOrderLeaf);
            TTTreeNode<K, T> parent = inOrderLeaf.getParent();
            //System.out.println(parent.getLeftSon().getKeyL());
            //System.out.println(inOrderLeafSonType);
            if (inOrderLeafSonType == TTTreeSonType.LEFT) {
                //System.out.println("SonType.LEFT");
                if ((parent.isThreeNode() && parent.getMiddleSon().isThreeNode()) || (!parent.isThreeNode() && parent.getRightSon().isThreeNode())) {
                    if (parent.isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(parent.getMiddleSon().getDataL());
                        parent.getMiddleSon().setDataL(parent.getMiddleSon().getDataR());
                        parent.getMiddleSon().setDataR(null);
                        if (!inOrderLeaf.isLeaf() && !parent.getMiddleSon().isLeaf()) {
                            if (!inOrderLeaf.getLeftSon().hasDataL()) {
                                inOrderLeaf.setLeftSon(inOrderLeaf.getRightSon());
                            }
                            inOrderLeaf.setRightSon(parent.getMiddleSon().getLeftSon());
                            parent.getMiddleSon().setLeftSon(parent.getMiddleSon().getMiddleSon());
                            parent.getMiddleSon().setMiddleSon(null);
                            inOrderLeaf.getRightSon().setParent(inOrderLeaf);
                        }
                    } else {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(parent.getRightSon().getDataL());
                        parent.getRightSon().setDataL(parent.getRightSon().getDataR());
                        parent.getRightSon().setDataR(null);
                        if (!inOrderLeaf.isLeaf() && !parent.getRightSon().isLeaf()) {
                            if (!inOrderLeaf.getLeftSon().hasDataL()) {
                                inOrderLeaf.setLeftSon(inOrderLeaf.getRightSon());
                            }
                            inOrderLeaf.setRightSon(parent.getRightSon().getLeftSon());
                            parent.getRightSon().setLeftSon(parent.getRightSon().getMiddleSon());
                            parent.getRightSon().setMiddleSon(null);
                            inOrderLeaf.getRightSon().setParent(inOrderLeaf);
                        }
                    }
                    return true;
                } else {
                    if (parent.isThreeNode()) {
                        T tempLeftData = parent.getMiddleSon().getDataL();
                        parent.getMiddleSon().setDataL(parent.getDataL());
                        parent.getMiddleSon().setDataR(tempLeftData);

                        parent.getMiddleSon().setMiddleSon(parent.getMiddleSon().getLeftSon());

                        if (!parent.getLeftSon().isLeaf()) {
                            if (parent.getLeftSon().getRightSon().hasDataL()) {
                                //System.out.println("0");
                                parent.getMiddleSon().setLeftSon(parent.getLeftSon().getRightSon());
                                parent.getLeftSon().getRightSon().setParent(parent.getMiddleSon());
                            } else {
                                //System.out.println("1");
                                parent.getMiddleSon().setLeftSon(parent.getLeftSon().getLeftSon());
                                parent.getLeftSon().getLeftSon().setParent(parent.getMiddleSon());
                            }
                        }

                        //parent.getMiddleSon().setLeftSon(parent.getLeftSon().getLeftSon());
                        parent.setLeftSon(parent.getMiddleSon());
                        parent.setMiddleSon(null);

                        parent.setDataL(parent.getDataR());
                        parent.setDataR(null);
                    } else {
                        //System.out.println("here");
                        T tempLeftData = parent.getRightSon().getDataL();
                        parent.getRightSon().setDataL(parent.getDataL());
                        parent.getRightSon().setDataR(tempLeftData);
                        parent.setDataL(null);

                        parent.getRightSon().setMiddleSon(parent.getRightSon().getLeftSon());
                        if (inOrderLeaf.hasLeftSon() && inOrderLeaf.getLeftSon().hasDataL()) {
                            parent.getRightSon().setLeftSon(inOrderLeaf.getLeftSon());
                            inOrderLeaf.getLeftSon().setParent(parent.getRightSon());
                        } else if (inOrderLeaf.hasRightSon() && inOrderLeaf.getRightSon().hasDataL()) {
                            parent.getRightSon().setLeftSon(inOrderLeaf.getRightSon());
                            inOrderLeaf.getRightSon().setParent(parent.getRightSon());
                        }
                    }
                    //parent.setLeftSon(null);
                }
            } else if (inOrderLeafSonType == TTTreeSonType.RIGHT) {
                //System.out.println("SonType.RIGHT");
                if ((parent.isThreeNode() && parent.getMiddleSon().isThreeNode()) || (!parent.isThreeNode() && parent.getLeftSon().isThreeNode())) {
                    //System.out.println("brother is ThreeNode");
                    if (parent.isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataR());
                        parent.setDataR(parent.getMiddleSon().getDataR());
                        parent.getMiddleSon().setDataR(null);
                        if (!inOrderLeaf.isLeaf() && !parent.getMiddleSon().isLeaf()) {
                            if (!inOrderLeaf.getRightSon().hasDataL()) {
                                inOrderLeaf.setRightSon(inOrderLeaf.getLeftSon());
                            }
                            inOrderLeaf.setLeftSon(parent.getMiddleSon().getRightSon());
                            parent.getMiddleSon().setRightSon(parent.getMiddleSon().getMiddleSon());
                            parent.getMiddleSon().setMiddleSon(null);
                            inOrderLeaf.getLeftSon().setParent(inOrderLeaf);
                        }
                    } else {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(parent.getLeftSon().getDataR());
                        if (!inOrderLeaf.isLeaf() && !parent.getLeftSon().isLeaf()) {
                            if (!inOrderLeaf.getRightSon().hasDataL()) {
                                inOrderLeaf.setRightSon(inOrderLeaf.getLeftSon());
                            }
                            inOrderLeaf.setLeftSon(parent.getLeftSon().getRightSon());
                            parent.getLeftSon().setRightSon(parent.getLeftSon().getMiddleSon());
                            parent.getLeftSon().setMiddleSon(null);
                            inOrderLeaf.getLeftSon().setParent(inOrderLeaf);
                        }
                        parent.getLeftSon().setDataR(null);
                    }
                    return true;
                } else {
                    if (parent.isThreeNode()) {
                        //System.out.println("here");
                        parent.getMiddleSon().setDataR(parent.getDataR());

                        parent.getMiddleSon().setMiddleSon(parent.getMiddleSon().getRightSon());

                        if (!parent.getRightSon().isLeaf()) {
                            if (parent.getRightSon().getRightSon().hasDataL()) {
                                parent.getMiddleSon().setRightSon(parent.getRightSon().getRightSon());
                                parent.getRightSon().getRightSon().setParent(parent.getMiddleSon());
                            } else {
                                parent.getMiddleSon().setRightSon(parent.getRightSon().getLeftSon());
                                parent.getRightSon().getLeftSon().setParent(parent.getMiddleSon());
                            }
                        }

                        parent.setRightSon(parent.getMiddleSon());

//                        System.out.println(parent.getMiddleSon().getKeyL());
//                        System.out.println(parent.getKeyL());
//                        System.out.println(parent.getRightSon().getKeyL());

                        parent.setMiddleSon(null);

                        parent.setDataR(null);
                    } else {
                        //System.out.println("right son, parent 2 vrchol");
                        parent.getLeftSon().setDataR(parent.getDataL());
                        parent.setDataL(null);

                        parent.getLeftSon().setMiddleSon(parent.getLeftSon().getRightSon());
                        // System.out.println(inOrderLeaf.hasRightSon());
                        // System.out.println(inOrderLeaf.hasLeftSon());
                        if (inOrderLeaf.hasLeftSon() && inOrderLeaf.getLeftSon().hasDataL()) {
                            //System.out.println("test");
                            parent.getLeftSon().setRightSon(inOrderLeaf.getLeftSon());
                            inOrderLeaf.getLeftSon().setParent(parent.getLeftSon());
                        } else if (inOrderLeaf.hasRightSon() && inOrderLeaf.getRightSon().hasDataL()) {
                            //System.out.println("I should be here");
                            parent.getLeftSon().setRightSon(inOrderLeaf.getRightSon());
                            //System.out.println(inOrderLeaf.getRightSon());
                            inOrderLeaf.getRightSon().setParent(parent.getLeftSon());
                        }
                    }
                    //parent.setRightSon(null);
                }
            } else {
                if (parent.getLeftSon().isThreeNode() || parent.getRightSon().isThreeNode()) {
                    if (parent.getLeftSon().isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataL());
                        parent.setDataL(parent.getLeftSon().getDataR());
                        parent.getLeftSon().setDataR(null);
                        if (!inOrderLeaf.isLeaf() && !parent.getLeftSon().isLeaf()) {
                            if (!inOrderLeaf.getRightSon().hasDataL()) {
                                inOrderLeaf.setRightSon(inOrderLeaf.getLeftSon());
                            }
                            inOrderLeaf.setLeftSon(parent.getLeftSon().getRightSon());
                            parent.getLeftSon().setRightSon(parent.getLeftSon().getMiddleSon());
                            parent.getLeftSon().setMiddleSon(null);
                            inOrderLeaf.getLeftSon().setParent(inOrderLeaf);
                        }
                        return true;
                    } else if (parent.getRightSon().isThreeNode()) {
                        inOrderLeaf.setDataL(parent.getDataR());
                        parent.setDataR(parent.getRightSon().getDataL());
                        parent.getRightSon().setDataL(parent.getRightSon().getDataR());
                        parent.getRightSon().setDataR(null);
                        if (!inOrderLeaf.isLeaf() && !parent.getRightSon().isLeaf()) {
                            if (!inOrderLeaf.getLeftSon().hasDataL()) {
                                inOrderLeaf.setLeftSon(inOrderLeaf.getRightSon());
                            }
                            inOrderLeaf.setRightSon(parent.getRightSon().getLeftSon());
                            parent.getRightSon().setLeftSon(parent.getRightSon().getMiddleSon());
                            parent.getRightSon().setMiddleSon(null);
                            inOrderLeaf.getRightSon().setParent(inOrderLeaf);
                        }
                        return true;
                    }
                } else {
                    if (!parent.isThreeNode()) {
                        System.out.println("Error: !parent.isThreeNode()");
                    }
                    parent.getLeftSon().setDataR(parent.getDataL());
                    parent.setDataL(parent.getDataR());

                    parent.getLeftSon().setMiddleSon(parent.getLeftSon().getRightSon());

                    if (!parent.getMiddleSon().isLeaf()) {
                        if (parent.getMiddleSon().getRightSon().hasDataL()) {
                            parent.getLeftSon().setRightSon(parent.getMiddleSon().getRightSon());
                            parent.getMiddleSon().getRightSon().setParent(parent.getLeftSon());
                        } else {
                            parent.getLeftSon().setRightSon(parent.getMiddleSon().getLeftSon());
                            parent.getMiddleSon().getLeftSon().setParent(parent.getLeftSon());
                        }
                    }

                    parent.setMiddleSon(null);

                    parent.setDataR(null);
                }
            }

            inOrderLeaf = inOrderLeaf.getParent();
        }
        //return false;
    }

    private TTTreeSonType getSonType(TTTreeNode<K, T> node) {
        if (node != null) {
            TTTreeNode<K, T> parent = node.getParent();
            if (parent != null) {
                if (parent.getRightSon() == node) {
                    return TTTreeSonType.RIGHT;
                }
                if (parent.getMiddleSon() == node) {
                    return TTTreeSonType.MIDDLE;
                }
                if (parent.getLeftSon() == node) {
                    return TTTreeSonType.LEFT;
                }
            }
        }
        return null;
    }

    private TTTreeNode<K, T> findInOrderLeaf(TTTreeNode<K, T> node, boolean leftToDelete) {
        TTTreeNode<K, T> result = null;
        if (leftToDelete) {
            if (node.isThreeNode()) {
                if (node.hasMiddleSon()) {
                    result = node.getMiddleSon();
                } else if (node.hasRightSon()) {
                    result = node.getRightSon();
                    System.out.println("Error in findInOrderLeaf, this should not happened.");
                }
            } else {
                if (node.hasRightSon()) {
                    result = node.getRightSon();
                }
            }
        } else {
            if (node.hasRightSon()) {
                result = node.getRightSon();
            }
        }
        if (result != null) {
            while (true) {
                if (result.hasLeftSon()) {
                    result = result.getLeftSon();
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
}
