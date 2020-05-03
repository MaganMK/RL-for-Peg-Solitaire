package agents.rl.nn;

import java.util.ArrayList;
import java.util.List;

public class Node {

    List<Edge> outputEdges = new ArrayList<>();
    List<Edge> inputEdges = new ArrayList<>();
    double output;
    double backwardsValue;

    public void addInputEdge(Node node) {
        inputEdges.add(new Edge(node, this));
    }
    public void addOutputEdge(Node node) {
        outputEdges.add(new Edge(this, node));
    }
    public double getInputWeightedSum() {
        double weightedSum = 0;
        for (Edge edge : inputEdges) {
            double weight = edge.weight;
            double previousLayerOutput = edge.from.output;
            weightedSum = weightedSum + (weight * previousLayerOutput);
        }
        return weightedSum;
    }

    public void calculateOutput() {
        output = NN.linear(getInputWeightedSum()); //Sigmoid
    }
}
