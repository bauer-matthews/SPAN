package protocol.role;

import com.google.common.base.MoreObjects;
import rewriting.terms.VariableTerm;

import java.util.Objects;

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

    InputProcess(VariableTerm variable) {

        Objects.requireNonNull(variable);
        this.variable = variable;
    }

    public VariableTerm getVariable() {
        return variable;
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

        return equal(variable, ((InputProcess) o).variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variable", variable)
                .toString();
    }
}
