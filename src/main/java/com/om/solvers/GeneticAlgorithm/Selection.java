package com.om.solvers.GeneticAlgorithm;

import com.om.solvers.RandomSearch.RandomSearch;
import com.om.utils.Instance;

import java.util.*;

public class Selection {
    public final int tournamentSize;

    public Selection(int touurnamentSize) {
        this.tournamentSize = touurnamentSize;
    }

    public Individual tournamentSelection(List<Individual> population) {
        Random rand = new Random();
        List<Individual> compatitors = new ArrayList<>();

        while (compatitors.size() < tournamentSize) {
            Individual candidate = population.get(rand.nextInt(population.size()));
            compatitors.add(candidate);
        }
        return Collections.min(compatitors, Comparator.comparing(Individual::getFitness));
    }
}
