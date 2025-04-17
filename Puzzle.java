/* 

	Puzzle.java
	
	COSC 202, Colgate University
	
	Starter file for Tile Puzzle assignment
	
*/

import java.util.*;

public class Puzzle {

    private static class boardState implements Comparable<boardState>  {
        int[][] board;
        private int heuristicvalue;
        private int totalCost;
        String path = "";

        public boardState(int[][] b, int heuristic, String _path, int moves){
            board = b;
            heuristicvalue = heuristic;
            path = _path;
            totalCost = moves +heuristic;
        }

        public int compareTo(boardState second){
            int difference = this.totalCost - second.totalCost;
            return difference;
        }
        public int cost(){
            return totalCost;
        }

        public String bToString(){
            StringBuilder saveString = new StringBuilder();
            for(int i = 0; i< board.length; i++){
                for(int j = 0; j < board.length; j++){
                    int number = board[i][j];
                    saveString.append(number);
                }
            }
           
            return saveString.toString();
        }

    }



	// this function creates a random puzzle of a given size
    public static int[][] makePuzzle(int size, int moves) {
        int[][] puzzle = new int[size][size];

        int i = 1;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                puzzle[row][col] = i++;


        int curRow = size-1, curCol = size-1;
        puzzle[curRow][curCol] = 0;

        Random rand = new Random();
        while (moves > 0) {
            switch (rand.nextInt(4)) {
                case 0:
                    if (curRow > 0) {
                        // up
                        int num = puzzle[curRow-1][curCol];
                        puzzle[curRow][curCol] = num;
                        puzzle[curRow-1][curCol] = 0;
                        curRow = curRow-1;
                        moves--;
                    }
                    break;

                case 1:
                    if (curRow < size-1) {
                        // down
                        int num = puzzle[curRow+1][curCol];
                        puzzle[curRow][curCol] = num;
                        puzzle[curRow+1][curCol] = 0;
                        curRow = curRow+1;
                        moves--;
                    }
                    break;

                case 2:
                    if (curCol > 0) {
                        // left
                        int num = puzzle[curRow][curCol-1];
                        puzzle[curRow][curCol] = num;
                        puzzle[curRow][curCol-1] = 0;
                        curCol = curCol - 1;
                        moves--;
                    }
                    break;

                case 3:
                    if (curCol < size-1) {
                        // right
                        int num = puzzle[curRow][curCol+1];
                        puzzle[curRow][curCol] = num;
                        puzzle[curRow][curCol+1] = 0;
                        curCol = curCol + 1;
                        moves--;
                    }
                    break;
            }
        }

        return puzzle;
    }
    
    // TODO: Implement this function
    public static String solvePuzzle(int[][] puzzle) {
        PriorityQueue<boardState> queue = new PriorityQueue<>();
        HashSet<String> visitedBoards = new HashSet<>();

        int h = heuristicCalculation(puzzle);
        queue.offer(new boardState(puzzle, h, "", 0));
        System.out.println(h);

        while(! queue.isEmpty()){
            boardState currentBoard = queue.poll();
            StringBuilder saveString = new StringBuilder(); 
            int x = -1; int y = -1;
            for(int i = 0; i< puzzle.length; i++){
                for(int j = 0; j < puzzle[0].length; j++){
                    int number = puzzle[i][j];
                    saveString.append(number);
                    if(number == 0){
                        x = i;
                        y = j;
                    }
                }
            }
            String bString = saveString.toString();
            // String bString = currentBoard.bToString();

            // add board if not visited before 
            if(!visitedBoards.contains(bString)){
                visitedBoards.add(bString);
            }

            if(currentBoard.heuristicvalue == 0){
                return currentBoard.path;
            }

// Try moving in 4 directions
int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
char[] moves = {'U','D','L','R'};

for (int i = 0; i < 4; i++) {
    int newX = x + directions[i][0];
    int newY = y + directions[i][1];

    if (newX >= 0 && New < puzzle.length && ny >= 0 && ny < puzzle[0].length) {
        int[][] newBoard = deepCopy(currentBoard.board);
        newBoard[x][y] = newBoard[nx][ny];
        newBoard[nx][ny] = 0;

        String newPath = currentBoard.path + moves[i];
        int newCost = heuristicCalculation(newBoard) + newPath.length(); // A* cost

        queue.offer(new boardState(newBoard, newCost, newPath));
    }
}

        }



        

        return null;    // replace by returning a string corresponding to a sequence of moves to solve the puzzle
    }

    public static void recurse(int[][] puzzle, HashSet<String> visitedBoards, int n){

        StringBuilder saveString = new StringBuilder(); 
        int x = 0; int y = 0;
        for(int i = 0; i< puzzle.length; i++){
            for(int j = 0; j < puzzle[i].length; j++){
                int number = puzzle[i][j];
                saveString.append( Integer.toString(number));
                if(number == 0){
                    x = i;
                    y = j;
                }
            }
        }
        if(!visitedBoards.contains(saveString.toString())){
            visitedBoards.add(saveString.toString());
        }

        // add to priority queue 

        // move by 1, calculate heuristic value and call function again 
        if((x+1) < n){
            int swap = puzzle[x+1][y];
            puzzle[x+1][y] = 0;
            puzzle[x][y] = swap;
            // call recursion 
        }
        if( x- 1 > 0){
            int swap = puzzle[x+1][y];
            puzzle[x+1][y] = 0;
            puzzle[x][y] = swap;
            // call recursion 
        }
        if((y+1) < n){
            int swap = puzzle[x][y+1];
            puzzle[x][y+1] = 0;
            puzzle[x][y] = swap;
            // call recursion 
        }        
        if((y-1) >0 ){
            int swap = puzzle[x][y-1];
            puzzle[x][y-1] = 0;
            puzzle[x][y] = swap;
            // call recursion 
        }

        // call next board in priority queue with lowest heuristic value 

    }
    public boolean matched(){

        return false;
    }

    // done 
    public static int heuristicCalculation(int[][]puzzle){
        int n = puzzle.length;
        int value = 0; 
        for(int i= 0; i< n; i++){
            for(int j = 0; j< n; j++){
                int number = puzzle[i][j];

                // for 2x2, perfect => 1 =(0,0) 2= (0,1) 3= (1,0) 0= (1,1)
                if(number!= 0){
                    // int row = (number -1) / n;
                    // int col = (number-1) % n;
                    value += Math.abs(i- ((number-1) / n)) + Math.abs(j- ((number-1) % n));
                }
            }
        }

        return value; 
    }

    // you can use this for testing
    public static void main(String[] args) {

        /* right now, this function creates two variables:
            1. the size of the puzzle (number of rows/cols) as an integer
            2. the number of random moves from the goal used to create a starting puzzle
           and then calls makePuzzle using those values

           feel free to modify this as you want for testing
           (only your solvePuzzle function will be examined)
        */
    
        

        int size = 4;
        int moves = 15;

        int[][] puzzle = makePuzzle(size, moves);

        System.out.println("Puzzle:");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.printf("%4d", puzzle[row][col]);
            }
            System.out.println();
        }

        String solution = solvePuzzle(puzzle);
        System.out.println("Puzzle:");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.printf("%4d", puzzle[row][col]);
            }
            System.out.println();
        }
        

        System.out.println("\nSolution:");
        System.out.println(solution);

        int[][] puzzle1 = makePuzzle(size, 0);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.printf("%4d", puzzle1[row][col]);
            }
            System.out.println();
        }
        System.out.println(Puzzle.heuristicCalculation(puzzle1));

    }
}
