package models;

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
public class ObservationIndexer {

    private final Map<Observation, Integer> observationMap;
    private final Map<Integer, Map<State, Integer>> representativeMap;
    private int freshIndex;

    public ObservationIndexer() {

        observationMap = new HashMap<>();
        representativeMap = new HashMap<>();
        freshIndex = 0;
    }

    public int getObservationIndex(State state) throws IOException, InterruptedException, ExecutionException {

        Integer index = observationMap.get(state.getObservation());

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

            if (!state.getFrame().isEmpty()) {
                representativeMap.get(frameSize).put(state, index);
            }

            observationMap.put(state.getObservation(), index);
            freshIndex++;
            return index;
        }
    }

    public int getNumObservations() {
        return observationMap.size();
    }

    public void reset() {
        observationMap.clear();
        representativeMap.clear();
        freshIndex = 0;
    }
}
