package utils.output;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing route from solver.
 */
public class RouteArray {
    List<Integer> nodes;
    int currentLoad;

    public RouteArray() {
        nodes = new ArrayList<>();
        currentLoad = 0;
    }

    public void addNode(int node, int demand, int capacity) {
        if (currentLoad + demand > capacity) {
            throw new IllegalArgumentException("Capacity overflow with node " + node);
        }
        nodes.add(node);
        currentLoad += demand;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }
}
