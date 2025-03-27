import solvers.GeneticAlgorithm.GeneticAlgorithm;
import solvers.RandomSearch.RandomSearch;
import utils.input.CVRP;
import utils.input.Parser;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\basic-instances\\A-n32-k5.vrp.txt";

        try {
            CVRP problem = Parser.parse(filePath);
            Evaluator evaluator = new Evaluator(problem);

            // hiperparameters
            int populationSize = 100;
            int generations = 100;
            double crossoverProbability = 0.7;
            double mutationProbability = 0.1;
            int tournamentSize = 5;

            // GA section
            GeneticAlgorithm ga = new GeneticAlgorithm(problem, evaluator, populationSize, crossoverProbability, mutationProbability, tournamentSize);
            List<RouteArray> bestSolutionGA = ga.solve();
            System.out.println("----GA/EA----");
            System.out.println("Best solution:");
            for (RouteArray route : bestSolutionGA) {
                System.out.println(route.getNodes() + " (Load: " + route.getCurrentLoad() + ")");
            }
            System.out.println("Whole distance: " + evaluator.calculateScore(bestSolutionGA));

            // RS section
            int numIterations = 10000;
            System.out.println("----RANDOM-SEARCH----");
            RandomSearch rs = new RandomSearch(problem, evaluator, numIterations);
            List<RouteArray> bestSolutionRS = rs.solve();
            System.out.println("Best solution: ");
            for (RouteArray route : bestSolutionRS) {
                System.out.println(route.getNodes() + " (Load: " + route.getCurrentLoad() + ")");
            }
            System.out.println("Whole distance: " + evaluator.calculateScore(bestSolutionRS));

        } catch (IOException e) {
            System.err.println("Error loading the file: " + e.getMessage());
        }
    }
}
