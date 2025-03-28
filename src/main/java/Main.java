import solvers.GreedyAlgorithm.GreedyAlgorithm;
import solvers.RandomSearch.RandomSearch;
import solvers.GeneticAlgorithm.GeneticAlgorithm;
import solvers.SimulatedAnnealing.SimulatedAnnealing;
import utils.input.CVRP;
import utils.input.Parser;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String instance = "A-n32-k5.vrp.txt";
        String filePath = "C:\\Users\\jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\basic-instances\\" + instance;

        int popSize = 100;
        int generations = 500;
        double crossoverProb = 0.8;
        double mutationProb = 0.01;
        int tournamentSize = 5;

        try {
            System.out.println("Parsing started...");
            CVRP problem = Parser.parse(filePath);
            System.out.println("Parsing completed.");
            Evaluator evaluator = new Evaluator(problem);

            runRandomSearch(problem, evaluator, instance);
            runGeneticAlgorithm(problem, evaluator, instance, popSize, generations, crossoverProb, mutationProb, tournamentSize);
            runGreedyAlgorithm(problem, evaluator, instance);
            runSA(problem, evaluator, instance);
            System.out.println("Whole program completed.");

        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Method for running Random Search 10 000 times
     */
    private static void runRandomSearch(CVRP problem, Evaluator evaluator, String instance) {
        int numIterations = 10000;
        RandomSearch rs = new RandomSearch(problem, evaluator, numIterations);

        System.out.println("Random Search running...");
        List<Double> resultsRS = rs.solve();
        System.out.println("Random Search completed.");

        logResults("Random Search [10k]", resultsRS, numIterations, instance);
    }

    /**
     * Method for running Genetic Algorithm 10 times
     */
    private static void runGeneticAlgorithm(CVRP problem, Evaluator evaluator, String instance, int popSize, int generations, double crossoverProb, double mutationProb, int tournamentSize) {
        int runs = 10;
        List<Double> scores = new ArrayList<>();
        System.out.println("Genetic Algorithm running... ");

        for (int i = 0; i < runs; i++) {
            GeneticAlgorithm ga = new GeneticAlgorithm(problem, evaluator, popSize, crossoverProb, mutationProb, tournamentSize, generations);
            List<RouteArray> solution = ga.solve();
            scores.add(evaluator.calculateScore(solution));
        }

        System.out.println("Genetic Algorithm completed.");
        logResults("Genetic Algorithm [10x]", scores, runs, instance);
    }

    /**
     * Method for running Greedy Algorithm once.
     */
    private static void runGreedyAlgorithm(CVRP problem, Evaluator evaluator, String instance) {
        System.out.println("Greedy Algorithm running... ");

        GreedyAlgorithm greedy = new GreedyAlgorithm(problem, evaluator);
        List<RouteArray> bestSolution = greedy.solve();
        double bestScore = evaluator.calculateScore(bestSolution);
        List<Double> scores = new ArrayList<>();
        scores.add(bestScore);

        System.out.println("Greedy Algorithm completed. ");
        logResults("Greedy Algorithm [1x]", scores, 1, instance);
    }

    /**
     * Method for running SA
     */
    private static void runSA(CVRP problem, Evaluator evaluator, String instance) {
        System.out.println("SA Algorithm running... ");

        SimulatedAnnealing sa = new SimulatedAnnealing(problem, evaluator);
        List<RouteArray> bestSolution = sa.solve();
        double bestScore = evaluator.calculateScore(bestSolution);
        List<Double> scores = new ArrayList<>();
        scores.add(bestScore);
        System.out.println("SA Algorithm completed. ");
        logResults("SA Algorithm [10x]", scores, 10, instance);
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
