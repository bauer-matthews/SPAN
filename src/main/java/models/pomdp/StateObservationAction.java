package models.pomdp;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class StateObservationAction {

    private final int stateIndex;
    private final int observationIndex;
    private final int actionIndex;

    public StateObservationAction(int stateIndex, int observationIndex, int actionIndex) {

        this.stateIndex = stateIndex;
        this.observationIndex = observationIndex;
        this.actionIndex = actionIndex;
    }

    public int getActionIndex() {
        return actionIndex;
    }

    public int getObservationIndex() {
        return observationIndex;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof StateObservationAction)) {
            return false;
        }

        if (stateIndex != ((StateObservationAction) o).stateIndex) return false;
        if (observationIndex != ((StateObservationAction) o).observationIndex) return false;
        if (actionIndex != ((StateObservationAction) o).actionIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateIndex, observationIndex, actionIndex);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state index", stateIndex)
                .add("observation index", observationIndex)
                .add("action index", actionIndex)
                .toString();
    }
}
