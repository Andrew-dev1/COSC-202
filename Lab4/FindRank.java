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
        
        T objectA ; 
        T smallobjectB;
        T bigobjectB;
        // edge cases for empty lists
        if(size1 == 0){ return list2.get(rank);}
        if(size2 == 0){ return list1.get(rank);}

        // ranks at the start and end of the lists
        if(rank == 0){
            objectA = list1.get(0);
            smallobjectB = list2.get(0);
            if(objectA.compareTo(smallobjectB) > 0){
                return smallobjectB;
            }
            else{
                return objectA;
            }
        }
        if(rank == size1+size2 -1){
            objectA = list1.get(size1-1);
            smallobjectB = list2.get(size2-1);
            if(objectA.compareTo(smallobjectB) > 0){
                return objectA;
            }
            else{
                return smallobjectB;
            }
        }

        if(rank >= size1 & list1.get(size1-1).compareTo(list2.get(0))< 0){
           return list2.get(rank -size1);
        }
        else if(rank >= size2 & list2.get(size2-1).compareTo(list1.get(0))< 0){
            return list1.get(rank -size1);
        }

        if (size1 > size2) {
            return findRank(list2, list1, rank);
        }



        int middleA = rank/2;
        int middleB = rank-middleA;
        int lowBound = 0; 
        int highBound = Math.min(rank, size1);

        return findInner(list1, list2, rank, lowBound, highBound);

        // return null;    // placeholder
        
    }

    public static <T extends Comparable<? super T>> T findInner(SortedList<T> list1, SortedList<T> list2, int rank, int low, int high){
        int middleA = low + (low+high) /2;
        int middleB = rank - middleA;

        System.out.println("midA: " + String.valueOf(middleA)+ " midB: "  + String.valueOf(middleB));
        if(middleB > list2.size()){
            // if the index is greater than size of list2, middleA needs to be bigger
            low = middleA +1; 
        }
        else if(middleB < 0){
            // if the index is less than 0, middleA needs to be lower
            high = middleA -1; 
        }
        else{
            T objectA = list1.get(middleA);
            T smallobjectB = list2.get(middleB);
            T bigobjectB = list2.get(middleB+1);
            
            if(objectA.compareTo(smallobjectB) >0){
                if(objectA.compareTo(bigobjectB )<0){
                    return objectA;
                }
                else{

                    if(smallobjectB.compareTo(list1.get(middleA+1) ) < 0){
                        return smallobjectB;
                    }
                    return findInner(list1, list2, rank, middleA, middleB);
                }

            }else{
                return findInner(list1, list2, rank, middleA, middleB);
            }
        }
        return null;
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
        System.out.println(findRank(list1, list2, 0));
        System.out.println(findRank(list1, list2, 19));

        System.out.println(findRank(list1, list2, 3));
        System.out.println(findRank(list1, list2, 4));
        System.out.println(findRank(list1, list2, 14));
        System.out.println(findRank(list1, list2, 20));
        System.out.println(findRank(list1, list2, 22));

        // Replace or add test cases here

    }

        
}
