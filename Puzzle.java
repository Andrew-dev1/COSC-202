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
        private int moves;
        String path = "";

        public boardState(int[][] b, int heuristic, String _path, int move){
            board = b;
            heuristicvalue = heuristic;
            path = _path;
            moves = move;
        }

        public int compareTo(boardState second){
            int difference = (this.heuristicvalue + moves)- (second.heuristicvalue +second.moves);
            return difference;
        }
        public int move(){
            return moves;
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

    public static int[][] deepCopy(int[][] board){
        int[][] newCopy = new int[board.length][board.length];
        for(int i = 0; i< board.length; i++){
            for(int j = 0; j < board.length; j++){
                newCopy[i][j] = board[i][j];
            }
        }
        return newCopy;
    }
    
    public static String solvePuzzle(int[][] puzzle) {
        PriorityQueue<boardState> queue = new PriorityQueue<>();
        HashSet<String> visitedBoards = new HashSet<>();

        int h = heuristicCalculation(puzzle);
        queue.offer(new boardState(puzzle, h, "", 0));

        while(! queue.isEmpty()){
            boardState currentBoard = queue.poll();

            int[][] board = currentBoard.board;
            StringBuilder saveString = new StringBuilder(); 
            int x = -1; int y = -1;
            for(int i = 0; i< board.length; i++){
                for(int j = 0; j < board[0].length; j++){
                    int number = board[i][j];
                    saveString.append(number);
                    if(number == 0){
                        x = i;
                        y = j;
                    }
                }
            }
            String bString = saveString.toString();

            // add board if not visited before and skip board if seen 
            if(visitedBoards.contains(bString))
                continue; 
            
            visitedBoards.add(bString);
            

            if(currentBoard.heuristicvalue == 0){
                return currentBoard.path;
            }

            // Try moving in 4 directions
            // they move in terms of where the tiles slide 
            int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
            char[] moves = {'D','U','R','L'};

            for (int i = 0; i < 4; i++) {
                int newX = x + directions[i][0];
                int newY = y + directions[i][1];

                if (newX >= 0 && newX < puzzle.length && newY >= 0 && newY < puzzle[0].length) {
                    int[][] newBoard = deepCopy(currentBoard.board);
                    newBoard[x][y] = newBoard[newX][newY];
                    newBoard[newX][newY] = 0;

                    String newPath = currentBoard.path + moves[i];
                    int newManhattan = heuristicCalculation(newBoard);

                    queue.offer(new boardState(newBoard, newManhattan, newPath, currentBoard.move()+1));
                }
            }

        }

        return null;  // no solution found
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
        System.out.println("\nSolution:");
        System.out.println(solution);

        int[][] test = {{1,2,3},{7,4,5},{0,8,6}};
        System.out.println("test:");
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.printf("%4d", test[row][col]);
            }
            System.out.println();
        }
        solution = solvePuzzle(test);
        System.out.println("\nSolution:");
        System.out.println(solution);
        

        

        // int[][] puzzle1 = makePuzzle(size, 0);
        // for (int row = 0; row < size; row++) {
        //     for (int col = 0; col < size; col++) {
        //         System.out.printf("%4d", puzzle1[row][col]);
        //     }
        //     System.out.println();
        // }
        // System.out.println(Puzzle.heuristicCalculation(puzzle1));

    }
}
