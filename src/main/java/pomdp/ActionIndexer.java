package pomdp;

import process.Action;

import java.util.HashMap;
import java.util.Map;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class ActionIndexer {

    // Mapping from state to observation index
    private final Map<Action, Long> actionMap;
    private long freshIndex;

    ActionIndexer() {

        actionMap = new HashMap<>();
        freshIndex = 0;
    }

    long getActionIndex(Action action) {

        Long index = actionMap.get(action);

        if (index == null) {
            index = freshIndex;
            actionMap.put(action, freshIndex);
            freshIndex++;
        }

        return index;
    }

    long getNumActions () {
        return actionMap.size();
    }

    void reset() {
        actionMap.clear();
        freshIndex = 0;
    }
}
