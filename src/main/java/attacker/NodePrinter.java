package attacker;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class NodePrinter {

    private static final Joiner COMMA_JOINER;

    static {
        COMMA_JOINER = Joiner.on(", ");
    }

    static String printNode(Node node) {

        StringBuilder sb = new StringBuilder();
        sb.append("NODE(");
        sb.append(node.getIndex());
        sb.append(") [ ");

        List<Long> childIndicies = new ArrayList<>();
        for (Node child : node.getChildren()) {
            childIndicies.add(child.getIndex());
        }

        sb.append("Children{");
        sb.append(COMMA_JOINER.join(childIndicies));
        sb.append("} ");

        sb.append(node.getState());
        sb.append(" ]\n");

        for (Node child : node.getChildren()) {
            sb.append(child.toString());
        }

        return sb.toString();
    }

    static String getDotString(ViewNode node) {

        StringBuilder sb = new StringBuilder();
        sb.append("n");
        sb.append(node.getIndex());
        sb.append(" [ label=\"");
        sb.append(node.getObservation().toMathString());
        sb.append("\"");
        sb.append(" ]");
        return sb.toString();
    }

    static String getDotString(TransitionNode node) {

        StringBuilder sb = new StringBuilder();
        sb.append("n");
        sb.append(node.getIndex());
        sb.append(" [ label=\"(");

        sb.append(node.getAction().getRecipe().toMathString());
        sb.append(", ");
        sb.append(node.getAction().getRoleIndex());
        sb.append(") P=");
        sb.append(node.getTransitionProb().toString(true));
        sb.append("\"");
        sb.append(" ]");

        return sb.toString();
    }

    static void addChildEdges(Node node, List<String> lines) {
        for (Node child : node.getChildren()) {
            lines.add("n" + node.getIndex() + " -> n" + child.getIndex() + " [color=black];");
        }
    }
}
