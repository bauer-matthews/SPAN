package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/10/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SpecialTerm implements Term {

    private final String name;

    public SpecialTerm(String name) {
        this.name = name;
    }

    @Override
    public Collection<VariableTerm> getVariables() {
        return Collections.emptyList();
    }

    @Override
    public Collection<NameTerm> getPrivateNames() {
        return Collections.emptyList();
    }

    @Override
    public Term substitute(VariableTerm var, Term term) {
        return this;
    }

    @Override
    public boolean isNameTerm() {
        return false;
    }

    @Override
    public boolean isGroundTerm() {
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
    public boolean hasSort(Sort sort) {
        return sort.equals(SortFactory.SPECIAL);
    }

    @Override
    public String toMathString() {
        return name;
    }

    @Override
    public Sort getSort() {
        return SortFactory.SPECIAL;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SpecialTerm)) {
            return false;
        }

        return name.equalsIgnoreCase(((SpecialTerm) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
