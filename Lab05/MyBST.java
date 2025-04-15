public class MyBST<K extends Comparable<? super K>, V> extends RedBlackBST<K, V> {

    public MyBST() {
        super();
    }


    // put any BST additional or modified functionality here
    protected V gethelper(int rank) {
               
        int left = root.getLeft() == null ? 0 :root.getLeft().getSize();
        Node<K, V> current = root; 
        
        while(rank != (left)){
            if(left > rank){
                current = current.getLeft();
            }
            else{
                rank -= (left+1);
                current = current.getRight();
            }
            left = current.getLeft() == null ? 0 :current.getLeft().getSize();
        }
        
        return current.getValue();
    } 

    protected Node<K,V> findPosition(int rank){
        rank++;    
        int left = root.getLeft() == null ? 0 :root.getLeft().getSize();
        System.out.println(root.getLeft().getValue());
        Node<K, V> current = root; 
        while(rank != (left+1)){
            if(left >= rank){
                current = current.getLeft();
            }
            else{
                rank -= (left+1);
                current = current.getRight();
            }
            left = current.getLeft() == null ? 0 :current.getLeft().getSize();
        }
        return current;
    }
    


}
