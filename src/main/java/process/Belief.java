package process;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;

import java.util.Objects;

/**
 * Created by mbauer on 8/8/2017.
 */
public class Belief {

    private final State state;
    private final Apfloat prob;


    Belief(State state, Apfloat prob) {

        Objects.requireNonNull(state);
        Objects.requireNonNull(prob);

        this.state = state;
        this.prob = prob;
    }

    public Apfloat getProb() {
        return prob;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Belief)) {
            return false;
        }

        if (!state.equals(((Belief) o).state)) return false;
        if (!prob.equals(((Belief) o).prob)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, prob);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state", state.toString())
                .add("prob", prob.toString(true))
                .toString();
    }
}
