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
        int h;

        NodeEntry(String name, int h) {
            this.name = name;
            this.h = h;
        }

        @Override
        public int compareTo(NodeEntry o) {
            if (this.h != o.h) {
                return Integer.compare(this.h, o.h);
            }
            return this.name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return name + "(h=" + h + ")";
        }
    }

    static void addEdge(Map<String, List<Edge>> graph, String from, String to, int cost) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, cost));
    }

    static void runGBFS(String scenarioName, Map<String, List<Edge>> graph, Map<String, Integer> hMap, String start, String goal) {
        System.out.println("GBFS: " + scenarioName);

        PriorityQueue<NodeEntry> pq = new PriorityQueue<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> expanded = new HashSet<>();
        List<String> expansionSequence = new ArrayList<>();

        pq.add(new NodeEntry(start, hMap.get(start)));
        parent.put(start, null);

        int step = 1;

        while (!pq.isEmpty()) {
            List<NodeEntry> frontierList = new ArrayList<>(pq);
            Collections.sort(frontierList);
            System.out.println("Step " + step + " | Frontier (OPEN): " + frontierList);

            NodeEntry curr = pq.poll();
            String u = curr.name;

            if (expanded.contains(u)) {
                continue;
            }

            expanded.add(u);
            expansionSequence.add(u);
            System.out.println("  -> Expanded: " + u + " | h(" + u + ") = " + curr.h);

            if (u.equals(goal)) {
                System.out.println("  -> Goal reached!");
                break;
            }

            List<Edge> neighbors = new ArrayList<>(graph.getOrDefault(u, Collections.emptyList()));
            neighbors.sort(Comparator.comparing(e -> e.to));

            for (Edge edge : neighbors) {
                String v = edge.to;
                if (!expanded.contains(v)) {
                    if (!parent.containsKey(v)) {
                        parent.put(v, u);
                    }
                    pq.add(new NodeEntry(v, hMap.get(v)));
                }
            }
            step++;
        }

        List<String> path = new ArrayList<>();
        String curr = goal;
        while (curr != null) {
            path.add(curr);
            curr = parent.get(curr);
        }
        Collections.reverse(path);

        int totalCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String u = path.get(i);
            String v = path.get(i + 1);
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                if (e.to.equals(v)) {
                    totalCost += e.cost;
                    break;
                }
            }
        }

        System.out.println("Expansion Sequence : " + expansionSequence);
        System.out.println("Nodes Expanded     : " + expansionSequence.size());
        System.out.println("Final Path         : " + String.join(" -> ", path));
        System.out.println("Total Path Cost    : " + totalCost);
        System.out.println();
    }

    public static void main(String[] args) {
        Map<String, List<Edge>> gA = new HashMap<>();
        for (String node : Arrays.asList("S", "A", "B", "C", "D", "E", "F", "G")) {
            gA.put(node, new ArrayList<>());
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

        runGBFS("Scenario A (Emergency Medical Drone Routing)", gA, hA, "S", "G");

        Map<String, List<Edge>> gB = new HashMap<>();
        for (String node : Arrays.asList("S", "A", "B", "C", "D", "E", "F", "G")) {
            gB.put(node, new ArrayList<>());
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

        runGBFS("Scenario B (Autonomous Campus Robot Navigation)", gB, hB, "S", "G");
    }
}