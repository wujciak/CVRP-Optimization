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
            int truckLoad = 0; // truck's current load
            int capacity = problem.getCapacity(); // max value truck can carry
            int lastVisited = depot;

            for (int node : nodes) {
                int demand = problem.getDemand(node);

                // check demand and return to depot if needed
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
