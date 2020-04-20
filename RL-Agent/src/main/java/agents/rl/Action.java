package agents.rl;

public class Action {

    private String from;
    private String to;

    public Action(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }

    @Override
    public String toString() {
        return "(" + from + ":" + to + ")";
    }
}
