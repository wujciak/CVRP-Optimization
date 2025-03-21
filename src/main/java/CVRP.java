import java.util.Random;

public class CVRP {
    //CVRP->solver->EVAL
    private int truckCapacity;
    private int depotIx;
    private int[] demands;
    private double[][] distanceMatrix;

    public CVRP(int truckCapacity, int depotIx, int[] demands, double[][] distanceMatrix) {
        this.truckCapacity = truckCapacity;
        this.depotIx = depotIx;
        this.demands = demands;
        this.distanceMatrix = distanceMatrix;
    }

    public static CVRP generateRandomProblem(int cityNum, int truckCapacity) {
        Random rand = new Random();
        int totalDemand = cityNum + 1;
        double[][] distanceMatrix = new double[totalDemand][totalDemand];
        int[] demands = new int[totalDemand];
        int depotIx = 0;

        double[] x = new double[totalDemand];
        double[] y = new double[totalDemand];
        for (int i = 0; i < totalDemand; i++) {
            x[i] = rand.nextDouble();
            y[i] = rand.nextDouble();
        }

        for (int i = 0; i < totalDemand; i++) {
            for (int j = 0; j < totalDemand; j++) {
                distanceMatrix[i][j] = Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
            }
        }

        demands[0] = 0;
        for (int i = 1; i < totalDemand; i++) {
            demands[i] = rand.nextInt(20) + 1;
        }

        return new CVRP(cityNum, truckCapacity, demands, distanceMatrix);
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public int getDepotIx() {
        return depotIx;
    }

    public int[] getDemands() {
        return demands;
    }

    public double getDistanceMatrix(int i, int j) {
        return distanceMatrix[i][j];
    }

}
