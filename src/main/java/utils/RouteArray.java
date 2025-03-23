package utils;

import java.util.ArrayList;
import java.util.List;

public class RouteArray {
    List<Integer> nodes;
    int currentLoad;

    public RouteArray() {
        nodes = new ArrayList<>();
        currentLoad = 0;
    }

    public void addNode(int node, int demand) {
        nodes.add(node);
        currentLoad += demand;
    }
}
