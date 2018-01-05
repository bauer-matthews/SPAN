package pomdp;

import cache.EquivalenceCache;
import process.Observation;
import process.State;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class ObservationIndexer {

    private final Map<Observation, Long> observationMap;
    private final Map<Integer, Map<State, Long>> representativeMap;
    private long freshIndex;

    ObservationIndexer() {

        observationMap = new HashMap<>();
        representativeMap = new HashMap<>();
        freshIndex = 0;
    }

    long getObservationIndex(State state) throws IOException, InterruptedException, ExecutionException {

        Long index = observationMap.get(state.getObservation());

        if (index != null) {
            return index;
        } else {

            int frameSize = state.getFrame().size();
            representativeMap.computeIfAbsent(frameSize, k -> new HashMap<>());

            for (State representative : representativeMap.get(frameSize).keySet()) {

                if (EquivalenceCache.checkEquivalence(representative, state).isEquivalent()) {

                    index = representativeMap.get(frameSize).get(representative);
                    observationMap.put(state.getObservation(), index);
                    return index;
                }
            }

            index = freshIndex;

            if(!state.getFrame().isEmpty()) {
                representativeMap.get(frameSize).put(state, index);
            }

            observationMap.put(state.getObservation(), index);
            freshIndex++;
            return index;
        }
    }

    long getNumObservations() {
        return observationMap.size();
    }

    void reset() {
        observationMap.clear();
        representativeMap.clear();
        freshIndex = 0;
    }
}
