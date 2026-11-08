import java.util.*;
// Scenario A — City Metro Navigation
public class Main {
    static String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H"};
    static int[][] matrix = {
        {0, 1, 1, 0, 0, 0, 0, 0}, // A
        {1, 0, 1, 1, 0, 0, 0, 0}, // B
        {1, 1, 0, 0, 1, 0, 0, 0}, // C
        {0, 1, 0, 0, 0, 1, 1, 0}, // D
        {0, 0, 1, 0, 0, 0, 1, 0}, // E
        {0, 0, 0, 1, 0, 0, 0, 1}, // F
        {0, 0, 0, 1, 1, 0, 0, 1}, // G
        {0, 0, 0, 0, 0, 1, 1, 0}  // H
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

    static List<String> reconstruct(String meet, Map<String, String> pf, Map<String, String> pb) {
        List<String> left = new ArrayList<>();
        String x = meet;
        while (x != null) {
            left.add(x);
            x = pf.get(x);
        }
        Collections.reverse(left);

        List<String> right = new ArrayList<>();
        x = pb.get(meet);
        while (x != null) {
            right.add(x);
            x = pb.get(x);
        }
        left.addAll(right);
        return left;
    }

    static String expandLevel(Queue<String> q, Map<String, String> thisParent, Map<String, String> otherParent, List<String> expansions) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            String u = q.poll();
            expansions.add(u);
            if (otherParent.containsKey(u)) return u;

            for (String v : graph.getOrDefault(u, Collections.emptyList())) {
                if (!thisParent.containsKey(v)) {
                    thisParent.put(v, u);
                    q.add(v);
                }
                if (otherParent.containsKey(v)) return v;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        buildGraph();
        String start = "A", goal = "H";
        Queue<String> qf = new LinkedList<>(), qb = new LinkedList<>();
        Map<String, String> pf = new HashMap<>(), pb = new HashMap<>();
        List<String> fExpansions = new ArrayList<>(), bExpansions = new ArrayList<>();

        qf.add(start); pf.put(start, null);
        qb.add(goal);  pb.put(goal, null);

        String meet = null;
        while (!qf.isEmpty() && !qb.isEmpty()) {
            meet = expandLevel(qf, pf, pb, fExpansions);
            if (meet != null) break;

            meet = expandLevel(qb, pb, pf, bExpansions);
            if (meet != null) break;
        }

        if (meet != null) {
            List<String> path = reconstruct(meet, pf, pb);
            System.out.println("Meeting State: " + meet);
            System.out.println("Path: " + String.join(" -> ", path));
            System.out.println("Path Length: " + (path.size() - 1));
            System.out.println("Forward Expansions: " + fExpansions);
            System.out.println("Backward Expansions: " + bExpansions);
        }
    }
}