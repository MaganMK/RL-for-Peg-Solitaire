package agents.rl.nn;

import static agents.rl.nn.NN.INITIALELIGIBILITY;
import static agents.rl.nn.NN.INITIALWEIGHT;

public class Edge {

    Node from;
    Node to;
    double weight;
    double eligibility;

    public Edge(Node from, Node to){
        this.from = from;
        this.to = to;
        this.weight = INITIALWEIGHT;
        this.eligibility = INITIALELIGIBILITY;
    }

    public void setWeight(double weight) {
        if(weight < 0) {
            this.weight = 0;
        } else {
            this.weight = weight;
        }
    }

    public void setEligibility(double eligibility) {
        this.eligibility = eligibility;
    }
}
