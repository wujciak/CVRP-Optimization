import utils.CVRP;
import utils.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Temporary debug code
        try {
            CVRP problem = Parser.parse("C:\\Users\\Jakub\\IdeaProjects\\OptimizationStartUp\\src\\main\\resources\\A-n32-k5.vrp.txt");

            System.out.println("Depot: " + problem.getDepotId());
            System.out.println("Capacity: " + problem.getCapacity());
            System.out.println("Distance from 1 to 2: " + problem.getDistance(1, 2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
