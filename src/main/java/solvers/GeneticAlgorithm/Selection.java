package solvers.GeneticAlgorithm;

import java.util.*;

/**
 * Class for selecting individuals for reproduction according to their fitness
 */
class Selection {
    private final int tournamentSize;

    public Selection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public Individual tournamentSelection(List<Individual> population) {
        Random rand = new Random();
        List<Individual> competitors = new ArrayList<>();
        while (competitors.size() < tournamentSize) {
            Individual candidate = population.get(rand.nextInt(population.size()));

            if (!competitors.contains(candidate)) {
                competitors.add(candidate);
            }
        }
        return Collections.min(competitors, Comparator.comparingDouble(Individual::getFitness));
    }
}
