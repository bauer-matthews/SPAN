package protocol.role;

import com.google.common.base.MoreObjects;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Objects.equal;

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

    InputProcess(VariableTerm variable, Optional<Term> inputGuard) {

        Objects.requireNonNull(variable);
        Objects.requireNonNull(inputGuard);

        this.variable = variable;
        this.inputGuard = inputGuard;
    }

    public VariableTerm getVariable() {
        return variable;
    }

    public Optional<Term> getInputGuard() {
        return inputGuard;
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

        if (! (o instanceof InputProcess)) {
            return false;
        }

        if(!variable.equals(((InputProcess) o).variable)) return false;
        if(!inputGuard.equals(((InputProcess) o).inputGuard)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, inputGuard);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variable", variable)
                .add("input guard", inputGuard)
                .toString();
    }
}
