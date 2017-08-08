package process;

import com.google.common.base.MoreObjects;
import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;

import java.util.List;
import java.util.Objects;

/**
 * Created by mbauer on 8/8/2017.
 */
public class BeliefState {

    private final List<Belief> beliefs;

    public BeliefState(List<Belief> beliefs) {

        Objects.requireNonNull(beliefs);
        this.beliefs = beliefs;
    }

    public List<Belief> getBeliefs() {
        return beliefs;
    }

    public Apfloat getProb(State state) {
        for (Belief belief : beliefs) {
            if (belief.getState().equals(state)) {
                return belief.getProb();
            }
        }

        return Apcomplex.ZERO;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof BeliefState)) {
            return false;
        }

        if (!beliefs.equals(((BeliefState) o).beliefs)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beliefs);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("beliefs", beliefs.toString())
                .toString();
    }
}
