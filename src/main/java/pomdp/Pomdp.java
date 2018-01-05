package pomdp;

import com.google.common.base.MoreObjects;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Pomdp {

    private final Map<StateObservationAction, Collection<StateObsProb>> transitions;

    private final long numObservations;
    private final long numStates;
    private final long numActions;

    public Pomdp(long numStates, long numActions, long numObservations,
                 Map<StateObservationAction, Collection<StateObsProb>> transitions ) {

        Objects.requireNonNull(transitions);

        this.numStates = numStates;
        this.numActions = numActions;
        this.numObservations = numObservations;
        this.transitions = transitions;
    }

    public long getNumObservations() {
        return numObservations;
    }

    public long getNumActions() {
        return numActions;
    }

    public long getNumStates() {
        return numStates;
    }

    public Map<StateObservationAction, Collection<StateObsProb>> getTransitions() {
        return transitions;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Pomdp)) {
            return false;
        }

        if (numStates != ((Pomdp) o).numStates) return false;
        if (numActions != ((Pomdp) o).numActions) return false;
        if (numObservations != ((Pomdp) o).numObservations) return false;
        if (!transitions.equals(((Pomdp) o).transitions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numActions, numObservations, numStates, transitions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("num states", numStates)
                .add("num actions", numActions)
                .add("num observations", numObservations)
                .add("transitions", transitions)
                .toString();
    }
}
