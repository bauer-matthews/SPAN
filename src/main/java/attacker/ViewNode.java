package attacker;

import org.apfloat.Aprational;
import process.Observation;

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
public class ViewNode implements Node {

    private final Observation observation;
    private final List<Node> children;

    private Aprational attackProb;
    private Long index;

    public ViewNode(Observation observation) {

        Objects.requireNonNull(observation);

        this.observation = observation;
        this.children = new ArrayList<>();
    }

    public Observation getObservation() {
        return observation;
    }

    public void setAttackProb(Aprational attackProb) {
        this.attackProb = attackProb;
    }

    public Aprational getAttackProb() {

        if (attackProb == null) {
            throw new UnsupportedOperationException("Attack prob has not been initialized");
        }

        return attackProb;
    }

    @Override
    public Node getRoot() {
        return this;
    }

    @Override
    public Aprational getAttackProbability() {

        if (children.size() == 0) {
            return attackProb;
        } else {

            Aprational attackProb = Aprational.ZERO;
            for (Node child : children) {
                attackProb = attackProb.add(child.getAttackProbability());
            }

            return attackProb;
        }
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
        sb.append(observation.toString());
        sb.append(", Attack Probability=");
        sb.append(attackProb.toString(true));
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
    public boolean equals(Object o) {

        if (!(o instanceof ViewNode)) {
            return false;
        }

        if (!observation.equals(((ViewNode) o).observation)) return false;
        if (!children.equals(((ViewNode) o).children)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(observation);
    }

    @Override
    public String toString() {

        return NodePrinter.printNode(this);
    }
}
