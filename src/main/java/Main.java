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
        String filePath = "C:\\Users\\jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\basic-instances\\A-n32-k5.vrp.txt";

        try {
            System.out.println("Parsing started...");
            CVRP problem = Parser.parse(filePath);
            System.out.println("Parsing completed.");
            Evaluator evaluator = new Evaluator(problem);

            runRandomSearch(problem, evaluator);
            runGeneticAlgorithm(problem, evaluator);
            runGreedyAlgorithm(problem, evaluator);
            //runTabuSearch(problem, evaluator);
            //runSimulatedAnnealing(problem, evaluator);
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Runs Random Search 10,000 times and records the best solution.
     */
    private static void runRandomSearch(CVRP problem, Evaluator evaluator) {
        int numIterations = 10000;
        RandomSearch rs = new RandomSearch(problem, evaluator, numIterations);

        System.out.println("Random Search running...");
        List<Double> resultsRS = rs.solve();
        System.out.println("Random Search completed.");

        logResults("Random Search [10k]", resultsRS, numIterations);
    }

    /**
     * Runs Genetic Algorithm 10 times and records best*, worst, avg, std.
     */
    private static void runGeneticAlgorithm(CVRP problem, Evaluator evaluator) {
        int runs = 10;
        List<Double> scores = new ArrayList<>();

        System.out.println("Genetic Algorithm running... ");
        for (int i = 0; i < runs; i++) {
            GeneticAlgorithm ga = new GeneticAlgorithm(problem, evaluator, 100, 0.7, 0.1, 5);
            List<RouteArray> solution = ga.solve();
            scores.add(evaluator.calculateScore(solution));
        }
        System.out.println("Genetic Algorithm completed.");
        logResults("Genetic Algorithm [10x]", scores, runs);
    }

    /**
     * Runs Greedy Algorithm once.
     */
    private static void runGreedyAlgorithm(CVRP problem, Evaluator evaluator) {
        GreedyAlgorithm greedy = new GreedyAlgorithm(problem, evaluator);
        List<RouteArray> bestSolution = greedy.solve();
        double bestScore = evaluator.calculateScore(bestSolution);
        List<Double> scores = new ArrayList<>();
        scores.add(bestScore);

        System.out.println("\n Greedy Algorithm");
        System.out.println("Best found: " + bestScore);
        logResults("Greedy Algorithm [1x]", scores, 1);
    }

//    private static void runTabuSearch(CVRP problem, Evaluator evaluator) {
//        int runs = 10;
//        List<Double> scores = new ArrayList<>();
//
//        for (int i = 0; i < runs; i++) {
//            TabuSearch ts = new TabuSearch(problem, evaluator, 500); // 500 iterations
//            List<RouteArray> solution = ts.solve();
//            scores.add(evaluator.calculateScore(solution));
//        }
//            logResults("TS 10x", scores, runs);
//    }
//    private static void runSimulatedAnnealing(CVRP problem, Evaluator evaluator) {
//        int runs = 10;
//        List<Double> scores = new ArrayList<>();
//
//        for (int i = 0; i < runs; i++) {
//            SimulatedAnnealing sa = new SimulatedAnnealing(problem, evaluator, 1000); // 1000 iterations
//            List<RouteArray> solution = sa.solve();
//            scores.add(evaluator.calculateScore(solution));
//        }
//            logResults("SA 10x", scores, runs);
//    }

    private static void logResults(String algorithm, List<Double> scores, int iterations) {
        String fileName = "results.log";
        double best = scores.stream().min(Double::compareTo).orElse(Double.NaN);
        double worst = scores.stream().max(Double::compareTo).orElse(Double.NaN);
        double avg = scores.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        double std = calculateStandardDeviation(scores, avg);

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("\n===========================================\n");
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

    private static double calculateStandardDeviation(List<Double> scores, double mean) {
        double sumSquaredDiffs = scores.stream().mapToDouble(score -> Math.pow(score - mean, 2)).sum();
        return Math.sqrt(sumSquaredDiffs / scores.size());
    }
}
