package solvers.GeneticAlgorithm;

import java.util.List;
import java.util.Random;

class Selection {

    public Selection() {
    }

    public Individual tournamentSelection(List<Individual> population) {
        Random rand = new Random();
        Individual best = population.get(rand.nextInt(population.size()));

        int tournamentSize = 5;
        for (int i = 0; i < tournamentSize; i++) {
            Individual ind = population.get(rand.nextInt(population.size()));
            if (ind.getFitness() < best.getFitness()) {
                best = ind;
            }
        }
        return best;
    }
}
