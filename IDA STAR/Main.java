import java.util.*;

public class Main {

    static class Edge {
        String to;
        int cost;

        Edge(String to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static final int FOUND = -1;
    static final int INF = 1_000_000;

    static void addEdge(Map<String, List<Edge>> graph, String from, String to, int cost) {
        graph.get(from).add(new Edge(to, cost));
    }

    static int dfs(String u, int g, int threshold, String goal,
                   Map<String, List<Edge>> graph, Map<String, Integer> hMap,
                   List<String> path, List<String> traversal, Set<Integer> prunedF) {
        traversal.add(u);
        int f = g + hMap.get(u);
        if (f > threshold) {
            prunedF.add(f);
            return f;
        }
        if (u.equals(goal)) {
            return FOUND;
        }

        int min = INF;
        for (Edge edge : graph.getOrDefault(u, Collections.emptyList())) {
            String v = edge.to;
            if (!path.contains(v)) {
                path.add(v);
                int t = dfs(v, g + edge.cost, threshold, goal, graph, hMap, path, traversal, prunedF);
                if (t == FOUND) {
                    return FOUND;
                }
                min = Math.min(min, t);
                path.remove(path.size() - 1);
            }
        }
        return min;
    }

    static void runIDAStar(String name, Map<String, List<Edge>> graph, Map<String, Integer> hMap, String start, String goal) {
        
        System.out.println("Running IDA* for: " + name);
       

        int threshold = hMap.get(start);
        List<String> path = new ArrayList<>();
        path.add(start);

        int iteration = 1;
        int totalNodeVisits = 0;

        while (true) {
            List<String> traversal = new ArrayList<>();
            Set<Integer> prunedF = new TreeSet<>();
            int t = dfs(start, 0, threshold, goal, graph, hMap, path, traversal, prunedF);

            totalNodeVisits += traversal.size();
            System.out.printf("Iteration %d: Threshold = %2d | Visited = %-26s | Pruned f-values = %s\n",
                    iteration++, threshold, traversal, prunedF);

            if (t == FOUND) {
                int totalCost = 0;
                for (int i = 0; i < path.size() - 1; i++) {
                    String u = path.get(i);
                    String v = path.get(i + 1);
                    for (Edge e : graph.get(u)) {
                        if (e.to.equals(v)) {
                            totalCost += e.cost;
                            break;
                        }
                    }
                }
                System.out.println("Goal found!");
                System.out.println("Final Path         : " + String.join(" -> ", path));
                System.out.println("Total Path Cost    : " + totalCost);
                System.out.println("Total State Visits : " + totalNodeVisits);
                System.out.println();
                break;
            }
            if (t >= INF) {
                System.out.println("No solution found.");
                break;
            }
            threshold = t;
        }
    }

    public static void main(String[] args) {
        // --- Scenario A ---
        List<String> nodesA = Arrays.asList("S", "A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Edge>> gA = new HashMap<>();
        for (String s : nodesA) {
            gA.put(s, new ArrayList<>());
        }

        addEdge(gA, "S", "A", 2);
        addEdge(gA, "S", "B", 5);
        addEdge(gA, "S", "C", 4);
        addEdge(gA, "A", "D", 5);
        addEdge(gA, "A", "E", 2);
        addEdge(gA, "B", "D", 1);
        addEdge(gA, "B", "F", 2);
        addEdge(gA, "C", "E", 2);
        addEdge(gA, "C", "F", 5);
        addEdge(gA, "D", "G", 4);
        addEdge(gA, "E", "G", 5);
        addEdge(gA, "F", "G", 2);

        Map<String, Integer> hA = new HashMap<>();
        hA.put("S", 7);
        hA.put("A", 6);
        hA.put("B", 3);
        hA.put("C", 5);
        hA.put("D", 4);
        hA.put("E", 4);
        hA.put("F", 2);
        hA.put("G", 0);

        runIDAStar("Scenario A (Emergency Medical Drone)", gA, hA, "S", "G");

        // --- Scenario B ---
        List<String> nodesB = Arrays.asList("S", "A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Edge>> gB = new HashMap<>();
        for (String s : nodesB) {
            gB.put(s, new ArrayList<>());
        }

        addEdge(gB, "S", "A", 1);
        addEdge(gB, "S", "B", 4);
        addEdge(gB, "S", "C", 2);
        addEdge(gB, "A", "D", 7);
        addEdge(gB, "A", "E", 2);
        addEdge(gB, "B", "D", 2);
        addEdge(gB, "B", "F", 3);
        addEdge(gB, "C", "E", 5);
        addEdge(gB, "C", "F", 2);
        addEdge(gB, "D", "G", 2);
        addEdge(gB, "E", "G", 4);
        addEdge(gB, "F", "G", 3);

        Map<String, Integer> hB = new HashMap<>();
        hB.put("S", 6);
        hB.put("A", 5);
        hB.put("B", 4);
        hB.put("C", 4);
        hB.put("D", 2);
        hB.put("E", 3);
        hB.put("F", 2);
        hB.put("G", 0);

        runIDAStar("Scenario B (Autonomous Campus Robot)", gB, hB, "S", "G");
    }
}