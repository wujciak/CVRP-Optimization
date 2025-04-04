package com.om.solvers.GeneticAlgorithm;

import com.om.utils.Evaluator;
import com.om.utils.Instance;
import com.om.utils.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneticAlgorithm {
    public final Instance instance;
    public final Evaluator evaluator;
    private final Selection selection;
    private final Crossover crossover;
    private final Mutation mutation;
    private final int popSize;
    private final int maxGenerations;

    public GeneticAlgorithm(Instance instance, Evaluator evaluator, int popSize, double crossoverP, double mutationP , int tournamentSize, int maxGenerations) {
        this.instance = instance;
        this.evaluator = evaluator;
        this.selection = new Selection(tournamentSize);
        this.crossover = new Crossover(crossoverP);
        this.mutation = new Mutation(mutationP);
        this.popSize = popSize;
        this.maxGenerations = maxGenerations;
    }

    public List<Route> solve() {
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
        for (int i = 0; i < popSize; i++) {
            Individual individual = new Individual(instance, generateRandomRoute());
            individual.eval(evaluator);
            population.add(individual);
        }
        return population;
    }

    private List<Individual> generateNextGeneration(List<Individual> population) {
        List<Individual> newPopulation = new ArrayList<>();
        Individual bestIndividual = findBestIndividual(population);

        // Elitism
        Individual eliteCopy = new Individual(bestIndividual.getInstance(), bestIndividual.getSequence());
        eliteCopy.eval(evaluator);
        newPopulation.add(eliteCopy);

        while (newPopulation.size() < popSize) {
            Individual parent1 = selection.tournamentSelection(population);
            Individual parent2 = selection.tournamentSelection(population);
            Individual child = crossover.orderedCrossover(parent1, parent2);
            mutation.mutate(child.getSequence());
            child.eval(evaluator);
            newPopulation.add(child);
        }
        return newPopulation;
    }

    private Individual findBestIndividual(List<Individual> population) {
        return Collections.min(population, Comparator.comparingDouble(Individual::getFitness));
    }

    private List<Route> decodeSolution(Individual bestIndividual) {
        List<Route> bestRoutes = new ArrayList<>();
        Route route = new Route(instance.getDepotId());

        for (Integer gene : bestIndividual.getSequence()) {
            try {
                route.addCity(gene);
            } catch (IllegalArgumentException e) {
                route.addCity(instance.getDepotId()); // Powrót do depotu
                bestRoutes.add(route);
                route = new Route(instance.getDepotId());
                route.addCity(gene);
            }
        }
        route.addCity(instance.getDepotId()); // Powrót na koniec
        bestRoutes.add(route);
        return bestRoutes;
    }


    private List<Integer> generateRandomRoute() {
        List<Integer> route = new ArrayList<>();
        // 2 for ignoring depot
        for (int i = 2; i <= instance.getDimension(); i++) {
            route.add(i);
        }
        Collections.shuffle(route);
        return route;
    }

}
