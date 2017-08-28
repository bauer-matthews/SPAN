package attacker;

import cache.GlobalDataCache;
import org.apfloat.Aprational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

    public List<String> getDotLines() throws ExecutionException {

        List<String> dotLines = new ArrayList<>();

        dotLines.add("digraph G {");
        rootNode.appendDotLines(dotLines);
        dotLines.add("}");

        return dotLines;
    }

    public boolean attackFound() {

        return GlobalDataCache.getAttackTree().getAttackProbability()
                .compareTo(Aprational.ONE.subtract(GlobalDataCache.getProtocol()
                        .getSafetyProperty().getProbability())) > 0;
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
