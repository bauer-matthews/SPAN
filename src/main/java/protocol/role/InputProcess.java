package protocol.role;

import com.google.common.base.MoreObjects;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class InputProcess implements AtomicProcess {

    private final VariableTerm variable;
    private final Optional<Term> inputGuard;
    private final int phase;

    InputProcess(VariableTerm variable, Optional<Term> inputGuard, int phase) {

        Objects.requireNonNull(variable);
        Objects.requireNonNull(inputGuard);

        this.variable = variable;
        this.inputGuard = inputGuard;
        this.phase = phase;
    }

    public VariableTerm getVariable() {
        return variable;
    }

    public Optional<Term> getInputGuard() {
        return inputGuard;
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public Collection<VariableTerm> getVariables() {
        return Collections.singletonList(variable);
    }

    @Override
    public AtomicProcess appendBranchIndexToVars(int index) {
        return new InputProcess((VariableTerm) variable.appendBranchIndexToVars(index), inputGuard, phase);
    }

    @Override
    public boolean isOutput() {
        return false;
    }

    @Override
    public boolean isInput() {
        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof InputProcess)) {
            return false;
        }

        if (!variable.equals(((InputProcess) o).variable)) return false;
        if (!inputGuard.equals(((InputProcess) o).inputGuard)) return false;
        if (phase != ((InputProcess) o).phase) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, inputGuard, phase);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variable", variable)
                .add("input guard", inputGuard)
                .add("phase", phase)
                .toString();
    }
}
