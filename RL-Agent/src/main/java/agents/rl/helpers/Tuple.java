package agents.rl.helpers;

public class Tuple<X, Y>{
    public final State x;
    public final Action y;
    public Tuple(State x, Action y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x.toString() + " -- " + y.toString() +")";
    }
}