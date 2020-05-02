package agents.rl;

import agents.rl.helpers.Action;
import agents.rl.helpers.State;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class CriticValueMap extends Critic{

    private HashMap<State, Double> valueMap = new HashMap<>();


    public CriticValueMap(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor) {
        super(agent, learningRate, eligibilityDecayRate, discountFactor);
    }

    @Override
    void populateState(State state) {
        if(!valueMap.keySet().contains(state)) {
            populateValueMap(state);
            populateEligibilityMap(state);
        }
    }

    private void populateValueMap(State state) {
        double value = ThreadLocalRandom.current().nextDouble(0, 1);
        valueMap.put(state, value);
    }


    @Override
    double getRDError(State lastState, Action lastAction, int reward, State currentState, Action currentAction) {
        currentRDError = reward + discountFactor*valueMap.get(currentState) - valueMap.get(lastState);
        return currentRDError;
    }

    @Override
    void update() {
        updateValueMap();
        updateEligibilityMap();
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
