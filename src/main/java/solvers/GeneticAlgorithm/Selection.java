package solvers.GeneticAlgorithm;

import java.util.List;
import java.util.Random;

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
        Individual best = population.get(rand.nextInt(population.size()));

        for (int i = 0; i < tournamentSize; i++) {
            Individual competitor = population.get(rand.nextInt(population.size()));
            if (competitor.getFitness() < best.getFitness()) {
                best = competitor;
            }
        }
        return best;
    }
}
