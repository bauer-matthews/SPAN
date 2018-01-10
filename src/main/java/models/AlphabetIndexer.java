package models;

import util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class AlphabetIndexer {

    // Mapping from state to observation index
    private final Map<Pair<Integer, Integer>, Integer> alphabetMap;
    private int freshIndex;

    AlphabetIndexer() {

        alphabetMap = new HashMap<>();
        freshIndex = 0;
    }

    public int getSymbolIndex(Pair<Integer, Integer> actionObs) {

        Integer index = alphabetMap.get(actionObs);

        if (index == null) {
            index = freshIndex;
            alphabetMap.put(actionObs, freshIndex);
            freshIndex++;
        }

        return index;
    }

    public int getAlphabetSize() {
        return alphabetMap.size();
    }

    void reset() {
        alphabetMap.clear();
        freshIndex = 0;
    }
}
