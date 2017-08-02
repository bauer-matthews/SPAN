package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by matt on 7/26/17.
 */
public class FrameVariableTerm implements Term {

    private final VariableTerm variableTerm;
    private final int index;

    public FrameVariableTerm(VariableTerm variableTerm, int index) {

        Objects.requireNonNull(variableTerm);

        this.variableTerm = variableTerm;
        this.index = index;
    }

    public int getRole() {
        return index;
    }

    public VariableTerm getName() {
        return variableTerm;
    }

    @Override
    public Collection<VariableTerm> getVariables() {
        return Collections.singletonList(variableTerm);
    }

    @Override
    public Term substitute(VariableTerm var, Term term) {

        if (variableTerm.equals(var)) {
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
        return variableTerm.toMathString() + index;
    }


    @Override
    public boolean equals(Object o) {

        if (!(o instanceof VariableTerm)) {
            return false;
        }

        if (!(variableTerm.equals(((FrameVariableTerm) o).variableTerm))) return false;
        if (!(index == ((FrameVariableTerm) o).index)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableTerm, index);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variable term", variableTerm.toString())
                .add("index", index)
                .toString();
    }
}
