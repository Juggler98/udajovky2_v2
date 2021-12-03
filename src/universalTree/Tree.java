package universalTree;

public abstract class Tree<K extends Comparable<K>, T extends Comparable<T> & TreeKey<K>> {

    protected TreeNode root;
    protected int size = 0;

    public abstract boolean add(T data);
    public abstract T search(K key);
    public abstract T remove(K key);

    public TreeNode getRoot() {
        return this.root;
    }

    public int getSize() {
        return this.size;
    }
}
