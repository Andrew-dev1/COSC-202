// BST helper node data type

public class Node<K extends Comparable<? super K>, V> {

    public static final boolean RED   = true;
    public static final boolean BLACK = false;

    private K key;           // key
    private V val;         // associated data
    private Node<K, V> left, right;  // links to left and right subtrees
    private boolean color;     // color of parent link
    private int size;          // subtree count

    public Node(K key, V val, boolean color, int size) {
        this.key = key;
        this.val = val;
        this.color = color;
        this.size = size;
    }

    public K getKey() { return key; }
    public V getValue() { return val; }
    public boolean getColor() { return color; }
    public int getSize() { return size; }

    public void setKey(K key) { this.key = key; }
    public void setValue(V val) { this.val = val; }
    public void setColor(boolean color) { this.color = color; }
    public void flipColor() { this.color = !this.color; }
    public void setSize(int size) { this.size = size; }

    public Node<K, V> getLeft() { return left; }
    public Node<K, V> getRight() { return right; }

    public void setLeft(Node<K, V> n) { this.left = n; }
    public void setRight(Node<K, V> n) { this.right = n; }

}    
