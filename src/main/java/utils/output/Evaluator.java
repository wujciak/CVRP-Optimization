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
        System.out.println("Evaluating solution with " + routes.size() + " routes.");

        for (RouteArray route : routes) {
            List<Integer> nodes = route.getNodes();
            int depot = route.getDepotId();
            int truckLoad = 0; // truck's current load
            int capacity = problem.getCapacity(); // max value truck can carry
            int lastVisited = depot;

            System.out.println("New Route Starting at Depot " + depot);

            for (int node : nodes) {
                if (node == depot) {
                    System.out.println("Returning to Depot.");
                    truckLoad = 0;
                    continue;
                }

                int demand = problem.getDemand(node);
                truckLoad += demand;
                // check feasibility
                if (truckLoad > capacity) {
                    System.out.println("ERROR: Capacity exceeded! Invalid route.");
                    return Double.MAX_VALUE; // penalty for impossible solution
                }

                double distance = problem.getDistance(lastVisited, node);
                totalDistance += distance;
                System.out.println("Traveling from " + lastVisited + " to " + node + " | Distance: " + distance);
                lastVisited = node;
            }

            double returnDistance = problem.getDistance(lastVisited, depot);
            totalDistance += returnDistance;
            System.out.println("Returning from " + lastVisited + " to depot " + depot + " | Distance: " + returnDistance);
        }

        System.out.println("Total Score (Distance): " + totalDistance);
        return totalDistance;
    }
}
