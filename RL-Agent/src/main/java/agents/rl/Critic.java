package agents.rl;

import agents.rl.helpers.Action;
import agents.rl.helpers.State;

import java.util.HashMap;

public abstract class Critic {

    double learningRate, eligibilityDecayRate, discountFactor;
    HashMap<State, Double> eligibilityMap = new HashMap<>();
    RLAgent agent;
    double currentRDError;

    public Critic(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor) {
        this.learningRate = learningRate;
        this.eligibilityDecayRate = eligibilityDecayRate;
        this.discountFactor = discountFactor;
        this.agent = agent;
    }

    abstract void populateState(State state);

    abstract double getRDError(State lastState, Action lastAction, int reward, State currentState, Action currentAction);
    abstract void update();

    void reset() {
        resetEligibilityMap();
    }

    void populateEligibilityMap(State state) {
        eligibilityMap.put(state, 1.0);
    }

    private void resetEligibilityMap() {
        for (State state : eligibilityMap.keySet()) {
            eligibilityMap.put(state, 0.0);
        }
    }


    void setEligibility(State state) {
        eligibilityMap.put(state, 1.0);
    }
}
