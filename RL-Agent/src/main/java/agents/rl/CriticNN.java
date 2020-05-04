package agents.rl;

import agents.rl.helpers.Action;
import agents.rl.helpers.State;
import agents.rl.nn.NN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CriticNN extends Critic{

    List<Integer> layers;
    private NN  model;


    public CriticNN(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor, List<Integer> layers) {
        super(agent, learningRate, eligibilityDecayRate, discountFactor);
        this.layers = layers;
        model = new NN(this.layers, learningRate);
    }

    @Override
    void populateState(State state) { }
    @Override
    void resetEligibilityMap() {
        model.resetEligibility();
    }

    @Override
    double getRDError(State lastState, int reward, State currentState) {
        currentRDError = reward
                + discountFactor*model.forwardPropagate(currentState.getState())
                - model.forwardPropagate(lastState.getState());
        return currentRDError;
    }

    @Override
    void update() {
        model.backwardPropagate(currentRDError, eligibilityDecayRate);
    }
}
