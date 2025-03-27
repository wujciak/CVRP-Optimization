package utils.output;

import utils.input.CVRP;

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
        double totalDistance = 0.0;

        for (RouteArray route : routes) {
            List<Integer> nodes = route.getNodes();

            // From depot to first node
            totalDistance += problem.getDistance(route.getDepotId(), nodes.getFirst());
            // Between nodes
            for (int i = 0; i < nodes.size() - 1; i++) { totalDistance += problem.getDistance(nodes.get(i), nodes.get(i + 1)); }
            // From last node to depot
            totalDistance += problem.getDistance(nodes.getLast(), problem.getDepotId());
        }

        return totalDistance;
    }
}
