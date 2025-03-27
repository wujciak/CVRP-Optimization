package solvers.SimulatedAnnealing;

import solvers.GreedyAlgorithm.GreedyAlgorithm;
import utils.input.CVRP;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    private final CVRP problem;
    private final Evaluator evaluator;
    private final Random random = new Random();

    private double temperature = 1000.0;
    private final double coolingRate = 0.995;
    private final int maxIterations = 10000;

    public SimulatedAnnealing(CVRP problem, Evaluator evaluator) {
        this.problem = problem;
        this.evaluator = evaluator;
    }

    public List<RouteArray> solve() {
        GreedyAlgorithm greedy = new GreedyAlgorithm(problem, evaluator);
        List<RouteArray> currentSolution = greedy.solve();
        List<RouteArray> bestSolution = new ArrayList<>(currentSolution);
        double bestScore = evaluator.calculateScore(bestSolution);

        for (int i = 0; i < maxIterations && temperature > 1e-3; i++) {
            List<RouteArray> newSolution = generateNeighbor(new ArrayList<>(currentSolution));
            double newScore = evaluator.calculateScore(newSolution);

            if (acceptanceProbability(bestScore, newScore) > random.nextDouble()) {
                currentSolution = new ArrayList<>(newSolution);
                if (newScore < bestScore) {
                    bestSolution = new ArrayList<>(newSolution);
                    bestScore = newScore;
                }
            }

            temperature *= coolingRate;
        }

        System.out.println("Simulated Annealing Best Distance: " + bestScore);
        return bestSolution;
    }

    private List<RouteArray> generateNeighbor(List<RouteArray> solution) {
        if (solution.isEmpty()) return solution;

        int routeIdx = random.nextInt(solution.size());
        RouteArray route = solution.get(routeIdx);
        if (route.getNodes().size() <= 2) return solution;

        int i = random.nextInt(route.getNodes().size() - 1) + 1;
        int j = random.nextInt(route.getNodes().size() - 1) + 1;

        if (i != j) {
            int temp = route.getNodes().get(i);
            route.getNodes().set(i, route.getNodes().get(j));
            route.getNodes().set(j, temp);
        }

        return solution;
    }

    private double acceptanceProbability(double oldScore, double newScore) {
        if (newScore < oldScore) return 1.0;
        return Math.exp((oldScore - newScore) / temperature);
    }
}
