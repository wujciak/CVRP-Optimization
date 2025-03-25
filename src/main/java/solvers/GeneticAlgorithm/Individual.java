package solvers.GeneticAlgorithm;

import utils.input.CVRP;
import utils.output.RouteArray;

import java.util.ArrayList;
import java.util.List;

class Individual {
    public static final int popSize = 100;
    private final List<Integer> genes = new ArrayList<>();
    private int fitness;
    private final CVRP problem;

    public Individual(CVRP problem) {
        this.problem = problem;
    }

    public Individual(List<Integer> genes, CVRP problem) {
        this.problem = problem;
        this.genes.addAll(genes);
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getGene(int id) {
        return genes.get(id);
    }

    public void setGene(int id, int gene) {
        genes.set(id, gene);
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public int eval() {
        RouteArray route = new RouteArray();
        fitness = 0;

        for (Integer node : genes) {
            int demand = problem.getDemand(node);
            int capacity = problem.getCapacity();
            try {
                route.addNode(node, demand, capacity);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                return Integer.MAX_VALUE; // penalty
            }
        }

        for (int i = 0; i < route.getNodes().size() - 1; i++) {
            fitness += (int) problem.getDistance(route.getNodes().get(i), route.getNodes().get(i + 1));
        }
        fitness += (int) problem.getDistance(route.getNodes().getLast(), problem.getDepotId());

        return fitness;
    }
}
