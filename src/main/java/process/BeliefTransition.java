package process;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;

import java.util.Objects;

/**
 * Created by mbauer on 8/8/2017.
 */
public class BeliefTransition {

    private final Apfloat transitionProbability;
    private final BeliefState beliefState;

    BeliefTransition(Apfloat transitionProbability, BeliefState beliefState) {

        Objects.requireNonNull(transitionProbability);
        Objects.requireNonNull(beliefState);

        this.transitionProbability = transitionProbability;
        this.beliefState = beliefState;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof BeliefTransition)) {
            return false;
        }

        if (!transitionProbability.equals(((BeliefTransition) o).transitionProbability)) return false;
        if (!beliefState.equals(((BeliefTransition) o).beliefState)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transitionProbability, beliefState);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transition probability", transitionProbability.toString(true))
                .add("belief state", beliefState.toString())
                .toString();
    }
}
