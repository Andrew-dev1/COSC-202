import java.util.*;

public class LogList<E> {
    
    private int length;
    private MyBST<Double,E> tree;

    

    public LogList() {
        tree  = new MyBST<>();
        tree.root = null;
        length = 0;
    }

    public LogList(Collection<E> items) {
        this();
        if(items == null){
            throw new IndexOutOfBoundsException(" ");
        }
        ArrayList<E> arr = new ArrayList<>(items);
        if(!arr.isEmpty()){
            tree.root = constructing(0, arr.size() - 1, arr);
        }
        length = arr.size();
        
    }

    public Node<Double, E> constructing(int start, int end, ArrayList<E> arr){
        // base cases 
        if(start > end){
            return null;
        }

        int mid = (start +end) / 2;
        // Use the index directly as the key
        double key = (double)mid;
        Node<Double, E> node = new Node<>(key, arr.get(mid), false, 1);
        
        // recursively construct the left and right nodes 
        node.setLeft(constructing(start, mid - 1, arr));
        node.setRight(constructing(mid + 1, end, arr));

        int sizeLeft = 0; int sizeRight = 0;
        if(node.getLeft() != null)
            sizeLeft = node.getLeft().getSize();
        if(node.getRight() != null)
            sizeRight = node.getRight().getSize();
        
        node.setSize(sizeLeft+ sizeRight+1); 

        return node;
    }

    public int size() {
        return length;
    }

    public void insert(int index, E item) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(index);
        if(tree.isEmpty()){ 
            tree.put(0.0, item);
        }
        else{
            double key;
            if(index == 0){
                Node<Double, E> node = findNode(tree.root, index);
                key = node.getKey()-1;
            }
            else if(index == size()){
                Node<Double, E> node = findNode(tree.root, index);
                key = node.getKey()+1;
            }
            else{
                Node<Double, E> left = findNode(tree.root, index-1);
                Node<Double, E> right = findNode(tree.root, index);
                key = (left.getKey() + right.getKey()) / 2;
            }
            tree.put(key, item);
        }
        
        length ++; 
    }

    public E get(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(index);
        if(tree.root == null){
            return null;
        } 
        return findNode(tree.root, index).getValue();
    }


    public E delete(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(index);

        Node<Double, E> node = findNode(tree.root, index);// tree.findPosition(index);
        tree.delete(node.getKey());
        length --; 
        return node.getValue();
    }
    private Node<Double, E> findNode(Node<Double, E> current, int rank){
        if(current == null){
            return null;
        }
        int left = current.getLeft() == null ? 0 :current.getLeft().getSize();
        if(left == rank) return current;
        if(left > rank) return(findNode(current.getLeft(), rank));
        if(left < rank) return(findNode(current.getRight(), rank-(left+1)));
        return null;
    }

    public String toString() {
        ArrayList<E> results = new ArrayList<>();
        helper1(results, tree.root);
        
        return results.toString();
    }
    public ArrayList<E> helper1(ArrayList<E> builder, Node<Double,E> node){
        if(node == null)
            return builder;

        helper1(builder, node.getLeft());
        builder.add(node.getValue()); 
        helper1(builder, node.getRight());
        return builder; 
    }


    public static void main(String[] args) {
        Set<String> strings = new HashSet<>();
        strings.add("apple");
        strings.add("banana");
        strings.add("cherry");
        strings.add("tooth");

        LogList<String> l1 = new LogList<>(strings);

        LogList<String> l = new LogList<>();
        System.out.println(l1);
        System.out.println(l1.get(0));
        System.out.println(l1.get(1));
        System.out.println(l1.get(2));
        System.out.println(l1.get(3));
    }

}
