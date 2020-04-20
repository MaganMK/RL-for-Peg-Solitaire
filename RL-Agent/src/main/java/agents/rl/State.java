package agents.rl;

import java.util.List;

public class State {

    private List<Integer> state;

    public State(List<Integer> state) { this.state = state; }

    public void setState(List<Integer> state) { this.state = state; }
    public List<Integer> getState() { return state; }

    @Override
    public String toString() {
        return state.toString();
    }
}
