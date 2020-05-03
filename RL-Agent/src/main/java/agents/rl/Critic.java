package agents.rl;

import agents.rl.helpers.Action;
import agents.rl.helpers.State;

import java.util.HashMap;

public abstract class Critic {

    double learningRate, eligibilityDecayRate, discountFactor;
    RLAgent agent;
    double currentRDError;

    public Critic(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor) {
        this.learningRate = learningRate;
        this.eligibilityDecayRate = eligibilityDecayRate;
        this.discountFactor = discountFactor;
        this.agent = agent;
    }

    abstract void populateState(State state);
    abstract double getRDError(State lastState, int reward, State currentState);
    abstract void update();
    abstract void resetEligibilityMap();

    void reset() {
        resetEligibilityMap();
    }
}
