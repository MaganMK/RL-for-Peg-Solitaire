package agents.rl;

import agents.rl.helpers.Action;
import agents.rl.helpers.Methods;
import agents.rl.helpers.State;
import agents.rl.helpers.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Actor {

    private double learningRate,
            eligibilityDecayRate,
            discountFactor;

    private RLAgent agent;
    private HashMap<Tuple<State, Action>, Double> policyMap = new HashMap<>();
    private HashMap<Tuple<State, Action>, Double> eligibilityMap = new HashMap<>();
    private List<Tuple<State, Action>> saps = new ArrayList<>();

    public Actor(RLAgent agent, double learningRate, double eligibilityDecayRate, double discountFactor) {

        this.agent = agent;
        this.learningRate = learningRate;
        this.eligibilityDecayRate = eligibilityDecayRate;
        this.discountFactor = discountFactor;
    }

    void populateState(State state) {
        if (!agent.getAllStates().values().contains(state)) {
            populateSaps(state);
            populatePolicyMap(state);
            populateEligibilityMap(state);
        }
    }

    private List<Action> getLegalActions() {
        List<Action> moves = new ArrayList<>();
        for (String legalMove : agent.game.getLegalMoves()) {
            String from = legalMove.split(" ")[0];
            String to = legalMove.split(" ")[1];
            moves.add(new Action(from, to));
        }
        return moves;
    }

    private void populateSaps(State state) {
        for (Action action : getLegalActions()) {
            Tuple<State, Action> sap = new Tuple<>(state, action);
            saps.add(sap);
        }
    }

    private void populatePolicyMap(State state) {
        for (Tuple<State, Action> sap : saps) {
            if (sap.x.equals(state)) {
                policyMap.put(sap, 0.0);
            }
        }
    }

    private void populateEligibilityMap(State state) {
        for (Tuple<State, Action> sap : saps) {
            if (sap.x.equals(state)) {
                eligibilityMap.put(sap, 0.0);
            }
        }
    }

    Action getAction(State state) {
        HashMap<Action, Double> possibleActions = new HashMap<>();
        for (Tuple<State, Action> sap : saps) {
            if (sap.x.equals(state)) {
                possibleActions.put(sap.y, policyMap.get(sap));
            }
        }

        double prob = ThreadLocalRandom.current().nextDouble(0, 1);
        if(prob >= agent.epsilon) {
            Action currentBest = possibleActions.keySet().iterator().next();
            for (Action candidate : possibleActions.keySet()) {
                if (possibleActions.get(candidate) > possibleActions.get(currentBest)) {
                    currentBest = candidate;
                }
            }
            return  currentBest;
        }

        return Methods.getRandomElement(possibleActions.keySet());
    }

    void reset() {
        resetEligibilityMap();
    }

    private void resetEligibilityMap() {
        for (Tuple<State, Action> sap : eligibilityMap.keySet()) {
            eligibilityMap.put(sap, 0.0);
        }
    }

    void setEligibility(State state, Action action) {
        for (Tuple<State, Action> sap : saps) {
            if (sap.x.equals(state) && sap.y.equals(action)) {
                eligibilityMap.put(sap, 1.0);
            }
        }
    }

    void update(double rdError) {
        updatePolicyMap(rdError);
        updateEligibilityMap();
    }

    private void updatePolicyMap(double rdError) {
        for (Tuple<State, Action> sap : saps) {
            double value = policyMap.get(sap) + learningRate*rdError*eligibilityMap.get(sap);
            policyMap.put(sap, value);
        }

    }

    private void updateEligibilityMap() {
        for (Tuple<State, Action> sap : saps) {
            double value = discountFactor*eligibilityDecayRate*eligibilityMap.get(sap);
            eligibilityMap.put(sap, value);
        }

    }

}
