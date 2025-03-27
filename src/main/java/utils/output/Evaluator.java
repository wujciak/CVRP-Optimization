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
            int depot = route.getDepotId();
            int truckLoad = 0;
            int capacity = problem.getCapacity();

            // Start from depot
            int lastVisited = depot;
            totalDistance += 0;

            for (int node : nodes) {
                int demand = problem.getDemand(node);

                // Check if adding this node exceeds capacity
                if (truckLoad + demand > capacity) {
                    // Return to depot
                    totalDistance += problem.getDistance(lastVisited, depot);
                    lastVisited = depot;
                    truckLoad = 0;
                }

                // Travel to the next node
                totalDistance += problem.getDistance(lastVisited, node);
                truckLoad += demand;
                lastVisited = node;
            }

            // Return to depot at the end
            totalDistance += problem.getDistance(lastVisited, depot);
        }

        return totalDistance;
    }

}
