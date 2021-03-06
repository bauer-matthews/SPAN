package models.pomdp;

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
public class StateObsProb {

    private final int stateIndex;
    private final int obsIndex;
    private final Aprational prob;


    public StateObsProb(int stateIndex, int obsIndex, Aprational prob) {

        this.stateIndex = stateIndex;
        this.obsIndex = obsIndex;
        this.prob = prob;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public int getObsIndex() {
        return obsIndex;
    }

    public Aprational getProb() {
        return prob;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof StateObsProb)) {
            return false;
        }

        if (stateIndex != ((StateObsProb) o).stateIndex) return false;
        if (obsIndex != ((StateObsProb) o).obsIndex) return false;
        if (!prob.equals(((StateObsProb) o).prob)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateIndex, obsIndex, prob);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state index", stateIndex)
                .add("observation index", obsIndex)
                .add("prob", prob.toString(true))
                .toString();
    }
}
