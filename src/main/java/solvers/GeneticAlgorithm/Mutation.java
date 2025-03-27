package solvers.GeneticAlgorithm;

import java.util.*;

/**
 * Class for performing random deformation of genotype (swap or inversion)
 */
class Mutation {
    private final double mutationProbability;

    public Mutation(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public void mutate(List<Integer> sequence) {
        Random rand = new Random();
        if (rand.nextDouble() < mutationProbability) {
            if (rand.nextBoolean()) {
                swap(sequence);
            } else {
                inversion(sequence);
            }
        }
    }

    public void swap(List<Integer> sequence) {
        Random rand = new Random();
        int id1 = rand.nextInt(sequence.size());
        int id2 = rand.nextInt(sequence.size());

        while (id1 == id2) {
            id2 = rand.nextInt(sequence.size());
        }

        Collections.swap(sequence, id1, id2);
    }

    public void inversion(List<Integer> route) {
        Random rand = new Random();
        int start = rand.nextInt(route.size());
        int end = rand.nextInt(route.size());

        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        while (start < end) {
            Collections.swap(route, start, end);
            start++;
            end--;
        }
    }
}
