package com.om.solvers.GeneticAlgorithm;

import com.om.utils.Evaluator;
import com.om.utils.Instance;
import com.om.utils.Route;

import java.util.ArrayList;
import java.util.List;

public class Individual {
    private List<Integer> sequence;
    private double fitness;
    private final Instance instance;

    public Individual(Instance instance, List<Integer> sequence) {
        this.instance = instance;
        this.sequence = new ArrayList<>(sequence);
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Instance getInstance() {
        return instance;
    }

    public void eval(Evaluator evaluator) {
        List<Route> routes = new ArrayList<>();
        Route currentRoute = new Route(instance.getDepotId());
        routes.add(currentRoute);
        int currentLoad = 0;
        int lastVisited = instance.getDepotId();

        for (int gene : sequence) {
            int demand = instance.getDemandMap().get(gene);

            if (currentLoad + demand > instance.getCapacity()) {
                currentRoute.addCity(instance.getDepotId());
                currentRoute = new Route(instance.getDepotId());
                routes.add(currentRoute);
                currentLoad = 0;
                lastVisited = instance.getDepotId();
            }

            currentRoute.addCity(gene);
            currentLoad += demand;
            lastVisited = gene;
        }

        if (lastVisited != instance.getDepotId()) {
            currentRoute.addCity(instance.getDepotId());
        }

        this.fitness = Evaluator.evaluate(routes, instance.getDistanceMatrix());
    }
}