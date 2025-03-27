package utils.output;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing route from solver.
 */
public class RouteArray {
    private final List<Integer> nodes;
    private int currentLoad;
    private final int depotId;

    public RouteArray(int depotId) {
        this.depotId = depotId;
        this.nodes = new ArrayList<>();
        this.currentLoad = 0;
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

    public int getDepotId() {
        return depotId;
    }
}
