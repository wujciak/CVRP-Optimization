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
        Set<Integer> unvisited = new HashSet<>();

        for (int i = 1; i <= problem.getDimension(); i++) {
            if (i != problem.getDepotId()) {
                unvisited.add(i);
            }
        }

        while (!unvisited.isEmpty()) {
            RouteArray route = new RouteArray(problem.getDepotId());
            int currentNode = problem.getDepotId();
            int currentLoad = 0;

            // Always start route with depot
            route.addNode(problem.getDepotId(), 0, problem.getCapacity());

            while (true) {
                int nextNode = findNearestFeasibleNeighbor(currentNode, unvisited, currentLoad);
                if (nextNode == -1) break;

                int demand = problem.getDemand(nextNode);
                route.addNode(nextNode, demand, problem.getCapacity());
                unvisited.remove(nextNode);
                currentLoad += demand;
                currentNode = nextNode;
            }

            // Always end route with depot
            route.addNode(problem.getDepotId(), 0, problem.getCapacity());
            solution.add(route);
        }

        double totalDistance = evaluator.calculateScore(solution);
        System.out.println("Greedy Solution Distance: " + totalDistance);
        return solution;
    }

    private int findNearestFeasibleNeighbor(int currentNode, Set<Integer> unvisited, int currentLoad) {
        int nearestNode = -1;
        double minDistance = Double.MAX_VALUE;

        for (int candidate : unvisited) {
            int demand = problem.getDemand(candidate);
            if (currentLoad + demand <= problem.getCapacity()) {
                double distance = problem.getDistance(currentNode, candidate);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestNode = candidate;
                }
            }
        }
        return nearestNode;
    }
}