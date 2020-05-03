package agents.rl.nn;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    List<Node> nodes = new ArrayList<>();

    public Layer (int nodeCount) {
        for (int i = 0; i<nodeCount; i++) {
            nodes.add(new Node());
        }
    }

}
