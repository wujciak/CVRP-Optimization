package utils;

import java.util.*;

public class CVRP {
    private final int dimension;
    private final int capacity;
    private final Map<Integer, int[]> coordinates;
    private final Map<Integer, Integer> demands;
    private final int depotId;
    private final double[][] distanceMatrix;

    public CVRP(int dimension, int capacity, Map<Integer, int[]> coordinates, Map<Integer, Integer> demands, int depotId) {
        this.dimension = dimension;
        this.capacity = capacity;
        this.coordinates = coordinates;
        this.demands = demands;
        this.depotId = depotId;
        this.distanceMatrix = calculateDistanceMatrix();
    }

    private double[][] calculateDistanceMatrix() {
        double[][] matrix = new double[dimension + 1][dimension + 1];

        for (int i = 1; i <= dimension; i++) {
            for (int j = i + 1; j <= dimension; j++) {
                double dist = calcDistance(coordinates.get(i), coordinates.get(j));
                // symmetric matrix
                matrix[i][j] = dist;
                matrix[j][i] = dist;
            }
        }
        return matrix;
    }

    private double calcDistance(int[] point1, int[] point2) {
        return Math.hypot(point2[0] - point1[0], point2[1] - point1[1]);
    }

    public double getDistance(int from, int to) {
        return distanceMatrix[from][to];
    }

    public int getDemand(int id) {
        return demands.getOrDefault(id, 0);
    }

    public int getDepotId() {
        return depotId;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "CVRP{" +
                "dimension=" + dimension +
                ", capacity=" + capacity +
                ", depotId=" + depotId +
                ", coordinates=" + coordinates +
                ", demands=" + demands +
                '}';
    }
}
