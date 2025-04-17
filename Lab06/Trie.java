import java.io.*;
import java.util.*;

public class Trie {

    // node class
    private static class TrieNode {

        boolean isWord;
        HashMap<Character, TrieNode> children = new HashMap<
            Character,  
            TrieNode
        >();
    }

    // root node
    private TrieNode root = new TrieNode();

    // method to implement
    public HashMap<String, Integer> suggestions(String target, int dist) {
        if(target == null || target.length() == 0){
            return null; 
        }
        HashMap<String, Integer> words = new HashMap<String, Integer>();
        // Start recursive traversal from the root
        suggestionsHelper1(root, "", 0, target, target.length(), dist, dist, words);

        return words;
    }
    
    private void suggestionsHelper1(TrieNode node, String currentWord, int currIndex, String target, int targetLength, int mistakesLeft, int maxDist, HashMap<String, Integer> results) {
        if(mistakesLeft <0){
            return;
        }


        if (node.isWord && currIndex == target.length()) {
            results.put(currentWord, Math.min(results.getOrDefault(currentWord, Integer.MAX_VALUE), maxDist - mistakesLeft));
        }

        // next letters 
        for(Character c: node.children.keySet()){
            TrieNode newNode = node.children.get(c);

            if (currIndex < target.length()) {
                if(c.equals(target.charAt(currIndex))){
                    // matched
                    suggestionsHelper1(newNode, currentWord+ c, currIndex+1, target, targetLength, mistakesLeft, maxDist, results);
                }
                else {
                    //substitution - goes to other Nodes with one less on maxDist
                    suggestionsHelper1(newNode, currentWord+ c, currIndex+1, target, targetLength, mistakesLeft-1, maxDist, results);
                }
            }else{
                // insertion- extra char in between, so one less on maxDist and adds letter
                suggestionsHelper1(newNode, currentWord + c, currIndex, target, targetLength, mistakesLeft -1, maxDist, results);
            }
        }

        //deletion- goes forward to next Node with one less on the maxDist 
        //can increment forward on the target to delete 
        if (currIndex < target.length()) {
            suggestionsHelper1(node, currentWord, currIndex+1, target, targetLength, mistakesLeft-1, maxDist, results);
        }

    }

    

    // method to add a string
    public boolean add(String s) {
        s = s.trim().toLowerCase();

        TrieNode current = root;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                TrieNode child = current.children.get(c);
                if (child == null) {
                    child = new TrieNode();
                    current.children.put(c, child);
                }
                current = child;
            }
        }

        if (current.isWord) return false;

        current.isWord = true;
        return true;
    }

    // method to check if a string has been added
    public boolean contains(String s) {
        s = s.trim().toLowerCase();

        TrieNode current = root;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                TrieNode child = current.children.get(c);
                if (child == null) {
                    return false;
                }
                current = child;
            }
        }

        return current.isWord;
    }

    // empty constructor
    public Trie() {
        super();
    }

    // constructor to add words from a stream, like standard input
    public Trie(InputStream source) {
        Scanner scan = new Scanner(source);
        addWords(scan);
        scan.close();
    }

    // constructor to add words from a file
    public Trie(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        addWords(scan);
        scan.close();
    }

    // helper function to add words from a scanner
    private void addWords(Scanner scan) {
        while (scan.hasNext()) {
            add(scan.next());
        }
    }

    // main function for testing
    public static void main(String[] args) {
        Trie dictionary;

        // the following section of code is set up
        // to read the filename from a command-line argument
        //
        // if you prefer to hard-code a filename for testing
        // or add words to the Trie manually, comment this section

        if (args.length > 0) {
            try {
                dictionary = new Trie(args[0]);
            } catch (FileNotFoundException e) {
                System.err.printf(
                    "could not open file %s for reading\n",
                    args[0]
                );
                return;
            }
        } else {
            dictionary = new Trie(System.in);
        }

        // here is an example of hard-coding the filename
        // making it easier to use the "Run" button in VS Code

        /*
        dictionary = new Trie("ospd.txt");
        */

        // here is an example of adding words to the Trie manually

        /*
        dictionary = new Trie();
        dictionary.add("cat");
        dictionary.add("car");
        dictionary.add("hat");
        */

        // here is an example of calling functions to test the Trie
        
        System.out.println(dictionary.root);
        System.out.println(dictionary.contains("cat"));
        System.out.println(dictionary.suggestions("zat", 1));
        System.out.println(dictionary.suggestions("cat", 1));
        
        



    }
}
