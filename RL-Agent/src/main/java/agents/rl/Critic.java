package agents.rl;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Critic {

    private double learningRate, eligibilityDecayRate, discountFactor;
    private State currentState, nextState;
    private int searchSpaceSize;
    private HashMap<State, Integer> valueMap = new HashMap<>();
    private HashMap<State, Integer> elibilityeMap = new HashMap<>();

    public Critic(double learningRate, double eligibilityDecayRate, double discountFactor, State initialState) {
        this.learningRate = learningRate;
        this.eligibilityDecayRate = eligibilityDecayRate;
        this.discountFactor = discountFactor;
        currentState = initialState;
        searchSpaceSize = currentState.getState().size();
        populateValueMap(currentState);
        populateEligibilityMap(currentState);
    }

    private void populateValueMap(State state) {
        int value = ThreadLocalRandom.current().nextInt(RLAgent.minReward/10, RLAgent.maxReward/10 + 1);
        valueMap.put(state, value);
    }

    private void populateEligibilityMap(State state) {
        valueMap.put(state, 0);
    }

    public void updateCurrentState(State state) {
        this.currentState = state;
    }
    public void updateNextState(State state) {
        this.nextState = state;
    }
}
