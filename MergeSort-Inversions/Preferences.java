import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Preferences {

    public static int inversions;
    public Preferences(){
        
    }

    public static <T> double preferenceSimilarity(ArrayList<T> list1, ArrayList<T> list2){
        HashMap<T, Integer> location = new HashMap<>();
        int index = 0;
        int[] list2Numbers = new int[list2.size()];
        for(T item: list1){
            location.put(item, index++);
        }

        // converts all of the items into integers based on their values
        for(int i = 0; i< list2.size(); i++ ){
            list2Numbers[i] = location.get(list2.get(i));
        }

        double total = (index * (index -1) / 2);
        mergesort(list2Numbers, 0, list2Numbers.length - 1);        
        double ratio = (total-inversions)/ total;
        inversions = 0;
        return ratio;
    }

    public static void mergesort(int[] list, int start, int end){
        if(end-start >= 1){
            int mid = (start+end) /2;
            mergesort(list, start, mid);
            mergesort(list, mid+1, end);
            int[] section = merge(list, start, mid, mid+1, end);
            for(int i = 0; i < section.length; i++){
                list[start+i] = section[i];
            }
        }
        
    }
    

    public static int[] merge(int[] list, int start1, int end1, int start2, int end2){
        int[] endResult = new int[(end1-start1) +( end2 - start2) +2];
        int counter = 0;
        while(start1 <= end1 && start2 <= end2){
            if(list[start1] < list[start2]){
                endResult[counter] = list[start1];
                start1 ++; 
            }
            else{
                endResult[counter] = list[start2];
                start2 ++; 
                inversions += (end1 - start1 + 1);
            }
            counter++;
        }

        if(start1 > end1){
            while(start2 <= end2)
                endResult[counter++] = list[start2++];
        }
        if(start2 > end2){
            while(start1 <= end1)
                endResult[counter++] = list[start1++];
        }
        return endResult;
    }

    public static void main(String[] args) {
        // ArrayList<String> l1 = new ArrayList<>();
        // ArrayList<String> l2 = new ArrayList<>();

        // l1.add("A");
        // l1.add("B");
        // l1.add("C");
        // l1.add("D");
        // l1.add("E");
        // l1.add("F");
        
        // l2.add("A");
        // l2.add("F");
        // l2.add("B");
        // l2.add("E");
        // l2.add("D");
        // l2.add("C");

        // preferenceSimilarity(l1,l2);

        // ArrayList<String> l3 = new ArrayList<>();
        // ArrayList<String> l4 = new ArrayList<>();
        
        // List<String> list = Arrays.asList("A", "D", "B", "G", "C", "F", "E");
        // l3.addAll(list);
        // list = Arrays.asList("B", "A", "D", "G", "E", "C", "F"); 
        // l4.addAll(list);
        // preferenceSimilarity(l3,l4);

    }


}
