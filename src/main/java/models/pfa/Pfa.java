package models.pfa;

import com.google.common.base.MoreObjects;

import java.util.Map;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Pfa {

    private final Map<Integer, Map<Integer, SymbolicTransition>> transitions;

    private final int numStates;
    private final int numSymbols;
    private final int length;

    Pfa(int numStates, int numSymbols, int length, Map<Integer, Map<Integer, SymbolicTransition>> transitions) {

        Objects.requireNonNull(transitions);

        this.numStates = numStates;
        this.numSymbols = numSymbols;
        this.length = length;
        this.transitions = transitions;
    }

    public int getNumSymbols() {
        return numSymbols;
    }

    public int getNumStates() {
        return numStates;
    }

    public Map<Integer, Map<Integer, SymbolicTransition>> getTransitions() {
        return transitions;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Pfa)) {
            return false;
        }

        if (numStates != ((Pfa) o).numStates) return false;
        if (numSymbols != ((Pfa) o).numSymbols) return false;
        if (length != ((Pfa) o).length) return false;
        if (!transitions.equals(((Pfa) o).transitions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSymbols, length, numStates, transitions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("num states", numStates)
                .add("num symbols", numSymbols)
                .add("length", length)
                .add("transitions", transitions)
                .toString();
    }
}
