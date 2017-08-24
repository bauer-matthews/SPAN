package attacker;

import java.util.List;
import java.util.Optional;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface Node extends Tree {

    List<Node> getChildren();

    void addChild(Tree tree);

    long getIndex();

    void setIndex(long index);

    String getState();
}
