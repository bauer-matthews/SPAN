package models.pfa;

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

    private final Multiset<StateProb> stateProbSet;

    public SymbolicTransition(Multiset<StateProb> stateProbSet) {
        this.stateProbSet = stateProbSet;
    }

    public Multiset<StateProb> getStateProbSet() {
        return stateProbSet;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SymbolicTransition)) {
            return false;
        }

        if (!stateProbSet.equals(((SymbolicTransition) o).stateProbSet)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateProbSet);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state prob set", stateProbSet)
                .toString();
    }
}
