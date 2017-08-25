package attacker;

import org.apfloat.Aprational;

import java.util.ArrayList;
import java.util.Collection;
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
public class AttackTree implements Tree {

    private final Node rootNode;
    private List<Node> nodes = new ArrayList<>();

    public AttackTree(Node rootNode, List<Node> nodes) {

        Objects.requireNonNull(rootNode);
        Objects.requireNonNull(nodes);

        this.rootNode = rootNode;
        this.nodes.addAll(nodes);

        rootNode.setIndex(0);
    }

    @Override
    public Node getRoot() {
        return rootNode;
    }

    @Override
    public Aprational getAttackProbability() {
        return rootNode.getAttackProbability();
    }

    public List<String> getDotLines() {

        List<String> dotLines = new ArrayList<>();

        dotLines.add("digraph G {");
        rootNode.appendDotLines(dotLines);
        dotLines.add("}");

        return dotLines;
    }

    public Collection<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {

        node.setIndex(nodes.size());
        this.nodes.add(node);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof AttackTree)) {
            return false;
        }

        if (!rootNode.equals(((AttackTree) o).rootNode)) return false;
        if (!nodes.equals(((AttackTree) o).nodes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootNode, nodes);
    }

    @Override
    public String toString() {
        return rootNode.toString();
    }
}
