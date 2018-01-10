package models.pomdp;

import com.google.common.base.MoreObjects;

import java.util.List;
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

    private final Map<Integer, List<SymbolicTransition>> transitions;

    private final int numObservations;
    private final int numStates;
    private final int numActions;

    public Pomdp(int numStates, int numActions, int numObservations,
                 Map<Integer, List<SymbolicTransition>> transitions ) {

        Objects.requireNonNull(transitions);

        this.numStates = numStates;
        this.numActions = numActions;
        this.numObservations = numObservations;
        this.transitions = transitions;
    }

    public int getNumObservations() {
        return numObservations;
    }

    public int getNumActions() {
        return numActions;
    }

    public int getNumStates() {
        return numStates;
    }

    public Map<Integer, List<SymbolicTransition>> getTransitions() {
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
