import java.util.*;

public class Main {

    static final int N = 20;
    static final int POP_SIZE = 30;
    static final int GENERATIONS = 100;
    static final double CROSSOVER_RATE = 0.8;
    static final double MUTATION_RATE = 0.02;
    static final int ELITE_COUNT = 2;

    static Random random = new Random(42);

    static int[] randomChromosome() {
        int[] ch = new int[N];
        for (int i = 0; i < N; i++) {
            ch[i] = random.nextInt(2);
        }
        return ch;
    }

    static int fitness(int[] ch) {
        int sum = 0;
        for (int gene : ch) {
            sum += gene;
        }
        return sum;
    }

    static int[] tournamentSelection(List<int[]> population, int k) {
        List<int[]> candidates = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            candidates.add(population.get(random.nextInt(population.size())));
        }
        candidates.sort((a, b) -> Integer.compare(fitness(b), fitness(a)));
        return candidates.get(0).clone();
    }

    static List<int[]> onePointCrossover(int[] p1, int[] p2, double crossoverRate) {
        if (random.nextDouble() > crossoverRate) {
            return Arrays.asList(p1.clone(), p2.clone());
        }
        int point = 1 + random.nextInt(N - 1);
        int[] c1 = new int[N];
        int[] c2 = new int[N];

        for (int i = 0; i < point; i++) {
            c1[i] = p1[i];
            c2[i] = p2[i];
        }
        for (int i = point; i < N; i++) {
            c1[i] = p2[i];
            c2[i] = p1[i];
        }
        return Arrays.asList(c1, c2);
    }

    static int[] mutate(int[] ch, double mutationRate) {
        int[] child = ch.clone();
        for (int i = 0; i < N; i++) {
            if (random.nextDouble() < mutationRate) {
                child[i] = 1 - child[i];
            }
        }
        return child;
    }

    static void geneticAlgorithm() {
        long startTime = System.nanoTime();
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < POP_SIZE; i++) {
            population.add(randomChromosome());
        }

        int[] bestOverall = null;
        int bestFitnessOverall = -1;
        int generationConverged = GENERATIONS;

        for (int generation = 0; generation < GENERATIONS; generation++) {
            population.sort((a, b) -> Integer.compare(fitness(b), fitness(a)));

            int[] best = population.get(0);
            int bestFit = fitness(best);
            double avgFit = population.stream().mapToInt(Main::fitness).average().orElse(0.0);

            if (generation == 0 || generation == 10 || generation == 20 || generation == 40 ||
                generation == 60 || generation == 80 || generation == 99 || bestFit == N) {
                System.out.printf("Gen %3d | Best Fitness: %2d | Avg Fitness: %5.2f | Best: %s\n",
                        generation, bestFit, avgFit, Arrays.toString(best));
            }

            if (bestFit == N) {
                bestOverall = best;
                bestFitnessOverall = bestFit;
                generationConverged = generation + 1;
                break;
            }

            List<int[]> nextPopulation = new ArrayList<>();
            for (int i = 0; i < ELITE_COUNT && i < population.size(); i++) {
                nextPopulation.add(population.get(i).clone());
            }

            while (nextPopulation.size() < POP_SIZE) {
                int[] p1 = tournamentSelection(population, 3);
                int[] p2 = tournamentSelection(population, 3);
                List<int[]> children = onePointCrossover(p1, p2, CROSSOVER_RATE);
                nextPopulation.add(mutate(children.get(0), MUTATION_RATE));
                if (nextPopulation.size() < POP_SIZE) {
                    nextPopulation.add(mutate(children.get(1), MUTATION_RATE));
                }
            }
            population = nextPopulation;
        }

        if (bestOverall == null) {
            population.sort((a, b) -> Integer.compare(fitness(b), fitness(a)));
            bestOverall = population.get(0);
            bestFitnessOverall = fitness(bestOverall);
        }

        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1e9;

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Optimal Chromosome  : " + Arrays.toString(bestOverall));
        System.out.println("Optimal Fitness     : " + bestFitnessOverall + " / " + N);
        System.out.println("Generations Taken   : " + generationConverged);
        System.out.printf("Execution Time (s)  : %.6f\n", executionTime);
    }

    public static void main(String[] args) {
        System.out.println("Running Genetic Algorithm (OneMax Problem)");
        geneticAlgorithm();
    }
}