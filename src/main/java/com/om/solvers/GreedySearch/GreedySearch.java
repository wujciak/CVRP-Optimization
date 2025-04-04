//package com.om.solvers.GreedySearch;
//
//import com.om.utils.Instance;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GreedySearch {
//
//    public static double greedySearch(Instance instance) {
//        double[][] distanceMatrix = instance.calculateDistanceMatrix();
//        int depot = instance.getDepotId();
//
//        List<Integer> remainingCustomers = new ArrayList<>(instance.getDemandMap().keySet());
//        remainingCustomers.remove(depot);
//
//        int currentCapacity = instance.getCapacity();
//        double totalDistance = 0.0;
//        int currentLocation = depot;
//
//        while (!remainingCustomers.isEmpty()) {
//            Integer closestCustomer = null;
//            double minDistance = Double.POSITIVE_INFINITY;
//            Integer indexToRemove = null;
//
//            for (int i = 0; i < remainingCustomers.size(); i++) {
//                int customer = remainingCustomers.get(i);
//                int demand = instance.getDemandMap().get(customer - 1);
//
//                if (currentCapacity >= demand) {
//                    double distance = instance.calcDistanceTwoPoints(currentLocation, customer - 1);
//                    if (distance < minDistance) {
//                        minDistance = distance;
//                        closestCustomer = customer - 1;
//                        indexToRemove = i;
//                    }
//                }
//            }
//
//            if (closestCustomer != null) {
//                totalDistance += minDistance;
//                currentLocation = closestCustomer;
//                currentCapacity -= distanceMatrix.getDemand(closestCustomer);
//                if (indexToRemove != null) {
//                    remainingCustomers.remove((int) indexToRemove);
//                }
//            } else {
//                totalDistance += distanceMatrix.getDistance(currentLocation, depot);
//                currentCapacity = instance.getCapacity();
//                currentLocation = depot;
//            }
//        }
//
//        totalDistance += distanceMatrix.getDistance(currentLocation, depot);
//
//        return totalDistance;
//    }
//}
//
