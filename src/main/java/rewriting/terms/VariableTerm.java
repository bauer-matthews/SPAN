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
    private final Sort sort;

    public VariableTerm(String termString, Sort sort) {

        Objects.requireNonNull(termString);
        Objects.requireNonNull(sort);

        this.variable = termString;
        this.sort = sort;
    }

    public String getName() {
        return variable;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public int getSize() {
        return 1;
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
    public boolean isGroundTerm() {
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
    public boolean hasSort(Sort sort) {
        return SortFactory.hasSort(this.sort, sort);
    }

    @Override
    public String toMathString() {
        return variable;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof VariableTerm)) {
            return false;
        }

        if(!(sort.equals(((VariableTerm) o).sort))) return false;

        return variable.equalsIgnoreCase(((VariableTerm) o).variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, sort);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variable", variable)
                .add("sort", sort)
                .toString();
    }
}
