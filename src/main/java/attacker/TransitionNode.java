package attacker;

import org.apfloat.Aprational;
import process.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class TransitionNode implements Node {

    private final Action action;
    private final Aprational transitionProb;
    private final List<Node> children;

    private Long index;

    public TransitionNode(Action action, Aprational transitionProb) {

        Objects.requireNonNull(action);
        Objects.requireNonNull(transitionProb);

        this.action = action;
        this.transitionProb = transitionProb;
        this.children = new ArrayList<>();
    }

    public Action getAction() {
        return action;
    }

    public Aprational getTransitionProb() {
        return transitionProb;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public void addChild(Tree tree) {
        children.add(tree.getRoot());
    }

    @Override
    public long getIndex() {

        if (index == null) {
            throw new UnsupportedOperationException("Node index has not be initialized");
        }

        return index;
    }

    @Override
    public void setIndex(long index) {

        this.index = index;
    }

    @Override
    public String getState() {

        StringBuilder sb = new StringBuilder();
        sb.append("State{");
        sb.append(action.toString());
        sb.append(", ");
        sb.append(transitionProb.toString(true));
        sb.append("}");

        return sb.toString();
    }

    @Override
    public void appendDotLines(List<String> lines) {

        lines.add(NodePrinter.getDotString(this));
        NodePrinter.addChildEdges(this, lines);

        for (Node child : children) {
            child.appendDotLines(lines);
        }
    }

    @Override
    public Node getRoot() {
        return this;
    }

    @Override
    public Aprational getAttackProbability() {

        Aprational attackProb = Aprational.ZERO;

        for (Node child : children) {
            attackProb = attackProb.add(transitionProb.multiply(child.getAttackProbability()));
        }

        return attackProb;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof TransitionNode)) {
            return false;
        }

        if (!action.equals(((TransitionNode) o).action)) return false;
        if (!transitionProb.equals(((TransitionNode) o).transitionProb)) return false;
        if (!children.equals(((TransitionNode) o).children)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, transitionProb, children);
    }

    @Override
    public String toString() {
        return NodePrinter.printNode(this);
    }
}
