package protocol.role;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;
import rewriting.Equality;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.FunctionTerm;
import rewriting.terms.Term;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class OutputProcess implements AtomicProcess {

    private final Collection<Guard> guards;
    private final Collection<ProbOutput> probOutputs;
    private final int phase;

    OutputProcess(Collection<Guard> guards, Collection<ProbOutput> probOutputs, int phase) {

        Objects.requireNonNull(guards);
        Objects.requireNonNull(probOutputs);

        this.guards = guards;
        this.probOutputs = probOutputs;
        this.phase = phase;

        Apfloat sum = Apfloat.ZERO;
        for (ProbOutput pout : probOutputs) {
            sum = sum.add(pout.getProbability());
        }

        if (!sum.equals(Apfloat.ONE)) {
            throw new IllegalArgumentException(Resources.INVALID_PROBS);
        }
    }

    public Collection<Guard> getGuards() {
        return guards;
    }


    public Collection<ProbOutput> getProbOutputs() {
        return probOutputs;
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public boolean isOutput() {
        return true;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof OutputProcess)) {
            return false;
        }

        if (!guards.equals(((OutputProcess) o).guards)) return false;
        if (!probOutputs.equals(((OutputProcess) o).probOutputs)) return false;
        if (phase != (((OutputProcess) o).phase)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guards, probOutputs, phase);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("guards", guards.toString())
                .add("probabilistic outputs", probOutputs.toString())
                .add("phase", phase)
                .toString();
    }
}
