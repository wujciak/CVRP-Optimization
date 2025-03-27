package solvers.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Class for merging genotypes of two individuals creating a child
 */
class Crossover {
    private final double crossoverProbability;

    public Crossover(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public Individual orderedCrossover(Individual parent1, Individual parent2) {
        Random rand = new Random();
        if (rand.nextDouble() >= crossoverProbability) {
            return new Individual(new ArrayList<>(parent1.getSequence()), parent1.getProblem());
        }

        List<Integer> parentSeq1 = parent1.getSequence();
        List<Integer> parentSeq2 = parent2.getSequence();
        int size = parentSeq1.size();

        // Randomly select two crossover points
        int start = rand.nextInt(size);
        int end = rand.nextInt(size - start) + start;

        // Initialize child with empty values
        List<Integer> childSequence = new ArrayList<>(Collections.nCopies(size, -1));

        // Copy the segment from parent1
        for (int i = start; i <= end; i++) {
            childSequence.set(i, parentSeq1.get(i));
        }

        // Fill remaining positions with parent2 values (in order)
        int index = (end + 1) % size;
        for (Integer value : parentSeq2) {
            if (!childSequence.contains(value)) {
                childSequence.set(index, value);
                index = (index + 1) % size;
            }
        }

        return new Individual(childSequence, parent1.getProblem());
    }
}
