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
public class NameTerm implements Term {

    private final String name;
    private final boolean protocolPrivate;

    public NameTerm(String termString, boolean protocolPrivate) {

        Objects.requireNonNull(termString);

        this.name = termString;
        this.protocolPrivate = protocolPrivate;
    }

    public String getName() {
        return name;
    }

    @Override
    public Collection<VariableTerm> getVariables() {
        return Collections.emptyList();
    }

    @Override
    public Term substitute(VariableTerm var, Term term) {
        return this;
    }

    @Override
    public boolean isNameTerm() {
        return true;
    }

    @Override
    public boolean isVariableTerm() {
        return false;
    }

    @Override
    public boolean isCompoundTerm() {
        return false;
    }

    @Override
    public String toMathString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof NameTerm)) {
            return false;
        }

        if (this.protocolPrivate != ((NameTerm) o).protocolPrivate) {
            return false;
        }

        return name.equalsIgnoreCase(((NameTerm) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, protocolPrivate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("private", protocolPrivate)
                .toString();
    }
}
