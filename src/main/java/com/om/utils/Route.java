package com.om.utils;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private final List<Integer> cities;
    private final int depot;

    public Route(int depot) {
        this.cities = new ArrayList<>();
        this.depot = depot;
    }

    public void addCity(int city) {
        cities.add(city);
    }

    public void addCities(List<Integer> newCities) {
        cities.addAll(newCities);
    }

    public List<Integer> getCities() {
        return cities;
    }

    public int getDepot() {
        return depot;
    }

    @Override
    public String toString() {
        return "Route{" +
                "depot=" + depot +
                ", cities=" + cities +
                '}';
    }
}
