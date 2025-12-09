import java.lang.foreign.Linker.Option;
import java.util.Arrays;

public class ConcatSequence {
    
    public ConcatSequence(){

    }

    public static int[] optimalOrder(int[] stringLengths){


        return null;
    }
    public static int OPT(int[] stringLengths){
        if(stringLengths == null || stringLengths.length == 0){
            return -100;
        }

        if(stringLengths.length == 2){
            return stringLengths[0]+ stringLengths[1];
        }
        int[][] grid = new int[stringLengths.length][stringLengths.length];

        for(int i = 0; i< stringLengths.length-1; i++){
            grid[i][i+1] = stringLengths[i] + stringLengths[i+1];
        }
        System.out.println(grid.length);
        System.out.println(grid[0].length);
        deepPrints(grid);
        return -100; 
    }
    
    public static void deepPrints(int[][] arr2D){
        for(int i = 0; i< arr2D.length; i++){
            int[] row = arr2D[i];
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) {
        int[] x = {2,3,4,2,3,4,5};
        OPT(x);
    }

}
