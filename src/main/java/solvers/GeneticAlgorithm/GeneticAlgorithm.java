package solvers.GeneticAlgorithm;

import utils.input.CVRP;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    public GeneticAlgorithm(CVRP problem, Evaluator evaluator, int populationSize,  double crossoverP, double mutationP , int tournamentSize) {
        this.problem = problem;
        this.evaluator = evaluator;
        this.selection = new Selection(tournamentSize);
        this.crossover = new Crossover(crossoverP);
        this.mutation = new Mutation(mutationP);
        this.populationSize = populationSize;
    }

    public List<RouteArray> solve() {
        // Population init
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(randomRoute(), problem);
            individual.evaluate(evaluator);
            population.add(individual);
        }

        // Selection and crossover
        for (int gen = 0; gen < populationSize; gen++) {
            List<Individual> newPopulation = new ArrayList<>();

            while (newPopulation.size() < populationSize) {
                Individual parent1 = selection.tournamentSelection(population);
                Individual parent2 = selection.tournamentSelection(population);
                Individual child = crossover.orderedCrossover(parent1, parent2);
                mutation.mutate(child.getSequence());
                child.evaluate(evaluator);
                newPopulation.add(child);
            }

            population = newPopulation;
        }

        Individual bestIndividual = Collections.min(population, (a, b) -> Double.compare(a.getFitness(), b.getFitness()));

        List<RouteArray> bestRoutes = new ArrayList<>();
        RouteArray route = new RouteArray(problem.getDepotId());

        for (Integer node : bestIndividual.getSequence()) {
            try {
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            } catch (IllegalArgumentException e) {
                bestRoutes.add(route);
                route = new RouteArray(problem.getDepotId());
                route.addNode(node, problem.getDemand(node), problem.getCapacity());
            }
        }
        bestRoutes.add(route);

        return bestRoutes;
    }

    private List<Integer> randomRoute() {
        List<Integer> route = new ArrayList<>();
        for (int i = 1; i <= problem.getDimension(); i++) {
            route.add(i);
        }
        Collections.shuffle(route);
        return route;
    }
}
