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
        RouteArray route = new RouteArray(problem.getDepotId());

        for (Integer node : sequence) {
            try {
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            } catch (IllegalArgumentException e) {
                routes.add(route);
                route = new RouteArray(problem.getDepotId());
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            }
        }
        routes.add(route);
        this.fitness = evaluator.calculateScore(routes);
    }
}
