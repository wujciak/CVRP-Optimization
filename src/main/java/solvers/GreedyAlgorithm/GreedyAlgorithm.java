package solvers.GreedyAlgorithm;

import utils.input.CVRP;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyAlgorithm {
    private final CVRP problem;
    private final Evaluator evaluator;

    public GreedyAlgorithm(CVRP problem, Evaluator evaluator) {
        this.problem = problem;
        this.evaluator = evaluator;
    }

    public List<RouteArray> solve() {
        List<RouteArray> solution = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        int depot = problem.getDepotId();

        while (visited.size() < problem.getDimension() - 1) {
            RouteArray route = new RouteArray(depot);
            int currentNode = depot;
            int remainingCapacity = problem.getCapacity();

            while (true) {
                int nextNode = findNearestNeighbor(currentNode, visited, remainingCapacity);
                if (nextNode == -1) break;
                int demand = problem.getDemand(nextNode);
                if (remainingCapacity < demand) break;

                route.addNode(nextNode, demand, problem.getCapacity());
                visited.add(nextNode);
                remainingCapacity -= demand;
                currentNode = nextNode;
            }

            if (!route.getNodes().isEmpty()) {
                route.addNode(depot, 0, problem.getCapacity());
                solution.add(route);
            }
        }

        double totalDistance = evaluator.calculateScore(solution);
        System.out.println("Greedy Algorithm Total Distance: " + totalDistance);

        return solution;
    }

    private int findNearestNeighbor(int currentNode, Set<Integer> visited, int remainingCapacity) {
        double minDistance = Double.MAX_VALUE;
        int nearestNode = -1;

        for (int i = 1; i <= problem.getDimension(); i++) {
            if (!visited.contains(i) && i != problem.getDepotId() && problem.getDemand(i) <= remainingCapacity) {
                double distance = problem.getDistance(currentNode, i);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestNode = i;
                }
            }
        }
        return nearestNode;
    }
}
