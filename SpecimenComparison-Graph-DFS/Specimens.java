import java.util.*;

public class Specimens {

    /* return either:
        
        (1) if the input comparisons are inconsistent, then return one or more pairs from diffPairs that are inconsistent with the data from samePairs
        
        (2) if the input comparisons are consistent, then return a collection of pairs involving the fewest number of specimens such that outstanding ambiguities among species are captured
    */
    public static Collection<Pair> processComparisons(
        int n,   /* number of specimens */
        Collection<Pair> samePairs,     /* pairs where specimens are judged to be the same species */
        Collection<Pair> diffPairs      /* pairs where specimens are judged to be different species */
        ){

    // can be changed 
    if(samePairs.isEmpty() && diffPairs.isEmpty() && n == 0){ return new ArrayList<>(); }

    HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
    for(Pair current: samePairs){
        int x = current.x();
        int y = current.y();
        if(graph.get(x) == null){
            graph.put(x, new ArrayList<>());
        }
        if(graph.get(y) == null){
            graph.put(y, new ArrayList<>());
        }
        graph.get(x).add(y);
        graph.get(y).add(x);
    }
    printGraph(graph);

    // group into components using dfs
    HashMap<Integer, Integer> groups = new HashMap<>();
    int groupId = 0;
    for (int specimen : graph.keySet()) {
        if (!groups.containsKey(specimen)) {
            dfs(specimen, graph, groups, groupId++);
        }
    }
    printGroups(groups);
    

    // Assign group IDs to specimens that weren't compared 
    for (int i = 0; i < n; i++) {
        if (!groups.containsKey(i)) {
            groups.put(i, groupId++);
        }
    }

    // find list of contradictions bc apparently we don't just want one pair 
    ArrayList<Pair> contradictions = new ArrayList<>();
    for(Pair diff: diffPairs){
        if(groups.get(diff.x())== groups.get(diff.y())){
            contradictions.add(diff);
        }
    }
    if(!contradictions.isEmpty()) return contradictions;

    // finding the remaining pairs to compare 
    // takes the first number of each group and stores as a comparison factor later
    HashMap<Integer, Integer> groupReps = new HashMap<>();
    for (int i = 0; i < n; i++) {
        int group = groups.get(i); 
        if (!groupReps.containsKey(group)) {
            groupReps.put(group, i);
        }
    }
    printGroups(groupReps);

    // creates a set of groups that shouldn't be added from the diffPairs
    HashSet<Pair> comparedGroupPairs = new HashSet<>();
    for (Pair diff : diffPairs) {
        int group1 = groups.get(diff.x());
        int group2 = groups.get(diff.y());
        // Ensure smaller group ID is first to maintain consistency
        if (group1 > group2) {
            int temp = group1;
            group1 = group2;
            group2 = temp;
        }
        System.out.printf("%d %d\n", group1, group2);
        comparedGroupPairs.add(new Pair(group1, group2));
    }

    // creates a final list of specimens to compare
    // would have to compare up to (number of components) squared
    List<Pair> requiredComparisons = new ArrayList<>();
    List<Integer> groupIds = new ArrayList<>(groupReps.keySet());
    
    for (int i = 0; i < groupIds.size(); i++) {
        for (int j = i + 1; j < groupIds.size(); j++) {
            int group1 = groupIds.get(i);
            int group2 = groupIds.get(j);
            
            // Check if these two groups were compared to be different yet 
            Pair groupPair = new Pair(group1, group2);
            if (!comparedGroupPairs.contains(groupPair)) {
                // If not compared, add a comparison between representatives
                int rep1 = groupReps.get(group1);
                int rep2 = groupReps.get(group2);
                requiredComparisons.add(new Pair(rep1, rep2));
            }
        }
    }


    return requiredComparisons;
    }


    private static void dfs(int specimen, HashMap<Integer, ArrayList<Integer>> graph,
                      Map<Integer, Integer> groups, int id) {
        groups.put(specimen, id);
        if(graph.containsKey(specimen)){ // if there are neighbors, keep traversing through and add to groups
            for (int neighbor : graph.get(specimen)) {
                if (!groups.containsKey(neighbor)) {
                    dfs(neighbor, graph, groups, id);
                }
            }
        }
    }

    // prints the adjacency map
    private static void printGraph(HashMap<Integer, ArrayList<Integer>> graph ){
        for(Integer key: graph.keySet()){
            String vals = graph.get(key).toString();
            System.out.println(key + ": "+ vals);
        }
    }
    
    // reverses the map to print each specimen by group
    private static void printGroups(HashMap<Integer, Integer> groupMap ){
        Map<Integer, List<Integer>> groups = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : groupMap.entrySet()) {
            int node = entry.getKey();
            int groupId = entry.getValue();
            
            groups.computeIfAbsent(groupId, k -> new ArrayList<>()).add(node); // line given by AI to reduce code
        }

        // Print all groups
        for (Map.Entry<Integer, List<Integer>> entry : groups.entrySet()) {
            System.out.println("Group " + entry.getKey() + ": " + entry.getValue());
        }
    }


    public static void main(String[] args) {
        // all similarities
        // ArrayList<Pair> a1 = new ArrayList<>();
        // a1.addAll(Arrays.asList(new Pair (0, 1), new Pair (1, 2), new Pair (2, 3), new Pair (0, 3), 
        //     new Pair (4, 5), new Pair (5, 6), new Pair (6, 7), new Pair (4, 7)));
        // System.out.println(processComparisons(8, a1, new ArrayList<Pair>()));

        // diffs and sims
        ArrayList<Pair> sims1 = new ArrayList<>();
        sims1.addAll(Arrays.asList(new Pair(0, 1), new Pair(1, 2), new Pair(0, 2), new Pair(3, 4), 
            new Pair(4, 5), new Pair(3, 5), new Pair(6, 7), new Pair(7, 8), new Pair(6, 8), 
            new Pair(9, 10), new Pair(10, 11), new Pair(9, 11)));

        ArrayList<Pair> diffs1 = new ArrayList<>();
        diffs1.addAll(Arrays.asList(new Pair(0, 3), new Pair(4, 7), new Pair(8, 11), new Pair(2, 9)));
        System.out.println(processComparisons(8, sims1, diffs1));

    }

}