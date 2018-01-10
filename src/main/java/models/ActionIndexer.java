package models;

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
    private final Map<Action, Integer> actionMap;
    private int freshIndex;

    ActionIndexer() {

        actionMap = new HashMap<>();
        freshIndex = 0;
    }

    int getActionIndex(Action action) {

        Integer index = actionMap.get(action);

        if (index == null) {
            index = freshIndex;
            actionMap.put(action, freshIndex);
            freshIndex++;
        }

        return index;
    }

    int getNumActions() {
        return actionMap.size();
    }

    void reset() {
        actionMap.clear();
        freshIndex = 0;
    }
}
