import java.util.*;
// Scenario A — Robot Moving Through a Warehouse 

public class Main {
    static String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H"};
    static int[][] matrix = {
        {0, 1, 1, 0, 0, 0, 0, 0}, 
        {0, 0, 0, 1, 1, 0, 0, 0}, 
        {0, 0, 0, 0, 0, 1, 1, 0}, 
        {0, 0, 0, 0, 0, 0, 0, 0}, 
        {0, 0, 0, 0, 0, 0, 0, 1}, 
        {0, 0, 0, 0, 0, 0, 0, 0}, 
        {0, 0, 0, 0, 0, 0, 0, 0}, 
        {0, 0, 0, 0, 0, 0, 0, 0}  
    };

    static Map<String, List<String>> graph = new HashMap<>();

    static void buildGraph() {
        for (int i = 0; i < labels.length; i++) {
            List<String> neighbours = new ArrayList<>();
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    neighbours.add(labels[j]);
                }
            }
            graph.put(labels[i], neighbours);
        }
    }

    static List<String> dls(String u, String goal, int limit, List<String> path, List<String> expanded) {
        expanded.add(u);
        if (u.equals(goal)) return new ArrayList<>(path);
        if (limit == 0) return null;

        for (String v : graph.getOrDefault(u, Collections.emptyList())) {
            if (!path.contains(v)) {
                path.add(v);
                List<String> ans = dls(v, goal, limit - 1, path, expanded);
                if (ans != null) return ans;
                path.remove(path.size() - 1);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        buildGraph();
        String start = "A", goal = "H";
        List<String> solution = null;

        for (int limit = 0; limit <= 100; limit++) {
            List<String> expanded = new ArrayList<>();
            List<String> path = new ArrayList<>(Collections.singletonList(start));
            solution = dls(start, goal, limit, path, expanded);
            System.out.println("Limit: " + limit + " | Expanded: " + expanded);
            if (solution != null) {
                System.out.println("Goal found at depth " + limit + ": " + String.join(" -> ", solution));
                break;
            }
        }
    }
}