package solvers.RandomSearch;

import utils.input.CVRP;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Random Search for solving the CVRP
 */
public class RandomSearch {
    private final CVRP problem;
    private final Evaluator evaluator;
    private final int numIterations;

    public RandomSearch(CVRP problem, Evaluator evaluator, int numIterations) {
        this.problem = problem;
        this.evaluator = evaluator;
        this.numIterations = numIterations;
    }

    public List<RouteArray> solve() {
        List<RouteArray> bestSolution = null;
        double bestScore = Double.MAX_VALUE;
        Random rand = new Random();

        for (int i = 0; i < numIterations; i++) {
            List<Integer> randomRoute = generateRandomRoute();
            List<RouteArray> routes = convertToRoutes(randomRoute);
            double score = evaluator.calculateScore(routes);

            if (score < bestScore) {
                bestScore = score;
                bestSolution = routes;
            }
        }

        return bestSolution;
    }

    private List<Integer> generateRandomRoute() {
        List<Integer> route = new ArrayList<>();
        for (int i = 1; i <= problem.getDimension(); i++) {
            route.add(i);
        }
        Collections.shuffle(route);
        return route;
    }

    private List<RouteArray> convertToRoutes(List<Integer> sequence) {
        List<RouteArray> routes = new ArrayList<>();
        RouteArray currentRoute = new RouteArray(problem.getDepotId());
        int capacity = problem.getCapacity();

        for (Integer node : sequence) {
            int demand = problem.getDemand(node);

            if (currentRoute.getCurrentLoad() + demand > capacity) {
                routes.add(currentRoute);
                currentRoute = new RouteArray(problem.getDepotId());
            }
            currentRoute.addNode(node, demand, capacity);
        }

        routes.add(currentRoute);
        return routes;
    }
}
