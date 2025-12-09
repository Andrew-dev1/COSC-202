import java.util.*;
import java.io.*;

public class PrettyPrint {

    public static List<Integer> splitWords(int[] lengths, int L, SlackFunctor sf) {
        if (lengths == null || lengths.length == 0) {
            return Collections.emptyList();
        }
        
        int n = lengths.length;
        
        // dp[i] = minimum cost of formatting words[i...n-1]
        double[] slacks = new double[n + 1];
        // next[i] = index to break after when starting at position i
        int[] next = new int[n + 1];
        
        // Base case: no cost to format an empty list
        slacks[n] = 0;
        
        // Fill dp array from right to left (bottom-up)
        for (int start = n - 1; start >= 0; start--) {
            int lineLength = 0;
            slacks[start] = Integer.MAX_VALUE;
            
            for (int end = start; end < n; end++) {
                // if a word is too long, algorithm fails
                int current = lengths[end];
                if(current > L){
                    return null;
                }
                
                // Add word length and space if not the first word
                lineLength += current;
                if (end > start) {
                    lineLength += 1;
                }
                 
                if (lineLength > L) {
                    break;
                }
                
                // Calculate slack 
                int slack = L - lineLength;
                double squared = slack*slack; // SlackFunctor doesn't work
                
                // Calculate total cost of slack from current line and lines after 
                double totalCost = squared + slacks[end + 1];
                
                // Update if we found a better solution if there is enough space 
                // in current line to add the word from line before, add it and increment break point
                if (totalCost < slacks[start]) {
                    slacks[start] = totalCost;
                    next[start] = end + 1;
                }
            }
        
            
        }
        
        // Build result by following the optimal breaks 
        // array next points to subsequent word to break at
        List<Integer> result = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int nextBreak = next[i];
            result.add(nextBreak - 1); // Convert to 0-indexed end position
            i = nextBreak;
        }
        
        return result;
    }


    public static String help_message() {
        return
            "Usage: java PrettyPrint line_length [inputfile] [outputfile]\n" +
            "  line_length (required): an integer specifying the maximum length for a line\n" +
            "  inputfile (optional): a file from which to read the input text (stdin if a hyphen or omitted)\n" +
            "  outputfile (optional): a file in which to store the output text (stdout if omitted)"
            ;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: required argument line_length missing");
            System.err.println(help_message());
            System.exit(1);
            return;
        }

        int line_length;
        try {
            line_length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Error: could not find integer argument line_length");
            System.err.println(help_message());
            System.exit(2);
            return;
        }

        Scanner input = null;
        PrintStream output = null;
        if (args.length < 2) {
            input = new Scanner(System.in);
        }
        else if (args.length >= 2) {
            if (args[1].equals("-")) {
                input = new Scanner(System.in);
            } else {
                try {
                    input = new Scanner(new File(args[1]));
                } catch (FileNotFoundException e) {
                    System.err.println("Error: could not open input file");
                    System.err.println(help_message());
                    System.exit(3);
                    return;
                }
            }
        }

        if (args.length >= 3) {
            try {
                output = new PrintStream(args[2]);
            } catch (FileNotFoundException e) {
                System.err.println("Error: could not open output file");
                System.err.println(help_message());
                input.close();
                System.exit(4);
                return;
            }
        } else {
            output = System.out;
        }

        ArrayList<String> words = new ArrayList<String>();
        while (input.hasNext()) {
            words.add(input.next());
        }
        input.close();

        int[] lengths = new int[words.size()];
        for (int i = 0; i < words.size(); i++) {
            lengths[i] = words.get(i).length();
        }
        
        List<Integer> breaks = splitWords(lengths, line_length,
            new SlackFunctor() {
                public double f(int slack) { return slack * slack; }
            });

        if (breaks != null) {
            int current_word = 0;
            for (int next_break : breaks) {
                while (current_word < next_break) {
                    output.print(words.get(current_word++));
                    output.print(" ");
                }
                output.print(words.get(current_word++));
                output.println();
            }

            output.close();
            System.exit(0);
            return;
    
        } else {
            System.err.println("Error: formatting impossible; an input word exceeds line length");
            output.close();
            System.exit(5);
            return;
        }
    }
}