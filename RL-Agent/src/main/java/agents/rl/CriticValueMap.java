package agents.rl;

import agents.rl.helpers.Action;
import agents.rl.helpers.State;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class CriticValueMap extends Critic{

    private HashMap<State, Double> valueMap = new HashMap<>();
    HashMap<State, Double> eligibilityMap = new HashMap<>();


    public CriticValueMap(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor) {
        super(agent, learningRate, eligibilityDecayRate, discountFactor);
    }

    @Override
    void populateState(State state) {
        if(!valueMap.keySet().contains(state)) {
            populateValueMap(state);
            setEligibilityMap(state);
        }
    }

    private void populateValueMap(State state) {
        double value = ThreadLocalRandom.current().nextDouble(0, 1);
        valueMap.put(state, value);
    }


    @Override
    double getRDError(State lastState, int reward, State currentState) {
        currentRDError = reward + discountFactor*valueMap.get(currentState) - valueMap.get(lastState);
        return currentRDError;
    }

    @Override
    void update() {
        setEligibilityMap(agent.lastState);
        updateValueMap();
        updateEligibilityMap();
    }

    @Override
    void resetEligibilityMap() {
        for (State state : eligibilityMap.keySet()) {
            eligibilityMap.put(state, 0.0);
        }
    }

    private void setEligibilityMap(State state) {
        eligibilityMap.put(state, 1.0);
    }


    private void updateValueMap() {
        for (State state : agent.getAllStates().values()) {
            double value = valueMap.get(state) + learningRate*currentRDError*eligibilityMap.get(state);
            valueMap.put(state, value);
        }
    }

    private void updateEligibilityMap() {
        for (State state : agent.getAllStates().values()) {
            double value = discountFactor*eligibilityDecayRate*eligibilityMap.get(state);
            eligibilityMap.put(state, value);
        }
    }

}
