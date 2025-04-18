import java.util.*;
import java.io.*;

public class PrettyPrint {

    public static List<Integer> splitWords(int[] lengths, int L, SlackFunctor slackFunctor) {
        if (lengths == null || lengths.length == 0) {
            return Collections.emptyList();
        }
        
        int n = lengths.length;
        
        // dp[i] = minimum cost of formatting words[i...n-1]
        int[] dp = new int[n + 1];
        // next[i] = index to break after when starting at position i
        int[] next = new int[n + 1];
        
        // Base case: no cost to format an empty list
        dp[n] = 0;
        
        // Fill dp array from right to left (bottom-up)
        for (int start = n - 1; start >= 0; start--) {
            int lineLength = 0;
            dp[start] = Integer.MAX_VALUE;
            
            for (int end = start; end < n; end++) {
                int current = lengths[end];
                if(current > L){
                    return null;
                }
                // Add word length
                lineLength += current;
                // Add space if not the first word
                if (end > start) {
                    lineLength += 1;
                }
                
                // Break if exceeding line width
                if (lineLength > L) {
                    break;
                }
                
                // Calculate slack penalty
                int slack = L - lineLength;
                int penalty = slack * slack;
                
                // Calculate total cost including penalty
                int cost = penalty + dp[end + 1];
                
                // Update if we found a better solution
                if (cost < dp[start]) {
                    dp[start] = cost;
                    next[start] = end + 1;
                }
            }
            
            // Handle case where no valid break was found
            if (dp[start] == Integer.MAX_VALUE) {
                next[start] = start + 1;
                dp[start] = Integer.MAX_VALUE / 2; // High penalty but avoid overflow
            }
        }
        
        // Build result by following the optimal breaks
        List<Integer> result = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int nextBreak = next[i];
            result.add(nextBreak - 1); // Convert to 0-indexed end position
            i = nextBreak;
        }
        
        return result;
    }

    private static int computeMinimumCost(int startPos, int[] lengths, int lineWidth, Map<Integer, Integer> memo, int[] lineBreaks) {
    // Base case: no more words to process
    if (startPos >= lengths.length) {
        return 0;
    }
    
    // Return memoized result if available
    if (memo.containsKey(startPos)) {
        return memo.get(startPos);
    }
    
    int currentLineLength = 0;
    int minimumCost = Integer.MAX_VALUE;
    int optimalBreakPoint = -1;
    
    // Try different ending positions for current line
    for (int endPos = startPos; endPos < lengths.length; endPos++) {
        // Add current word length
        int wordAfter = lengths[endPos];
        if(wordAfter > lineWidth){
            return -1;
        }
        if (endPos == startPos) {
            currentLineLength = wordAfter;
        } else {
            // Add space + word length
            currentLineLength += 1 + wordAfter; 
        }
        
        // Stop if we exceed the line width
        if (currentLineLength > lineWidth) {
            break;
        }
        
        // Calculate slack penalty
        int slackSpace = lineWidth - currentLineLength;
        // Last line has no penalty (common in text formatting algorithms)
        int penalty = (endPos == lengths.length - 1) ? 0 : slackSpace * slackSpace;
        
        // Recursively calculate cost for the rest of the text
        int totalCost = penalty + computeMinimumCost(endPos + 1, lengths, lineWidth, memo, lineBreaks);
        
        // Update minimum if we found a better solution
        if (totalCost < minimumCost) {
            minimumCost = totalCost;
            optimalBreakPoint = endPos + 1;
        }
    }
    // Handle the case where no valid break was found (e.g., single word too long for line)
    if (optimalBreakPoint == -1) {
        // Force break after current word as a fallback
        optimalBreakPoint = startPos + 1;
        minimumCost = Integer.MAX_VALUE / 2;
    }

    
    // Store the optimal break point
    lineBreaks[startPos] = optimalBreakPoint;
    
    // Memoize the result
    memo.put(startPos, minimumCost);
    
    return minimumCost;
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