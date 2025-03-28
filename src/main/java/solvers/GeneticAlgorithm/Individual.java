package solvers.GeneticAlgorithm;

import utils.input.CVRP;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.util.ArrayList;
import java.util.List;

/**
 * (Solution)
 */
class Individual {
    private final List<Integer> sequence;
    private double fitness;
    private final CVRP problem;

    public Individual(CVRP problem) {
        this.problem = problem;
        this.sequence = new ArrayList<>();
    }

    public Individual(List<Integer> sequence, CVRP problem) {
        this.problem = problem;
        this.sequence = new ArrayList<>(sequence);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public CVRP getProblem() { return problem; }

    public void evaluate(Evaluator evaluator) {
        List<RouteArray> routes = new ArrayList<>();
        RouteArray currentRoute = new RouteArray(problem.getDepotId());
        routes.add(currentRoute);
        int currentLoad = 0;

        for (int node : sequence) {
            int demand = problem.getDemand(node);
            if (currentLoad + demand > problem.getCapacity()) {
                // Return to depot and start new route
                currentRoute = new RouteArray(problem.getDepotId());
                routes.add(currentRoute);
                currentLoad = 0;
            }
            currentRoute.addNode(node, demand, problem.getCapacity());
            currentLoad += demand;
        }
        this.fitness = evaluator.calculateScore(routes);
    }
}
