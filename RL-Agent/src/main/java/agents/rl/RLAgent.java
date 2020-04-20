package agents.rl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RLAgent {

    public static final List<String> NAMES = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"));
    public static final int minReward= -100;
    public static final int maxReward= 100;

    private int episodes;
    private Actor actor;
    private Critic critic;
    private State state;

    private HashMap<List<Integer>, State> allStates = new HashMap<>();


    public RLAgent(int episodes, double actorLearningRate, double criticLearningRate, double actorEligibilityDecayRate,
                 double criticEligibilityDecayRate, double actorDiscountFactor, double criticDiscountFactor,
                 double epsilon, double epsilonDecayRate, List<Integer> initialState) {
        this.episodes = episodes;
        this.state = new State(initialState);
        this.actor = new Actor(this, actorLearningRate, actorEligibilityDecayRate,
                actorDiscountFactor, epsilon, epsilonDecayRate, state);
        this.critic = new Critic(criticLearningRate, criticEligibilityDecayRate, criticDiscountFactor, state);
    }

    public HashMap<List<Integer>, State> getAllStates() {
        return allStates;
    }

    public void updateState(List<Integer> state) {
        if (allStates.keySet().contains(state)) {
            this.state = allStates.get(state);
            actor.updateCurrentState(this.state);
            critic.updateCurrentState(this.state);
        } else {
            State s = new State(state);
            this.state = s;
            actor.updateCurrentState(this.state);
            critic.updateCurrentState(this.state);
            allStates.put(state, s);
        }
    }

    public String getAction() {
        Action action = actor.makeAction();
        return action.getFrom() + ":" + action.getTo();
    }
}
