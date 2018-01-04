package pomdp;

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
    private final Map<State, Long> stateMap;
    private long freshIndex;

    StateIndexer() {

        stateMap = new HashMap<>();
        freshIndex = 0;
    }

    Long getStateIndex(State state) {

        Long index = stateMap.get(state);

        if (index == null) {
            index = freshIndex;
            stateMap.put(state, freshIndex);
            freshIndex++;
        }

        return index;
    }

    long getNumStates() {
        return stateMap.size();
    }

    void reset() {
        stateMap.clear();
        freshIndex = 0;
    }
}
