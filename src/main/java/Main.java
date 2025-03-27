import solvers.GreedyAlgorithm.GreedyAlgorithm;
import solvers.RandomSearch.RandomSearch;
import solvers.GeneticAlgorithm.GeneticAlgorithm;
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
        String filePath = "C:\\Users\\jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\hard-instances\\A-n54-k7.vrp.txt";
        String instance = "A-n54-k7";

        try {
            System.out.println("Parsing started...");
            CVRP problem = Parser.parse(filePath);
            System.out.println("Parsing completed.");
            Evaluator evaluator = new Evaluator(problem);

            runRandomSearch(problem, evaluator, instance);
            runGeneticAlgorithm(problem, evaluator, instance);
            runGreedyAlgorithm(problem, evaluator, instance);
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
    private static void runGeneticAlgorithm(CVRP problem, Evaluator evaluator, String instance) {
        int runs = 10;
        List<Double> scores = new ArrayList<>();
        System.out.println("Genetic Algorithm running... ");

        for (int i = 0; i < runs; i++) {
            GeneticAlgorithm ga = new GeneticAlgorithm(problem, evaluator, 100, 0.7, 0.1, 5);
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

        // Debugging: Print routes and total distance
        System.out.println("Greedy Algorithm Solution:");
        for (int i = 0; i < bestSolution.size(); i++) {
            System.out.println("Route " + (i + 1) + ": " + bestSolution.get(i).getNodes());
        }
        System.out.println("Total Evaluated Distance: " + bestScore);

        System.out.println("Greedy Algorithm completed. ");
        logResults("Greedy Algorithm [1x]", scores, 1, instance);
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
