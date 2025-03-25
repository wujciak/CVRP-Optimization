package solvers.GeneticAlgorithm;

import java.util.*;

/**
 * Class mutating genotypes by swap or/and inversion method.
 */
class Mutation {
    private final double probabilityMutation;

    public Mutation(double probabilityMutation) {
        this.probabilityMutation = probabilityMutation;
    }

    public void mutate(List<Integer> route) {
        Random rand = new Random();
        if (rand.nextDouble() < probabilityMutation) {
            if (rand.nextBoolean()) {
                swap(route);
            } else {
                inversion(route);
            }
        }
    }

    public void swap(List<Integer> route) {
        Random rand = new Random();
        int id1 = route.get(rand.nextInt(route.size()));
        int id2 = route.get(rand.nextInt(route.size()));

        while (id1 == id2) {
            id2 = rand.nextInt(route.size());
        }

        Collections.swap(route, id1, id2);
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
