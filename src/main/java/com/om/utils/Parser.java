package com.om.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Instance parse(String filePath) throws IOException {
        int dimension = 0;
        int capacity = 0;
        Map<Integer, int[]> coordMap = new HashMap<>();
        Map<Integer, Integer> demandMap = new HashMap<>();
        int depotId = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("DIMENSION")) {
                    dimension = Integer.parseInt(line.split(":")[1].trim());
                }
                else if (line.startsWith("CAPACITY")) {
                    capacity = Integer.parseInt(line.split(":")[1].trim());
                }
                else if (line.startsWith("NODE_COORD_SECTION")) {
                    while ((line = br.readLine()) != null && !line.trim().equals("DEMAND_SECTION")) {
                        String[] parts = line.trim().split("\\s+"); // separation by space
                        if (parts.length == 3) {
                            int id = Integer.parseInt(parts[0]);
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            coordMap.put(id, new int[]{x, y});
                        }
                    }
                }

                assert line != null;
                if (line.startsWith("DEMAND_SECTION")) {
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("DEPOT_SECTION")) break;

                        String[] parts = line.trim().split("\\s+");
                        if (parts.length == 2) {
                            int id = Integer.parseInt(parts[0]);
                            int demand = Integer.parseInt(parts[1]);
                            demandMap.put(id, demand);
                        }
                    }
                }

                assert line != null;
                if (line.startsWith("DEPOT_SECTION")) {
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.equals("-1")) {
                            break;
                        }
                        depotId = Integer.parseInt(line);
                    }
                }
            }
        }

        return new Instance(dimension, capacity, coordMap, demandMap, depotId);
    }
}
