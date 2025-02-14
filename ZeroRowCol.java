import java.util.*;

public class ZeroRowCol {

	public static void zeroRowCol(int[][] arr) {

		HashSet<Integer> rowSet = new HashSet<>();
        HashSet<Integer> colSet = new HashSet<>();

        for(int i = 0; i< arr.length; i++){
            int[] col = arr[i];
            for(int j = 0; j< col.length; j++){
                if(arr[i][j] == 0){
                    rowSet.add(i);
                    colSet.add(j);
                }
            }
        }
        for(int i = 0; i< arr.length; i++){
            int[] col = arr[i];
            for(int j = 0; j< col.length; j++){
                if(rowSet.contains(i)){
                    arr[i][j] = 0;
                }
                else if(colSet.contains(j)){
                    arr[i][j] = 0;
                }
            }
        }
	}

	public static void main(String[] args) {
        ArrayList<int[][]> arrs = new ArrayList<>();
        int[][] arr1 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        int[][] arr2 = { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } };
        int[][] arr3 = { { 0, 1, 2, 3, 4, 5 },{ 6, 7, 8, 9, 10, 11 } };
        int[][] arr4 = { { 1, 0, 1, 0, 1 }, { 5, 5, 5, 5, 5 }, { 1, 0, 1, 0, 1 } };
        int[][] arr5 = { };
        int[][] arr6 = { { 1, 0, 1 }, { }, { 9, 8, 7 } };

        arrs.add(arr1);
        arrs.add(arr2);
        arrs.add(arr3);
        arrs.add(arr4);
        arrs.add(arr5);
        arrs.add(arr6);

        for(int[][] test: arrs){
            for(int[] innerRow: test){
                System.out.println(Arrays.toString(innerRow));
            }
            zeroRowCol(test);
            for(int[] innerRow: test){
                System.out.println(Arrays.toString(innerRow));
            }            
            System.out.println();
        }
        List<Integer> l = new ArrayList<Integer>();
        for(int i = 0; i< 10; i++){
            l.add(i);
        }
        for (int i = 0; i < l.size() / 2; i++){
            l.remove(0);
            System.out.println("removed one");
        }
        
		/* Put any of your own tests here
			 This code won't be executed when you submit */

	}
}