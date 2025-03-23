package utils;

import java.util.List;

/**
 * Class for evaluating given solution. Calculating total distance.
 */
public class Evaluator {
    private final CVRP problem;

    public Evaluator(CVRP problem) {
        this.problem = problem;
    }

    public double calculateScore(List<RouteArray> routes) {
        double score = 0.0;
        for (RouteArray route : routes) {
            for (int i = 0; i < route.nodes.size() - 1; i++) {
                score = problem.getDistance(route.nodes.get(i), route.nodes.get(i + 1));
            }
        }
        return score;
    }
}
