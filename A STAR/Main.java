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

    static class NodeEntry implements Comparable<NodeEntry> {
        String name;
        int g, h, f;

        NodeEntry(String name, int g, int h) {
            this.name = name;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        @Override
        public int compareTo(NodeEntry o) {
            if (this.f != o.f) {
                return Integer.compare(this.f, o.f);
            }
            if (this.h != o.h) {
                return Integer.compare(this.h, o.h);
            }
            return this.name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return name + "(g=" + g + ", h=" + h + ", f=" + f + ")";
        }
    }

    static void addEdge(Map<String, List<Edge>> graph, String from, String to, int cost) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, cost));
    }

    static void runAStar(String name, Map<String, List<Edge>> graph, Map<String, Integer> hMap, String start, String goal) {
        System.out.println("Running A* for: " + name);
        System.out.println("===============================================================");

        PriorityQueue<NodeEntry> pq = new PriorityQueue<>();
        Map<String, Integer> bestG = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        List<String> expansionTrace = new ArrayList<>();

        bestG.put(start, 0);
        pq.add(new NodeEntry(start, 0, hMap.get(start)));

        int step = 1;

        while (!pq.isEmpty()) {
            NodeEntry curr = pq.poll();
            String u = curr.name;

            if (curr.g > bestG.getOrDefault(u, Integer.MAX_VALUE)) {
                continue;
            }

            expansionTrace.add(u);
            System.out.println("Step " + step + " | Expanded: " + u + " [g=" + curr.g + ", h=" + curr.h + ", f=" + curr.f + "]");

            if (u.equals(goal)) {
                System.out.println("  -> Goal reached!");
                System.out.println("  -> Frontier (OPEN): " + getCleanFrontier(pq, bestG));
                break;
            }

            List<Edge> neighbors = new ArrayList<>(graph.getOrDefault(u, Collections.emptyList()));
            neighbors.sort(Comparator.comparing(e -> e.to));

            for (Edge edge : neighbors) {
                String v = edge.to;
                int newG = curr.g + edge.cost;
                if (newG < bestG.getOrDefault(v, Integer.MAX_VALUE)) {
                    bestG.put(v, newG);
                    parent.put(v, u);
                    pq.add(new NodeEntry(v, newG, hMap.get(v)));
                }
            }

            System.out.println("  -> Frontier (OPEN) after step: " + getCleanFrontier(pq, bestG));
            step++;
        }

        List<String> path = new ArrayList<>();
        String curr = goal;
        while (curr != null) {
            path.add(curr);
            curr = parent.get(curr);
        }
        Collections.reverse(path);

        System.out.println();
        System.out.println("Expansion Sequence : " + expansionTrace);
        System.out.println("Nodes Expanded     : " + expansionTrace.size());
        System.out.println("Final Path         : " + String.join(" -> ", path));
        System.out.println("Total Path Cost    : " + bestG.get(goal));
        System.out.println();
    }

    private static List<NodeEntry> getCleanFrontier(PriorityQueue<NodeEntry> pq, Map<String, Integer> bestG) {
        List<NodeEntry> list = new ArrayList<>();
        for (NodeEntry entry : pq) {
            if (entry.g == bestG.getOrDefault(entry.name, Integer.MAX_VALUE)) {
                list.add(entry);
            }
        }
        Collections.sort(list);
        return list;
    }

    public static void main(String[] args) {
        Map<String, List<Edge>> gA = new HashMap<>();
        for (String s : Arrays.asList("S", "A", "B", "C", "D", "E", "F", "G")) {
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

        runAStar("Scenario A (Emergency Medical Drone)", gA, hA, "S", "G");

        Map<String, List<Edge>> gB = new HashMap<>();
        for (String s : Arrays.asList("S", "A", "B", "C", "D", "E", "F", "G")) {
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

        runAStar("Scenario B (Autonomous Campus Robot)", gB, hB, "S", "G");
    }
}