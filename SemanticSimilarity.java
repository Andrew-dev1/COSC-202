import java.util.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.text.StringCharacterIterator;

public class SemanticSimilarity
{

    /* Algorithm 1 implementation:
        
        Input: a sequence of sentences
        Output: a data structure that permits efficient similarity queries
            on pairs of words
            
        NOTE: The output is NOT RETURNED. This is a constructor.
        Instead, some instance variable(s) of this class are initialized
        using the data from the sentences.
    */
    public SemanticSimilarity(Iterable<String> sentences) {
        for(String sentence: sentences){
            // remove all duplicates since each word should only be counted once per sentence 
            String[] words = sentence.split(" ");
            HashSet<String> removeDuplicates = new HashSet<>();
            for(String word: words){
                removeDuplicates.add(word);
            }

            // added each of the word as keys in the outer hashmap and then put all of the connected 
            // words as keys for the inner hashmap and increment occurence per sentence
            for(String word: removeDuplicates){
                wordsMap.put(word, wordsMap.getOrDefault(word, new HashMap<String, Integer>()));
                for(String innerWord: removeDuplicates){
                    if(!word.equals(innerWord)){
                        wordsMap.get(word).put(innerWord, wordsMap.get(word).getOrDefault(innerWord,0)+1);                    }
                }
            }
        }
    }


    /* Algorithm 2 implementation
    
        Input: a pair of words
        Ouput: the cosine similarity of those words given the data
            initially provided to the constructor
    */
    public double similarity(String s1, String s2) {
        if(wordsMap.get(s2) == null || wordsMap.get(s1) == null){
            return -1.0; 
        }
        int squares1 = 0;
        int squares2 = 0;
        int sum = 0;
        HashMap<String,Integer> tempMap1 = wordsMap.get(s1);
        HashMap<String,Integer> tempMap2 = wordsMap.get(s2);

        /* iterating through the first key set to see if there are words that occur in the same sentence 
         * as both s1 and s2. if the key doesn't exist in map1 or map2, then it would have multiplied to 0
         * and contributed nothing to the sum. 
        */
        for(String key: tempMap1.keySet()){
            int value1 = tempMap1.get(key);
            int value2 = tempMap2.getOrDefault(key, 0);
            squares1 += (value1 *value1);
            sum += (value1 *value2); 

        }

        // calculates the sum of the squares of all counts for list for second word
        for(String key: tempMap2.keySet()){
            int value2 = tempMap2.get(key);
            squares2 += (value2 * value2);
        }

        return (double)sum / (Math.sqrt(squares2) * Math.sqrt(squares1));
    }


    /* Don't forget to add any instance variables for data structures
        that the class will need to initialize in the constructor (Algorithm 1)
    */
    
    private HashMap<String, HashMap<String,Integer>> wordsMap = new HashMap<>();
    
    
    
    
    /* EXAMPLE CODE FOR TESTING */
    
    public static void main(String[] args) {

        Scanner scan;

        if (args.length > 0 && !args[0].equals("-")) {
            // if an argument is provided, assume it's a filename, and use text from it
            try {
                scan = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.err.printf("%s: error: could not open %s for reading%n", MethodHandles.lookup().lookupClass().getSimpleName(), args[0]);
                System.exit(1);
                return;
            }
        } else {
            // if there's no argument, read from standard input
            scan = new Scanner(System.in);
            
        }

        // convert the input to sentences
        Iterable<String> sentences = parseText(scan);
        
        // print out the sentences as they will be passed into Algorithm 1
        for (String sentence: sentences) {
            System.out.println(sentence);
        }
        System.out.println();
        
        // run Algorithm 1
        SemanticSimilarity sim = new SemanticSimilarity(sentences);
        
        // run "javac SemanticSimilarity.java"  and then "java SemanticSimilarity text.txt"
        System.out.println(sim.similarity("this", "that"));
        System.out.println(sim.similarity("those", "these"));
        System.out.println(sim.similarity("this", "is"));
        System.out.println(sim.similarity("what", "why"));

    }

    

    // Characters that are used by the parser helper function
    private static String endPunct = "[\\Q.!?\\E]";
    private static String otherPunct = "[\\p{Punct}&&[^\\Q.!?\\E]]";

    // Helper function so that any text can be converted to the input expected
    // Input: An iterator that returns strings, e.g., a Scanner
    // Output: A sequence of sentences
    public static Iterable<String> parseText(Iterator<String> text) {
        ArrayList<String> sentences = new ArrayList<String>();

        StringBuilder sentence = null;
        while (text.hasNext()) {
            if (sentence == null) {
                sentence = new StringBuilder();
            }
            String word = text.next().toLowerCase().replaceAll(otherPunct, "");
            if (word.endsWith(".") || word.endsWith("!") || word.endsWith("?")) {
                word = word.replaceAll(endPunct, "");
                if (sentence.length() > 0) {
                    sentence.append(" ");
                }
                sentence.append(word);
                sentences.add(sentence.toString());
                sentence = null;
            }
            else {
                if (sentence.length() > 0) {
                    sentence.append(" ");
                }
                sentence.append(word);
            }
        }
        if (sentence != null) {
            sentences.add(sentence.toString());
        }

        return sentences;
    }

}