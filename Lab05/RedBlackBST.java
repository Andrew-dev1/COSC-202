/******************************************************************************
 *  Source:   https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java
 *
 *  A symbol table implemented using a left-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *  (c) 2000-2022, Robert Sedgewick and Kevin Wayne
 *  From section 3.3 of the textbook Algorithms 4/E
 *
 ******************************************************************************/

import java.util.NoSuchElementException;
import java.util.LinkedList;

/**
 *  The {@code BST} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides ordered methods for finding the <em>minimum</em>,
 *  <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  <p>
 *  This implementation uses a <em>left-leaning red-black BST</em>.
 *  The <em>put</em>, <em>get</em>, <em>contains</em>, <em>remove</em>,
 *  <em>minimum</em>, <em>maximum</em>, <em>ceiling</em>, <em>floor</em>,
 *  <em>rank</em>, and <em>select</em> operations each take
 *  &Theta;(log <em>n</em>) time in the worst case, where <em>n</em> is the
 *  number of key-value pairs in the symbol table.
 *  The <em>size</em>, and <em>is-empty</em> operations take &Theta;(1) time.
 *  The <em>keys</em> methods take
 *  <em>O</em>(log <em>n</em> + <em>m</em>) time, where <em>m</em> is
 *  the number of keys returned by the iterator.
 *  Construction takes &Theta;(1) time.
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class RedBlackBST<K extends Comparable<? super K>, V> {

    protected Node<K, V> root;     // root of the BST

    /**
     * Initializes an empty symbol table.
     */
    public RedBlackBST() {
    }

   /***************************************************************************
    *  Node<K, V> helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
    protected boolean isRed(Node<K, V> x) {
        if (x == null) return false;
        return x.getColor() == Node.RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    protected int size(Node<K, V> x) {
        if (x == null) return 0;
        return x.getSize();
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

   /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }


   /***************************************************************************
    *  Standard BST search.
    ***************************************************************************/

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    protected V get(Node<K, V> x, K key) {
        while (x != null) {
            int cmp = key.compareTo(x.getKey());
            if      (cmp < 0) x = x.getLeft();
            else if (cmp > 0) x = x.getRight();
            else              return x.getValue();
        }
        return null;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(K key) {
        return get(key) != null;
    }

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(K key, V val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }

        root = put(root, key, val);
        root.setColor(Node.BLACK);
    }

    // insert the key-value pair in the subtree rooted at h
    protected Node<K, V> put(Node<K, V> h, K key, V val) {
        if (h == null) return new Node<K, V>(key, val, Node.RED, 1);

        int cmp = key.compareTo(h.getKey());
        if      (cmp < 0) h.setLeft(put(h.getLeft(),  key, val));
        else if (cmp > 0) h.setRight(put(h.getRight(), key, val));
        else              h.setValue(val);

        // fix-up any right-leaning links
        if (isRed(h.getRight()) && !isRed(h.getLeft()))      h = rotateLeft(h);
        if (isRed(h.getLeft())  &&  isRed(h.getLeft().getLeft())) h = rotateRight(h);
        if (isRed(h.getLeft())  &&  isRed(h.getRight()))     flipColors(h);
        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);

        return h;
    }

   /***************************************************************************
    *  Red-black tree deletion.
    ***************************************************************************/

    /**
     * Removes the smallest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.getLeft()) && !isRed(root.getRight()))
            root.setColor(Node.RED);

        root = deleteMin(root);
        if (!isEmpty()) root.setColor(Node.BLACK);
    }

    // delete the key-value pair with the minimum key rooted at h
    protected Node<K, V> deleteMin(Node<K, V> h) {
        if (h.getLeft() == null)
            return null;

        if (!isRed(h.getLeft()) && !isRed(h.getLeft().getLeft()))
            h = moveRedLeft(h);

        h.setLeft(deleteMin(h.getLeft()));
        return balance(h);
    }


    /**
     * Removes the largest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.getLeft()) && !isRed(root.getRight()))
            root.setColor(Node.RED);

        root = deleteMax(root);
        if (!isEmpty()) root.setColor(Node.BLACK);
    }

    // delete the key-value pair with the maximum key rooted at h
    protected Node<K, V> deleteMax(Node<K, V> h) {
        if (isRed(h.getLeft()))
            h = rotateRight(h);

        if (h.getRight() == null)
            return null;

        if (!isRed(h.getRight()) && !isRed(h.getRight().getLeft()))
            h = moveRedRight(h);

        h.setRight(deleteMax(h.getRight()));

        return balance(h);
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        // if both children of root are black, set root to red
        if (!isRed(root.getLeft()) && !isRed(root.getRight()))
            root.setColor(Node.RED);

        root = delete(root, key);
        if (!isEmpty()) root.setColor(Node.BLACK);
    }

    // delete the key-value pair with the given key rooted at h
    protected Node<K, V> delete(Node<K, V> h, K key) {

        if (key.compareTo(h.getKey()) < 0)  {
            if (!isRed(h.getLeft()) && !isRed(h.getLeft().getLeft()))
                h = moveRedLeft(h);
            h.setLeft(delete(h.getLeft(), key));
        }
        else {
            if (isRed(h.getLeft()))
                h = rotateRight(h);
            if (key.compareTo(h.getKey()) == 0 && (h.getRight() == null))
                return null;
            if (!isRed(h.getRight()) && !isRed(h.getRight().getLeft()))
                h = moveRedRight(h);
            if (key.compareTo(h.getKey()) == 0) {
                Node<K, V> x = min(h.getRight());
                h.setKey(x.getKey());
                h.setValue(x.getValue());
                h.setRight(deleteMin(h.getRight()));
            }
            else h.setRight(delete(h.getRight(), key));
        }
        return balance(h);
    }

   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    // make a left-leaning link lean to the right
    protected Node<K, V> rotateRight(Node<K, V> h) {
        Node<K, V> x = h.getLeft();
        h.setLeft(x.getRight());
        x.setRight(h);
        x.setColor(h.getColor());
        h.setColor(Node.RED);
        x.setSize(h.getSize());
        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);
        return x;
    }

    // make a right-leaning link lean to the left
    protected Node<K, V> rotateLeft(Node<K, V> h) {
        Node<K, V> x = h.getRight();
        h.setRight(x.getLeft());
        x.setLeft(h);
        x.setColor(h.getColor());
        h.setColor(Node.RED);
        x.setSize(h.getSize());
        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);
        return x;
    }

    // flip the colors of a node and its two children
    protected void flipColors(Node<K, V> h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.flipColor();
        h.getLeft().flipColor();
        h.getRight().flipColor();
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    protected Node<K, V> moveRedLeft(Node<K, V> h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.getRight().getLeft())) {
            h.setRight(rotateRight(h.getRight()));
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    protected Node<K, V> moveRedRight(Node<K, V> h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.getLeft().getLeft())) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    protected Node<K, V> balance(Node<K, V> h) {
        // assert (h != null);

        if (isRed(h.getRight()) && !isRed(h.getLeft()))    h = rotateLeft(h);
        if (isRed(h.getLeft()) && isRed(h.getLeft().getLeft())) h = rotateRight(h);
        if (isRed(h.getLeft()) && isRed(h.getRight()))     flipColors(h);

        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);
        return h;
    }


   /***************************************************************************
    *  Utility functions.
    ***************************************************************************/

    /**
     * Returns the height of the BST (for debugging).
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }
    private int height(Node<K, V> x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.getLeft()), height(x.getRight()));
    }

   /***************************************************************************
    *  Ordered symbol table methods.
    ***************************************************************************/

    /**
     * Returns the smallest key in the symbol table.
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public K min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).getKey();
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node<K, V> min(Node<K, V> x) {
        // assert x != null;
        if (x.getLeft() == null) return x;
        else return min(x.getLeft());
    }

    /**
     * Returns the largest key in the symbol table.
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public K max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).getKey();
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node<K, V> max(Node<K, V> x) {
        // assert x != null;
        if (x.getRight() == null) return x;
        else return max(x.getRight());
    }


    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public K floor(K key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node<K, V> x = floor(root, key);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x.getKey();
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node<K, V> floor(Node<K, V> x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.getKey());
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.getLeft(), key);
        Node<K, V> t = floor(x.getRight(), key);
        if (t != null) return t;
        else           return x;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public K ceiling(K key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node<K, V> x = ceiling(root, key);
        if (x == null) throw new NoSuchElementException("argument to ceiling() is too large");
        else return x.getKey();
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node<K, V> ceiling(Node<K, V> x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.getKey());
        if (cmp == 0) return x;
        if (cmp > 0)  return ceiling(x.getRight(), key);
        Node<K, V> t = ceiling(x.getLeft(), key);
        if (t != null) return t;
        else           return x;
    }

    /**
     * Return the key in the symbol table of a given {@code rank}.
     * This key has the property that there are {@code rank} keys in
     * the symbol table that are smaller. In other words, this key is the
     * ({@code rank}+1)st smallest key in the symbol table.
     *
     * @param  rank the order statistic
     * @return the key in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and
     *        <em>n</em>–1
     */
    public K select(int rank) {
        if (rank < 0 || rank >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        }
        return select(root, rank);
    }

    // Return key in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private K select(Node<K, V> x, int rank) {
        if (x == null) return null;
        int leftSize = size(x.getLeft());
        if      (leftSize > rank) return select(x.getLeft(),  rank);
        else if (leftSize < rank) return select(x.getRight(), rank - leftSize - 1);
        else                      return x.getKey();
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(K key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(K key, Node<K, V> x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.getKey());
        if      (cmp < 0) return rank(key, x.getLeft());
        else if (cmp > 0) return 1 + size(x.getLeft()) + rank(key, x.getRight());
        else              return size(x.getLeft());
    }

   /***************************************************************************
    *  Range count and range search.
    ***************************************************************************/

    /**
     * Returns all keys in the symbol table in ascending order as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (K key : st.keys())}.
     * @return all keys in the symbol table in ascending order
     */
    public Iterable<K> keys() {
        if (isEmpty()) return new LinkedList<K>();
        return keys(min(), max());
    }

    /**
     * Returns all keys in the symbol table in the given range in ascending order,
     * as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between {@code lo}
     *    (inclusive) and {@code hi} (inclusive) in ascending order
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
    public Iterable<K> keys(K lo, K hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        LinkedList<K> queue = new LinkedList<K>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node<K, V> x, LinkedList<K> queue, K lo, K hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.getKey());
        int cmphi = hi.compareTo(x.getKey());
        if (cmplo < 0) keys(x.getLeft(), queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.getKey());
        if (cmphi > 0) keys(x.getRight(), queue, lo, hi);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo}
     *    (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
    public int size(K lo, K hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }


   /***************************************************************************
    *  Check integrity of red-black tree data structure.
    ***************************************************************************/
    public boolean check() {
        if (!isBST())            System.err.println("Not in symmetric order");
        if (!isSizeConsistent()) System.err.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.err.println("Ranks not consistent");
        if (!is23())             System.err.println("Not a 2-3 tree");
        if (!isBalanced())       System.err.println("Not balanced");
        return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    public boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: elegant solution due to Bob Dondero
    private boolean isBST(Node<K, V> x, K min, K max) {
        if (x == null) return true;
        if (min != null && x.getKey().compareTo(min) <= 0) return false;
        if (max != null && x.getKey().compareTo(max) >= 0) return false;
        return isBST(x.getLeft(), min, x.getKey()) && isBST(x.getRight(), x.getKey(), max);
    }

    // are the size fields correct?
    public boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node<K, V> x) {
        if (x == null) return true;
        if (x.getSize() != size(x.getLeft()) + size(x.getRight()) + 1) return false;
        return isSizeConsistent(x.getLeft()) && isSizeConsistent(x.getRight());
    }

    // check that ranks are consistent
    public boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (K key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    public boolean is23() { return is23(root); }
    private boolean is23(Node<K, V> x) {
        if (x == null) return true;
        if (isRed(x.getRight())) return false;
        if (x != root && isRed(x) && isRed(x.getLeft()))
            return false;
        return is23(x.getLeft()) && is23(x.getRight());
    }

    // do all paths from root to leaf have same number of black edges?
    public boolean isBalanced() {
        int black = 0;     // number of black links on path from root to min
        Node<K, V> x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.getLeft();
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    public boolean isBalanced(Node<K, V> x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.getLeft(), black) && isBalanced(x.getRight(), black);
    }


    /***************************************************************************
    *  Testing
    ***************************************************************************/

    /**
     * Prints a preorder traversal representation of this RB-BST.
     * @return a string representing a preorder traversal of the tree
     */
    public String toString() {
        StringBuilder b = new StringBuilder();

        preorder(b, root);
        return b.toString();
    }

    private void addString(StringBuilder b, String s) {
        if (b.length() == 0)
            b.append(s);
        else {
            b.append(", ");
            b.append(s);
        }
    }

    private void preorder(StringBuilder b, Node<K, V> n) {
        if (n == null)
            addString(b, "null");
        else {
            addString(b, String.format("%s (%s, %s)", isRed(n) ? "R" : "B", n.getKey().toString(), n.getValue().toString()));
            preorder(b, n.getLeft());
            preorder(b, n.getRight());
        }
    }

    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        RedBlackBST<String, Integer> tree = new RedBlackBST<>();
        tree.check();
        System.out.println(tree);

    }
}
