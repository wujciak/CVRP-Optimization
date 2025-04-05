package com.om.solvers.GreedySearch;

import com.om.utils.Instance;
import com.om.utils.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GreedySearch {

    public static List<Route> solve(Instance instance) {
        double[][] distanceMatrix = instance.getDistanceMatrix();
        int depot = instance.getDepotId();
        Map<Integer, Integer> demandMap = instance.getDemandMap();

        List<Integer> remainingCustomers = new ArrayList<>(demandMap.keySet());
        remainingCustomers.remove((Integer) depot);

        List<Route> allRoutes = new ArrayList<>();

        while (!remainingCustomers.isEmpty()) {
            int currentLocation = depot;
            int currentCapacity = instance.getCapacity();
            Route route = new Route(depot);

            boolean foundCustomer = true;

            while (foundCustomer) {
                Integer closestCustomer = null;
                double minDistance = Double.POSITIVE_INFINITY;

                for (Integer customer : remainingCustomers) {
                    int demand = demandMap.get(customer);
                    if (currentCapacity >= demand) {
                        double distance = distanceMatrix[currentLocation][customer];
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestCustomer = customer;
                        }
                    }
                }

                if (closestCustomer != null) {
                    route.addCity(closestCustomer);
                    currentCapacity -= demandMap.get(closestCustomer);
                    currentLocation = closestCustomer;
                    remainingCustomers.remove(closestCustomer);
                } else {
                    foundCustomer = false;
                }
            }

            allRoutes.add(route);
        }

        return allRoutes;
    }
}
