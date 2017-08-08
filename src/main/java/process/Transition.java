package process;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;

import java.util.Objects;

/**
 * Created by mbauer on 8/2/2017.
 */
public class Transition {

    private final Apfloat transitionProbability;
    private final State state;

    Transition(Apfloat transitionProbability, State state) {

        Objects.requireNonNull(transitionProbability);
        Objects.requireNonNull(state);

        this.transitionProbability = transitionProbability;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Transition)) {
            return false;
        }

        if (!transitionProbability.equals(((Transition) o).transitionProbability)) return false;
        if (!state.equals(((Transition) o).state)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transitionProbability, state);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transition probability", transitionProbability.toString(true))
                .add("state", state.toString())
                .toString();
    }
}
