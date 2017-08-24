package process;

import com.google.common.base.MoreObjects;
import org.apfloat.Aprational;

import java.util.Objects;

/**
 * Created by mbauer on 8/2/2017.
 */
public class Transition {

    private final Aprational transitionProbability;
    private final State originalState;
    private final State newState;

    Transition(Aprational transitionProbability, State originalState, State newState) {

        Objects.requireNonNull(transitionProbability);
        Objects.requireNonNull(originalState);
        Objects.requireNonNull(newState);

        this.transitionProbability = transitionProbability;
        this.originalState = originalState;
        this.newState = newState;
    }

    public Aprational getTransitionProbability() {
        return transitionProbability;
    }

    public State getOriginalState() {
        return originalState;
    }

    public State getNewState() {
        return newState;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Transition)) {
            return false;
        }

        if (!transitionProbability.equals(((Transition) o).transitionProbability)) return false;
        if (!originalState.equals(((Transition) o).originalState)) return false;
        if (!newState.equals(((Transition) o).newState)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transitionProbability, originalState, newState);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transition probability", transitionProbability.toString(true))
                .add("original state", originalState.toString())
                .add("new state", newState.toString())
                .toString();
    }
}
