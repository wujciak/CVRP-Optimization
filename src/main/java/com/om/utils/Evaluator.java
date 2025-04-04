package com.om.utils;

import java.util.List;

public class Evaluator {

    public static double evaluate(List<Route> routes, double[][] distanceMatrix) {
        double totalDistance = 0.0;

        for (Route route : routes) {
            totalDistance += calculateRouteDistance(route, distanceMatrix);
        }

        return totalDistance;
    }

    private static double calculateRouteDistance(Route route, double[][] distanceMatrix) {
        List<Integer> cities = route.getCities();
        int depot = route.getDepot();
        double distance = 0.0;

        if (!cities.isEmpty()) {
            distance += distanceMatrix[depot][cities.getFirst()];
        }

        for (int i = 0; i < cities.size() - 1; i++) {
            distance += distanceMatrix[cities.get(i)][cities.get(i + 1)];
        }

        if (!cities.isEmpty()) {
            distance += distanceMatrix[cities.getLast()][depot];
        }

        return distance;
    }
}
