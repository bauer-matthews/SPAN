package models;

import cache.EquivalenceCache;
import equivalence.EquivalenceCheckResult;
import process.Observation;
import process.State;
import rewriting.terms.Term;
import util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by matt on 3/8/18.
 */
public class ObsAttackIndexer {

    private final Map<Observation, Pair<Integer, Boolean>> observationMap;
    private final Map<Integer, List<State>> representativeMap;
    private int freshIndex;

    public ObsAttackIndexer() {

        observationMap = new HashMap<>();
        representativeMap = new HashMap<>();
        freshIndex = 0;
    }

    public Pair<Integer, Boolean> getObservationAttackPair(State state)
            throws IOException, InterruptedException, ExecutionException {

        Pair<Integer, Boolean> obsAttackPair = observationMap.get(state.getObservation());
        EquivalenceCheckResult result = null;

        if (obsAttackPair != null) {
            return obsAttackPair;
        } else {

            int frameSize = state.getFrame().size();

            if (representativeMap.get(frameSize) == null) {
                representativeMap.put(frameSize, new ArrayList<State>());
            }

            for (State representative : representativeMap.get(frameSize)) {

                result = EquivalenceCache.checkEquivalence(representative, state);

                if (result.isEquivalent()) {

                    obsAttackPair = getObservationAttackPair(representative);
                    observationMap.put(state.getObservation(), obsAttackPair);
                    return obsAttackPair;
                }
            }

            if (!state.getFrame().isEmpty()) {
                representativeMap.get(frameSize).add(state);
            }

            boolean isAttack;
            if (result != null) {
                isAttack = result.isPhi2attack();
            } else {
                isAttack = EquivalenceCache.checkEquivalence(state, state).isPhi2attack();
            }

            obsAttackPair = new Pair<>(freshIndex, isAttack);
            observationMap.put(state.getObservation(), obsAttackPair);
            freshIndex++;
            return obsAttackPair;
        }
    }

    public void reset() {
        observationMap.clear();
        representativeMap.clear();
        freshIndex = 0;
    }
}
