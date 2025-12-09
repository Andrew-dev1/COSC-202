import java.util.*;


public class FindRank{
    private static final boolean debug = false;

    private static void debug(String output) {
        if (debug)
            System.out.println(output);
    }


    public static <T extends Comparable<? super T>> T findRank(SortedList<T> list1, SortedList<T> list2, int rank) {
        
        // checks for ranks that are too big or too small
        if(list1.size() +list2.size() <= rank || rank < 0){
            return null;
        }
        int size1 = list1.size();
        int size2 = list2.size();

        // makes list1 the smallest list
        if (size1 > size2) {
            return findRank(list2, list1, rank);
        }

        // edge cases for empty lists
        if(size1 == 0){ return list2.get(rank);}
        if(size2 == 0){ return list1.get(rank);}

        // ranks at the start and end of the lists
        if(rank == 0){
            T objectA = list1.get(0);
            T smallobjectB = list2.get(0);
            if(objectA.compareTo(smallobjectB) > 0){
                return smallobjectB;
            }
            else{
                return objectA;
            }
        }
        if(rank == size1+size2 -1){
            T objectA = list1.get(size1-1);
            T smallobjectB = list2.get(size2-1);
            if(objectA.compareTo(smallobjectB) > 0){
                return objectA;
            }
            else{
                return smallobjectB;
            }
        }


        // edge cases for one list being bigger than the other 
        if (list1.get(size1 - 1).compareTo(list2.get(0)) < 0) {
            if (rank < size1) {
                return list1.get(rank);
            } else {
                return list2.get(rank - size1);
            }
        }

        if (list2.get(size2 - 1).compareTo(list1.get(0)) < 0) {
            if (rank < size2) {
                return list2.get(rank);
            } else {
                return list1.get(rank - size2);
            }
        }

        // setting boundaries
        int low = Math.max(0, rank - size2);
        int high = Math.min(size1 - 1, rank);
        
        return findInner(list1, list2, rank, low, high);
    }
    
    private static <T extends Comparable<? super T>> T findInner(
            SortedList<T> list1, SortedList<T> list2, int rank, int low, int high) {
        
        // Base case: search space is exhausted
        if (low > high) {
            return null;
        }
        
        // Calculate midpoints
        int mid1 = low + (high - low) / 2;
        int mid2 = rank - mid1 - 1;
        
         // Get elements at partition
        T a = list1.get(mid1);
        T b = list2.get(mid2);
        
        // Compare with next elements if they exist
        T nextA = (mid1 + 1 < list1.size()) ? list1.get(mid1 + 1) : null;
        T nextB = (mid2 + 1 < list2.size()) ? list2.get(mid2 + 1) : null;
        
        // Check if we've found the right partition
        boolean correctPartition = (nextB == null || a.compareTo(nextB) <= 0) && 
                                (nextA == null || b.compareTo(nextA) <= 0);
        
        if (correctPartition) {
            return a.compareTo(b) >= 0 ? a : b;
        } 
        else if (b.compareTo(nextA) > 0) {
            return findInner(list1, list2, rank, mid1 + 1, high);
        } 
        else {
            return findInner(list1, list2, rank, low, mid1 - 1);
        }
    }


    public static void main(String [] args){
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        for (int x = 0; x < 20; x += 2){
            a.add(x);
            b.add(x+3);
        }
        
        // SortedList<Integer> list1 = new SortedList<Integer>(a);
        // SortedList<Integer> list2 = new SortedList<Integer>(b);

        // System.out.println(list1);
        // System.out.println(list2);
        // System.out.println(findRank(list1, list2, 0));
        // System.out.println(findRank(list1, list2, 19));

        // System.out.println(findRank(list1, list2, 3));
        // System.out.println(findRank(list1, list2, 4));
        // System.out.println(findRank(list1, list2, 14));
        // System.out.println(findRank(list1, list2, 19));
        // System.out.println(findRank(list1, list2, 17));
        // System.out.println(findRank(list1, list2, 22));

        // Replace or add test cases here
        // ArrayList<Integer> c = new ArrayList<Integer>(Arrays.asList(100, 103, 104, 107, 108, 111, 112, 115, 116, 119));
        // ArrayList<Integer> d = new ArrayList<Integer>(Arrays.asList(101, 102, 105, 106, 109, 110, 113, 114, 117, 118));
        // SortedList<Integer> list1 = new SortedList<Integer>(c);
        // SortedList<Integer> list2 = new SortedList<Integer>(d);

        // int[] arr = {0, 1, 2, 3};
        // for(int i: arr){
        //     System.out.println(findRank(list2, list1, i));
        // }
        // ArrayList<Integer> aa = new ArrayList<Integer>(Arrays.asList(300, 400));
        // ArrayList<Integer> bb = new ArrayList<Integer>(Arrays.asList(200, 500));
        // SortedList<Integer> list3 = new SortedList<Integer>(aa);
        // SortedList<Integer> list4 = new SortedList<Integer>(bb);
        
        // for(int i: arr){
        //     System.out.println(findRank(list3, list4, i));
        // }
    }

        
}
