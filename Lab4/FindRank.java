import java.util.*;


public class FindRank{
    private static final boolean debug = false;

    private static void debug(String output) {
        if (debug)
            System.out.println(output);
    }


    public static <T extends Comparable<? super T>> T findRank(SortedList<T> list1, SortedList<T> list2, int rank) {
        int smaller;
        int larger;

        if(list1.size() < list2.size()){
            smaller = list1.size();
            larger = list2.size();
        } else{
            smaller = list2.size();
            larger = list1.size();
        }

        if(smaller +larger <= rank){
            return null;
        }
    
        T object1 = list1.get(rank/2);
        T object2 = list2.get(rank/2);

        if(rank == 1){
            if(object1.compareTo(object2) > 0){
                return object2;
            }
            else{
                return object1;
            }
        }


        return null;    // placeholder
        
    }



    public static void main(String [] args){
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        for (int x = 0; x < 20; x += 2){
            a.add(x);
            b.add(x+3);
        }

        SortedList<Integer> list1 = new SortedList<Integer>(a);
        SortedList<Integer> list2 = new SortedList<Integer>(b);

        System.out.println(list1);
        System.out.println(list2);
        
        System.out.println(findRank(list1, list2, 1));
        System.out.println(findRank(list1, list2, 3));
        System.out.println(findRank(list1, list2, 4));
        System.out.println(findRank(list1, list2, 20));
        System.out.println(findRank(list1, list2, 22));

        // Replace or add test cases here

    }

        
}
