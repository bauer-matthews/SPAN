package protocol.role;

import com.google.common.base.MoreObjects;
import protocol.Metadata;
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
public class InputAction implements Action {

    private final VariableTerm variable;

    public InputAction(VariableTerm variable) {

        Objects.requireNonNull(variable);
        this.variable = variable;
    }

    @Override
    public boolean equals(Object o) {

        if (! (o instanceof InputAction)) {
            return false;
        }

        return equal(variable, ((InputAction) o).variable);
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
