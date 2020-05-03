package agents.rl;

import java.util.HashMap;
import java.util.List;

import agents.interfaces.AgentInterface;
import agents.rl.helpers.Action;
import agents.rl.helpers.Chart;
import agents.rl.helpers.State;
import game.interfaces.BoardInterface;
import javafx.application.Platform;
import org.jfree.ui.RefineryUtilities;

public class RLAgent implements AgentInterface {

    private Actor actor;
    private Critic critic;
    private int episodes;

    BoardInterface game;
    State currentState;
    State lastState;
    Action currentAction;
    Action lastAction;
    double epsilon;
    double epsilonDecay;

    private HashMap<List<Integer>, State> allStates = new HashMap<>();


    public RLAgent(BoardInterface game, int episodes, double actorLearningRate, double criticLearningRate,
                   double actorEligibilityDecayRate, double criticEligibilityDecayRate, double actorDiscountFactor,
                   double criticDiscountFactor, double epsilon, double epsilonDecayRate, List<Integer> nn) {

        this.game = game;
        this.episodes = episodes;
        this.epsilon = epsilon;
        this.epsilonDecay = epsilonDecayRate;

        this.actor = new Actor(this, actorLearningRate, actorEligibilityDecayRate,
                actorDiscountFactor);

        if(nn.isEmpty()) {
            this.critic = new CriticValueMap(this, criticLearningRate, criticEligibilityDecayRate, criticDiscountFactor);

        } else {
            this.critic = new CriticNN(this, criticLearningRate, criticEligibilityDecayRate, criticDiscountFactor, nn);
        }

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

    private void bestEffort() {
        game.makeMove(currentAction.getFrom(), currentAction.getTo());
        lastState = currentState;
        lastAction = currentAction;
        updateState(game.getState());
        if(!game.isFinished()) {
            currentAction = actor.getAction(currentState);
        }
    }


    private void makeMove() {

        int reward = game.makeMove(currentAction.getFrom(), currentAction.getTo());

        lastState = currentState;
        lastAction = currentAction;
        updateState(game.getState());


        double rdError = critic.getRDError(lastState, reward, currentState);

        actor.setEligibility(lastState, lastAction);

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

        Chart chart = new Chart("Training plot");

        for (int i=0; i<episodes; i++) {
            while (!game.isFinished()) {
                makeMove();
            }
            System.out.println(i + " : " + game.pegsLeft());
            chart.addPegsLeft(i, game.pegsLeft());
            chart.addEpsilon(i, epsilon);
            if(epsilon-epsilonDecay >= 0){
                epsilon -= epsilonDecay;
            } else {
                epsilon = 0;
            }
            newGame();
        }

        chart.makeChart();
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        return true;
    }

    @Override
    public void show(int milliseconds) {
        epsilon = 0;
        newGame();
        game.setShowBoard(true);
        Thread thread = new Thread(() -> {
            Runnable playGame = this::bestEffort;
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
