package models.pomdp;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Multiset;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SymbolicTransition {


    private final StateObservationAction stateObservationAction;
    private final Multiset<StateObsProb> stateObsProbSet;

    public SymbolicTransition(StateObservationAction stateObservationAction, Multiset<StateObsProb> stateObsProbSet) {
        this.stateObservationAction = stateObservationAction;
        this.stateObsProbSet = stateObsProbSet;
    }

    public StateObservationAction getStateObservationAction() {
        return stateObservationAction;
    }

    public Multiset<StateObsProb> getStateObsProbSet() {
        return stateObsProbSet;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SymbolicTransition)) {
            return false;
        }

        if (stateObservationAction != ((SymbolicTransition) o).stateObservationAction) return false;
        if (!stateObsProbSet.equals(((SymbolicTransition) o).stateObsProbSet)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateObservationAction, stateObsProbSet);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state action", stateObservationAction)
                .add("state observation prob set", stateObsProbSet)
                .toString();
    }
}
