package models;

import process.State;

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
class StateIndexer {

    // Mapping from state to observation index
    private final Map<State, Integer> stateMap;
    private int freshIndex;

    StateIndexer() {

        stateMap = new HashMap<>();
        freshIndex = 0;
    }

    Integer getStateIndex(State state) {

        Integer index = stateMap.get(state);

        if (index == null) {
            index = freshIndex;
            stateMap.put(state, freshIndex);
            freshIndex++;
        }

        return index;
    }

    int getNumStates() {
        return stateMap.size();
    }

    void reset() {
        stateMap.clear();
        freshIndex = 0;
    }
}
