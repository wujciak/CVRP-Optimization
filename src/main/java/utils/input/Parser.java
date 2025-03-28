package utils.input;

import java.io.*;
import java.util.*;

/**
 * Class for parsing input text file into object of CVRP.
 */
public class Parser {
    public static CVRP parse(String filePath) throws IOException {
        int dimension = 0;
        int capacity = 0;
        Map<Integer, int[]> coordinates = new HashMap<>();
        Map<Integer, Integer> demands = new HashMap<>();
        int depotId = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("DIMENSION")) {
                    dimension = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("CAPACITY")) {
                    capacity = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("NODE_COORD_SECTION")) {
                    while ((line = br.readLine()) != null && !line.startsWith("DEMAND_SECTION")) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 3) continue;
                        int id = Integer.parseInt(parts[0]);
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        coordinates.put(id, new int[]{x, y});
                    }
                }

                assert line != null;
                if (line.startsWith("DEMAND_SECTION")) {
                    System.out.println("Parsing DEMAND_SECTION...");  // Debug
                    while ((line = br.readLine()) != null && !line.startsWith("DEPOT_SECTION")) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 2) continue;
                        int id = Integer.parseInt(parts[0]);
                        int demand = Integer.parseInt(parts[1]);
                        demands.put(id, demand);
                    }
                }

                assert line != null;
                if (line.startsWith("DEPOT_SECTION")) {
                    String depotLine = br.readLine();
                    if (depotLine != null && !depotLine.trim().equals("-1")) {
                        depotId = Integer.parseInt(depotLine.trim());
                    }
                }
            }
        }

        return new CVRP(dimension, capacity, coordinates, demands, depotId);
    }
}
