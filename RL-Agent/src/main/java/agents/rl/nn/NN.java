package agents.rl.nn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NN {

    static double INITIALWEIGHT = 1.0;
    static double INITIALELIGIBILITY = 1.0;

    private List<Layer> hiddenLayers = new ArrayList<>();
    private Layer inputLayer;
    private Layer outputLayer;
    private int numberOfLayers;
    private double learningRate;

    public NN(List<Integer> layers, double learningRate) {
        this.learningRate = learningRate;
        numberOfLayers = layers.size();
        inputLayer = new Layer(layers.get(0));
        outputLayer = new Layer(layers.get(numberOfLayers-1));
        layers.remove(0);
        layers.remove(layers.size()-1);


        for (int hiddenLayerNodeCount : layers) {
            Layer currentLayer = new Layer(hiddenLayerNodeCount);
            hiddenLayers.add(currentLayer);
        }

        createEdges(inputLayer, hiddenLayers.get(0));

        for (int i = 1; i<hiddenLayers.size(); i++) {
            createEdges(hiddenLayers.get(i-1), hiddenLayers.get(i));
        }
        createEdges(hiddenLayers.get(hiddenLayers.size()-1), outputLayer);
    }

    private void createEdges(Layer fromLayer, Layer toLayer) {
        for (Node from : fromLayer.nodes) {
            for (Node to : toLayer.nodes) {
                from.addOutputEdge(to);
                to.addInputEdge(from);
            }
        }
    }

    public void resetEligibility() {
        resetElegibilityLayer(outputLayer);

        for(Layer hiddenLayer : hiddenLayers) {
            resetElegibilityLayer(hiddenLayer);
        }
    }

    private void resetElegibilityLayer(Layer layer) {
        for (Node n : layer.nodes) {
            for (Edge edge : n.inputEdges) {
                edge.eligibility = INITIALELIGIBILITY;
            }
        }
    }

    public double forwardPropagate(List<Integer> input) {
        for (int i=0; i<input.size(); i++) {
            inputLayer.nodes.get(i).output = Double.valueOf(input.get(i));
        }
        for (Layer hiddenLayer : hiddenLayers) {
            for (Node node : hiddenLayer.nodes) {
                node.calculateOutput();
            }
        }
        Node outputNode = outputLayer.nodes.get(0);
        outputNode.calculateOutput();
        return outputNode.output;
    }

    public void backwardPropagate(double rdError, double eligibilityDecay) {
        backPropOutPutLayer(outputLayer, rdError, eligibilityDecay);
        for (Layer hiddenLayer : hiddenLayers) {
            backPropHiddenLayer(hiddenLayer, rdError, eligibilityDecay);
        }

    }

    private void backPropOutPutLayer(Layer outputLayer, double rdError, double eligibilityDecay) {
        for (Node node : outputLayer.nodes) {
            for (Edge edge : node.inputEdges) {
                double backwardValue = linearDerived(node.getInputWeightedSum());
                node.backwardsValue = backwardValue;
                double derivative = edge.from.output*backwardValue;

                double oldEligibility = edge.eligibility;
                edge.setEligibility((oldEligibility + derivative)*eligibilityDecay);
                double oldWeight = edge.weight;
                edge.setWeight(oldWeight + learningRate*rdError*edge.eligibility);
            }
        }
    }

    private void backPropHiddenLayer(Layer layer, double rdError, double eligibilityDecay) {
        for (Node node : layer.nodes) {
            for (Edge edge : node.inputEdges) {
                double sum = 0;
                for (Edge toEdge : node.outputEdges) {
                    sum += toEdge.weight*toEdge.to.backwardsValue;
                }
                double backwardValue = linearDerived(node.getInputWeightedSum())*sum;
                node.backwardsValue = backwardValue;
                double derivative = edge.from.output*backwardValue;

                double oldEligibility = edge.eligibility;
                edge.setEligibility((oldEligibility + derivative)*eligibilityDecay);
                double oldWeight = edge.weight;
                edge.setWeight(oldWeight + learningRate*rdError*edge.eligibility);
            }
        }
    }

    static double sigmoid(double x) {
        return 1.0/(1 + Math.exp(-x));
    }
    static double sigmoidDerived(double x) {
        return Math.exp(-x)/((Math.exp(-x) + 1)*(Math.exp(-x) + 1));
    }

    static double linear(double x) {
        return x;
    }
    static double linearDerived(double x) {
        return 1;
    }

}
