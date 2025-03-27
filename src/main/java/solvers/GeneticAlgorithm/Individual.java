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

    public int getGene(int id) {
        return sequence.get(id);
    }

    public void setGene(int id, int gene) {
        sequence.set(id, gene);
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public CVRP getProblem() {return problem;}

    public void evaluate(Evaluator evaluator) {
        RouteArray route = new RouteArray(problem.getDepotId());

        for (Integer node : sequence) {
            try {
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                this.fitness = Integer.MAX_VALUE; // penalty
                return;
            }
        }
        this.fitness = evaluator.calculateScore(List.of(route));
    }
}
