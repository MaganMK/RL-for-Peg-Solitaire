package agents.rl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import agents.interfaces.AgentInterface;
import agents.rl.helpers.Action;
import agents.rl.helpers.State;
import game.helpers.GameType;
import game.interfaces.BoardInterface;
import javafx.application.Platform;

public class RLAgent implements AgentInterface {

    static final int looseReward= -50;
    static final int winReward= 100;

    private Actor actor;
    private Critic critic;
    private int episodes;

    BoardInterface game;

    private State currentState;
    private State lastState;
    private Action currentAction;
    private Action lastAction;
    double epsilon;
    double epsilonDecay;

    private HashMap<List<Integer>, State> allStates = new HashMap<>();


    public RLAgent(BoardInterface game, int episodes, double actorLearningRate, double criticLearningRate,
                   double actorEligibilityDecayRate, double criticEligibilityDecayRate, double actorDiscountFactor,
                   double criticDiscountFactor, double epsilon, double epsilonDecayRate) {

        this.game = game;
        this.episodes = episodes;
        this.epsilon = epsilon;
        this.epsilonDecay = epsilonDecayRate;

        this.actor = new Actor(this, actorLearningRate, actorEligibilityDecayRate,
                actorDiscountFactor);
        this.critic = new CriticValueMap(this, criticLearningRate, criticEligibilityDecayRate, criticDiscountFactor);
        updateState(game.getState());
        this.currentAction = actor.getAction(currentState);

    }

    HashMap<List<Integer>, State> getAllStates() {
        return allStates;
    }

    private void updateState(List<Integer> state) {
        if (allStates.keySet().contains(state)) {
            currentState = allStates.get(state);
        } else {
            currentState = new State(state);
            actor.populateState(currentState);
            critic.populateState(currentState);
            allStates.put(state, currentState);
        }
    }


    private void oneRound() {

        int reward = game.makeMove(currentAction.getFrom(), currentAction.getTo());

        lastState = currentState;
        lastAction = currentAction;
        updateState(game.getState());

        double rdError = critic.getRDError(lastState, lastAction, reward, currentState, currentAction);
        actor.setEligibility(lastState, lastAction);
        critic.setEligibility(lastState);
        critic.update();
        actor.update(rdError);

        if(!game.isFinished()) {
            currentAction = actor.getAction(currentState);
        }
    }

    private void newGame() {
        game.reGenerateBoard();
        updateState(game.getState());
        actor.reset();
        critic.reset();
    }

    @Override
    public boolean train() {
        game.setShowBoard(false);
        newGame();

        for (int i=0; i<episodes; i++) {
            while (!game.isFinished()) {
                // UI update is run on the Application thread
                oneRound();
            }
            System.out.println(i + " : " + game.pegsLeft());
            epsilon = epsilon*epsilonDecay;
            newGame();
        }

        return true;
    }

    @Override
    public void show(int milliseconds) {
        epsilon = 0;
        newGame();
        game.setShowBoard(true);
        Thread thread = new Thread(() -> {
            Runnable playGame = this::oneRound;
            while (!game.isFinished()) {
                    // UI update is run on the Application thread
                Platform.runLater(playGame);
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException ex) { }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }


}
