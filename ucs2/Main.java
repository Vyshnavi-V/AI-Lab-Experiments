import java.util.*;
// Scenario B — Campus Energy-Aware Robot
public class Main {
    static class NodeCost implements Comparable<NodeCost> {
        int cost;
        String node;

        NodeCost(int cost, String node) {
            this.cost = cost;
            this.node = node;
        }

        @Override
        public int compareTo(NodeCost other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    private static final Map<String, List<int[]>> graph = new HashMap<>();
    private static final String[] labels = {"A", "B", "C", "D", "E", "F", "G"};
    private static final int[][] matrix = {
        {0, 1, 4, 0, 0, 0, 0},  // A
        {1, 0, 1, 0, 0, 0, 10}, // B
        {4, 1, 0, 1, 0, 0, 0},  // C
        {0, 0, 1, 0, 1, 0, 1},  // D
        {0, 0, 0, 1, 0, 1, 1},  // E
        {0, 0, 0, 0, 1, 0, 1},  // F
        {0, 10, 0, 1, 1, 1, 0}  // G
    };

    static void buildGraph() {
        for (int i = 0; i < labels.length; i++) {
            graph.put(labels[i], new ArrayList<>());
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] > 0) {
                    graph.get(labels[i]).add(new int[]{j, matrix[i][j]});
                }
            }
        }
    }

    public static void runUCS(String start, String goal) {
        PriorityQueue<NodeCost> pq = new PriorityQueue<>();
        Map<String, Integer> bestCost = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        List<String> expanded = new ArrayList<>();
        int staleEntries = 0;

        pq.add(new NodeCost(0, start));
        bestCost.put(start, 0);
        parent.put(start, null);

        while (!pq.isEmpty()) {
            NodeCost current = pq.poll();
            String u = current.node;
            int cost = current.cost;

            if (cost != bestCost.getOrDefault(u, Integer.MAX_VALUE)) {
                staleEntries++;
                continue;
            }

            expanded.add(u);
            if (u.equals(goal)) break;

            for (int[] edge : graph.getOrDefault(u, Collections.emptyList())) {
                String v = labels[edge[0]];
                int weight = edge[1];
                int newCost = cost + weight;

                if (newCost < bestCost.getOrDefault(v, Integer.MAX_VALUE)) {
                    bestCost.put(v, newCost);
                    parent.put(v, u);
                    pq.add(new NodeCost(newCost, v));
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String curr = goal; curr != null; curr = parent.get(curr)) {
            path.add(0, curr);
        }

        System.out.println("UCS Path: " + String.join(" -> ", path));
        System.out.println("Total Energy Consumed (Cost): " + bestCost.get(goal) + " units");
        System.out.println("Nodes Expanded: " + expanded);
        System.out.println("Stale Entries Skipped: " + staleEntries);
    }

    public static void main(String[] args) {
        buildGraph();
        runUCS("A", "G");
    }
}