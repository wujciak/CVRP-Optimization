package com.om;

import com.om.solvers.GeneticAlgorithm.GeneticAlgorithm;
import com.om.solvers.RandomSearch.RandomSearch;
import com.om.utils.Evaluator;
import com.om.utils.Instance;
import com.om.utils.Parser;
import com.om.utils.Route;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\jakub\\IdeaProjects\\OptimizationCVRP\\src\\main\\resources\\instances\\A-n32-k5.vrp.txt";

        int popSize = 100;
        int generations = 100;
        double crossoverProb = 0.7;
        double mutationProb = 0.1;
        int tournamentSize = 5;

        try {
            Instance instance = Parser.parse(filePath);
            Evaluator evaluator = new Evaluator();
            runGeneticAlgorithm(instance, evaluator, popSize, crossoverProb, mutationProb, tournamentSize, generations);
//            RandomSearch solver = new RandomSearch(instance);
//            List<Route> routes = solver.solve();
//            for (Route route : routes) {
//                System.out.println(route);
//            }
//            double evaluator = Evaluator.evaluate(routes, instance.getDistanceMatrix());
//            System.out.println(evaluator);
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Method for running Genetic Algorithm 10 times
     */
    private static void runGeneticAlgorithm(Instance instance, Evaluator evaluator, int popSize, double crossoverP, double mutationP , int tournamentSize, int maxGenerations) {
        int runs = 10;
        List<Double> scores = new ArrayList<>();
        System.out.println("Genetic Algorithm running... ");

        for (int i = 0; i < runs; i++) {
            GeneticAlgorithm ga = new GeneticAlgorithm(instance, evaluator, popSize, crossoverP, mutationP, tournamentSize, maxGenerations);
            List<Route> solution = ga.solve();
            double evaluator1 = Evaluator.evaluate(solution, instance.getDistanceMatrix());
            scores.add(evaluator1);
        }

        System.out.println("Genetic Algorithm completed.");
        logResults("Genetic Algorithm [10x]", scores, runs, "n32-k5");
    }

    /**
     * Method for saving logs in a file
     */
    private static void logResults(String algorithm, List<Double> scores, int iterations, String instance) {
        String fileName = "results.log";
        double best = scores.stream().min(Double::compareTo).orElse(Double.NaN);
        double worst = scores.stream().max(Double::compareTo).orElse(Double.NaN);
        double avg = scores.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double std = calculateStandardDeviation(scores, avg);

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("\n===========================================\n");
            writer.write("Instance: " + instance + "\n");
            writer.write("Algorithm: " + algorithm + "\n");
            writer.write("Timestamp: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Iterations: " + iterations + "\n");
            writer.write("Best*: " + best + "\n");
            writer.write("Worst: " + worst + "\n");
            writer.write("Avg: " + avg + "\n");
            writer.write("Std: " + std + "\n");
            writer.write("===========================================\n\n");
            System.out.println("Results logged in: " + new File(fileName).getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    /**
     * Method for calculating standard deviation
     */
    private static double calculateStandardDeviation(List<Double> scores, double mean) {
        double sumSquaredDiffs = scores.stream().mapToDouble(score -> Math.pow(score - mean, 2)).sum();
        return Math.sqrt(sumSquaredDiffs / scores.size());
    }
}