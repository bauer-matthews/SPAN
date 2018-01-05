package pomdp;

import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.Set;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SymbolicTransition {


    private final StateObservationAction stateAction;
    private final Set<StateObsProb> stateProbSet;

    SymbolicTransition(StateObservationAction stateAction, Set<StateObsProb> stateProbSet) {
        this.stateAction = stateAction;
        this.stateProbSet = stateProbSet;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SymbolicTransition)) {
            return false;
        }

        if (stateAction != ((SymbolicTransition) o).stateAction) return false;
        if (!stateProbSet.equals(((SymbolicTransition) o).stateProbSet)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateAction, stateProbSet);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state action", stateAction)
                .add("state prob set", stateProbSet)
                .toString();
    }

}
