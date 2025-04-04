package com.om.solvers.GeneticAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {
    private final double mutationProb;

    public Mutation(double prob) {
        mutationProb = prob;
    }

    public void mutate(List<Integer> sequence) {
        Random rand = new Random();
        if (rand.nextDouble() < mutationProb) {
            if (rand.nextBoolean()) {
                swap(sequence);
            } else {
                inverse(sequence);
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

    public void inverse(List<Integer> sequence) {
        Random rand = new Random();
        int startIdx = rand.nextInt(sequence.size());
        int endIdx = rand.nextInt(sequence.size());

        while (startIdx == endIdx) {
            endIdx = rand.nextInt(sequence.size());
        }

        if (startIdx > endIdx) {
            int tmp = startIdx;
            startIdx = endIdx;
            endIdx = tmp;
        }

        while (startIdx < endIdx) {
            Collections.swap(sequence, startIdx, endIdx);
            startIdx++;
            endIdx--;
        }
    }
}
