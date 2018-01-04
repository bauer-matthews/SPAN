package pomdp;

import com.google.common.base.MoreObjects;
import org.apfloat.Aprational;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class StateProb {

    private final long stateIndex;
    private final Aprational prob;


    public StateProb(long stateIndex, Aprational prob) {

        this.stateIndex = stateIndex;
        this.prob = prob;
    }

    public long getStateIndex() {
        return stateIndex;
    }

    public Aprational getProb() {
        return prob;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof StateProb)) {
            return false;
        }

        if (stateIndex != ((StateProb) o).stateIndex) return false;
        if (!prob.equals(((StateProb) o).prob)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateIndex, prob);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state index", stateIndex)
                .add("prob", prob.toString(true))
                .toString();
    }
}
