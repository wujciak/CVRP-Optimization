package solvers.GeneticAlgorithm;

import utils.input.CVRP;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.util.*;

/**
 * Genetic Algorithm for solving CVRP
 */
public class GeneticAlgorithm {
    public final CVRP problem;
    public final Evaluator evaluator;
    private final Selection selection;
    private final Crossover crossover;
    private final Mutation mutation;
    private final int populationSize;
    private final int maxGenerations;

    public GeneticAlgorithm(CVRP problem, Evaluator evaluator, int populationSize, double crossoverP, double mutationP , int tournamentSize, int maxGenerations) {
        this.problem = problem;
        this.evaluator = evaluator;
        this.selection = new Selection(tournamentSize);
        this.crossover = new Crossover(crossoverP);
        this.mutation = new Mutation(mutationP);
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
    }

    public List<RouteArray> solve() {
        List<Individual> population = initializePopulation();
        Individual bestIndividual = findBestIndividual(population);

        int stagnationCounter = 0;
        double lastBestFitness = bestIndividual.getFitness();

        for (int gen=0; gen < maxGenerations; gen++) {
            population = generateNextGeneration(population);
            Individual currentBestIndividual = findBestIndividual(population);

            if (currentBestIndividual.getFitness() >= lastBestFitness) {
                stagnationCounter++;
                if (stagnationCounter >= 20) {
                    break;
                }
            } else {
                stagnationCounter = 0;
                lastBestFitness = currentBestIndividual.getFitness();
            }

            if (currentBestIndividual.getFitness() < bestIndividual.getFitness()) {
                bestIndividual = currentBestIndividual;
            }
        }

        return decodeSolution(bestIndividual);
    }

    private List<Individual> initializePopulation() {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(generateRandomRoute(), problem);
            individual.evaluate(evaluator);
            population.add(individual);
        }
        return population;
    }

    private List<Individual> generateNextGeneration(List<Individual> population) {
        List<Individual> newPopulation = new ArrayList<>();
        Individual bestIndividual = findBestIndividual(population);
        newPopulation.add(new Individual(bestIndividual.getSequence(), bestIndividual.getProblem()));

        while (newPopulation.size() < populationSize) {
            Individual parent1 = selection.tournamentSelection(population);
            Individual parent2 = selection.tournamentSelection(population);
            Individual child = crossover.orderedCrossover(parent1, parent2);
            mutation.mutate(child.getSequence());
            child.evaluate(evaluator);
            newPopulation.add(child);
        }
        return newPopulation;
    }

    private Individual findBestIndividual(List<Individual> population) {
        return Collections.min(population, Comparator.comparingDouble(Individual::getFitness));
    }

    private List<RouteArray> decodeSolution(Individual bestIndividual) {
        List<RouteArray> bestRoutes = new ArrayList<>();
        RouteArray route = new RouteArray(problem.getDepotId());

        for (Integer node : bestIndividual.getSequence()) {
            try {
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            } catch (IllegalArgumentException e) {
                route.addNode(problem.getDepotId(), 0, problem.getCapacity()); // Powrót do depotu
                bestRoutes.add(route);
                route = new RouteArray(problem.getDepotId());
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            }
        }
        route.addNode(problem.getDepotId(), 0, problem.getCapacity()); // Powrót na koniec
        bestRoutes.add(route);
        return bestRoutes;
    }


    private List<Integer> generateRandomRoute() {
        List<Integer> route = new ArrayList<>();
        // 2 for ignoring depot
        for (int i = 2; i <= problem.getDimension(); i++) {
            route.add(i);
        }
        Collections.shuffle(route);
        return route;
    }
}
