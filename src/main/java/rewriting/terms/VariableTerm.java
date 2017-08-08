package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class VariableTerm implements Term {

    private final String variable;

    public VariableTerm(String termString) {

        Objects.requireNonNull(termString);

        this.variable = termString;
    }

    public String getName() {
        return variable;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof VariableTerm)) {
            return false;
        }

        return variable.equalsIgnoreCase(((VariableTerm) o).variable);
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

    @Override
    public Collection<VariableTerm> getVariables() {
        return Collections.singletonList(this);
    }

    @Override
    public Collection<NameTerm> getPrivateNames() {
        return Collections.emptyList();
    }

    @Override
    public Term substitute(VariableTerm var, Term term) {

        if (this.equals(var)) {
            return term;
        } else {
            return this;
        }
    }

    @Override
    public boolean isNameTerm() {
        return false;
    }

    @Override
    public boolean isVariableTerm() {
        return true;
    }

    @Override
    public boolean isCompoundTerm() {
        return false;
    }

    @Override
    public String toMathString() {
        return variable;
    }
}
