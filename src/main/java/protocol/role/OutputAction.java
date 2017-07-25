package protocol.role;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;
import rewriting.Equality;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class OutputAction implements Action {

    Collection<Equality> guards;
    Collection<ProbOutput> probOutputs;

    OutputAction(Collection<Equality> guards, Collection<ProbOutput> probOutputs) {

        Objects.requireNonNull(guards);
        Objects.requireNonNull(probOutputs);

        this.guards = guards;
        this.probOutputs = probOutputs;

        Apfloat sum = Apfloat.ZERO;
        for(ProbOutput pout : probOutputs) {
            sum = sum.add(pout.getProbability());
        }

        if(!sum.equals(Apfloat.ONE)) {
            throw new IllegalArgumentException(Resources.INVALID_PROBS);
        }
    }

    @Override
    public boolean equals(Object o) {

        if (! (o instanceof OutputAction)) {
            return false;
        }

        if(!guards.equals(((OutputAction) o).guards)) return false;
        if(!probOutputs.equals(((OutputAction) o).probOutputs)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guards, probOutputs);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("guards", guards.toString())
                .add("probabilistic outputs", probOutputs.toString())
                .toString();
    }
}
