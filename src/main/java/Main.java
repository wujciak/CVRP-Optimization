import solvers.GeneticAlgorithm.GeneticAlgorithm;
import utils.input.CVRP;
import utils.input.Parser;
import utils.output.Evaluator;
import utils.output.RouteArray;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CVRP problem = Parser.parse("C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\A-n32-k5.vrp.txt");
            Evaluator evaluator = new Evaluator(problem);
            GeneticAlgorithm ga = new GeneticAlgorithm();
            List<RouteArray> bestSolution = ga.solve(problem, evaluator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
