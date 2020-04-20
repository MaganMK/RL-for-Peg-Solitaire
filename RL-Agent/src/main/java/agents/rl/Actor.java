package agents.rl;

import agents.rl.helpers.Methods;
import agents.rl.helpers.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Actor {

    private double learningRate,
            eligibilityDecayRate,
            discountFactor,
            epsilon,
            epsilonDecayRate;

    private RLAgent agent;
    private State currentState, nextState;
    private HashMap<Tuple<State, Action>, Integer> policyMap = new HashMap<>();
    private HashMap<Tuple<State, Action>, Integer> elibiletyMap = new HashMap<>();
    private int searchSpaceSize;
    private List<Action> allActions;
    private List<Tuple<State, Action>> saps = new ArrayList<>();

    public Actor(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor, double epsilon,
                 double epsilonDecayRate, State initialState) {

        this.agent = agent;
        this.learningRate = learningRate;
        this.eligibilityDecayRate = eligibilityDecayRate;
        this.discountFactor = discountFactor;
        this.epsilon = epsilon;
        this.epsilonDecayRate = epsilonDecayRate;
        searchSpaceSize = initialState.getState().size();
        allActions = allActions();
        updateCurrentState(initialState);
    }

    public void updateCurrentState(State state) {
        this.currentState = state;
        if (!agent.getAllStates().values().contains(currentState)) {
            populateSaps(currentState);
            populateValueMap(currentState);
            populateEligibilityMap(currentState);
        }
    }

    private List<Action> allActions() {
        List<Action> moves = new ArrayList<>();
        for (int i = 0; i<searchSpaceSize; i++) {
            for (int j = 0; j<searchSpaceSize; j++) {
                String from = RLAgent.NAMES.get(i);
                String to = RLAgent.NAMES.get(j);
                moves.add(new Action(from, to));
            }
        }
        return moves;
    }

    private void populateSaps(State state) {
        for (Action action : allActions) {
            Tuple<State, Action> sap = new Tuple<>(state, action);
            saps.add(sap);
        }
    }

    private void populateValueMap(State state) {
        for (Tuple<State, Action> sap : saps) {
            if (sap.x == state) {
                int value = ThreadLocalRandom.current().nextInt(RLAgent.minReward/10, RLAgent.maxReward/10 + 1);
                policyMap.put(sap, value);
            }
        }
    }

    private void populateEligibilityMap(State state) {
        for (Tuple<State, Action> sap : saps) {
            if (sap.x == state) {
                elibiletyMap.put(sap, 0);
            }
        }
    }

    public Action makeAction() {
        double prob = ThreadLocalRandom.current().nextDouble(0, 1);
        HashMap<Integer, Action> possibleActions = new HashMap<>();
        for (Tuple<State, Action> sap : saps) {
            if (sap.x.toString().equals(currentState.toString())) {
                possibleActions.put(policyMap.get(sap), sap.y);
            }
        }

        int key = prob <= epsilon ? Collections.max(possibleActions.keySet())
                : Methods.getRandomElement(possibleActions.keySet());
        return possibleActions.get(key);
    }


}
