package com.om;

import com.om.solvers.GeneticAlgorithm.GeneticAlgorithm;
import com.om.solvers.GreedySearch.GreedySearch;
import com.om.solvers.RandomSearch.RandomSearch;
import com.om.solvers.SimulatedAnnealing.SimulatedAnnealing;
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
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String instanceName = "A-n37-k6.vrp.txt";
        String filePath = "C:\\Users\\jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\instances\\" + instanceName;

        int popSize = 100;
        int generations = 100;
        double crossoverProb = 0.7;
        double mutationProb = 0.1;
        int tournamentSize = 5;

        try {
            Instance instance = Parser.parse(filePath);
            Evaluator evaluator = new Evaluator();

            runRandomSearch(instance, instanceName);
            runGeneticAlgorithm(instance, evaluator, popSize, crossoverProb, mutationProb, tournamentSize, generations, instanceName);
            runGreedySearch(instance, instanceName);
            runSimulatedAnnealing(instance, instanceName);
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Method for running Random Search 1000 times
     */
    public static void runRandomSearch(Instance instance, String instanceName) {
        int runs = 1000;
        List<Double> scores = new ArrayList<>();
        List<Route> bestSolution = null;
        double bestScore = Double.MAX_VALUE;

        for (int i = 0; i < runs; i++) {
            RandomSearch rs = new RandomSearch(instance);
            List<Route> solution = rs.solve();
            double score = Evaluator.evaluate(solution, instance.getDistanceMatrix());
            scores.add(score);
            if (score < bestScore) {
                bestScore = score;
                bestSolution = solution;
            }
        }

        logResults("Random Search [1000x]", scores, bestSolution, instanceName, "C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\results\\RS");
    }

    /**
     * Method for running Genetic Algorithm 10 times
     */
    public static void runGeneticAlgorithm(Instance instance, Evaluator evaluator, int popSize, double crossoverP, double mutationP , int tournamentSize, int maxGenerations, String instanceName) {
        int runs = 10;
        List<Double> scores = new ArrayList<>();
        List<Route> bestSolution = null;
        double bestScore = Double.MAX_VALUE;

        for (int i = 0; i < runs; i++) {
            GeneticAlgorithm ga = new GeneticAlgorithm(instance, evaluator, popSize, crossoverP, mutationP, tournamentSize, maxGenerations);
            List<Route> solution = ga.solve();
            double score = Evaluator.evaluate(solution, instance.getDistanceMatrix());
            scores.add(score);

            if (score < bestScore) {
                bestScore = score;
                bestSolution = solution;
            }

            System.out.println("\nRun " + (i + 1) + " GA Total Distance: " + score);
            for (Route route : solution) {
                System.out.println(route);
            }
        }

        logResults("Genetic Algorithm [10x]", scores, bestSolution, instanceName, "C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\results\\GA");
    }

    /**
     * Method for running greedy algorithm once
     */
    public static void runGreedySearch(Instance instance, String instanceName) {
        List<Route> solution = GreedySearch.solve(instance);
        double score = Evaluator.evaluate(solution, instance.getDistanceMatrix());

        System.out.println("\nGreedy Search Total Distance: " + score);
        for (Route route : solution) {
            System.out.println(route);
        }

        logResults("Greedy Search [1x]", Collections.singletonList(score), solution, instanceName, "C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\results\\GS");
    }


    /**
     * Method for running Genetic Algorithm 10 times
     */
    public static void runSimulatedAnnealing(Instance instance, String instanceName) {
        int runs = 10;
        List<Double> scores = new ArrayList<>();
        List<Route> bestSolution = null;
        double bestScore = Double.MAX_VALUE;
        for (int i = 0; i < runs; i++) {
            SimulatedAnnealing sa = new SimulatedAnnealing(instance);
            List<Route> solution = sa.solve();
            double score = Evaluator.evaluate(solution, instance.getDistanceMatrix());
            scores.add(score);

            if (score < bestScore) {
                bestScore = score;
                bestSolution = solution;
            }

            System.out.println("\nRun " + (i + 1) + " SA Total Distance: " + score);
            for (Route route : solution) {
                System.out.println(route);
            }
        }

        logResults("Simulated Annealing [10x]", scores, bestSolution, instanceName, "C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\results\\SA");
    }

    /**
     * Method for saving logs in a file
     */
    private static void logResults(String algorithm, List<Double> scores, List<Route> bestSolution, String instanceName, String RESULTS_DIR) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = algorithm + "_" + instanceName.replace(".txt", "") + "_" + timestamp + ".log";

        File dir = new File(RESULTS_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                System.err.println("Failed to create directory: " + RESULTS_DIR);
                return;
            }
        }
        File outputFile = new File(dir, fileName);

        double best = scores.stream().min(Double::compareTo).orElse(Double.NaN);
        double worst = scores.stream().max(Double::compareTo).orElse(Double.NaN);
        double avg = scores.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double std = calculateStandardDeviation(scores, avg);

        try (FileWriter writer = new FileWriter(outputFile, true)) {
            writer.write("\n===========================================\n");
            writer.write("Instance: " + instanceName + "\n");
            writer.write("Algorithm: " + algorithm + "\n");
            writer.write("Timestamp: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Iterations: " + scores.size() + "\n");
            writer.write("Best*: " + best + "\n");
            writer.write("Worst: " + worst + "\n");
            writer.write("Avg: " + avg + "\n");
            writer.write("Std: " + std + "\n");

            if (bestSolution != null) {
                writer.write("\nBest Solution Routes:\n");
                for (Route route : bestSolution) {
                    writer.write(route.toString() + "\n");
                }
            }

            writer.write("===========================================\n\n");
            System.out.println(algorithm + " results logged in: " + outputFile.getAbsolutePath());
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