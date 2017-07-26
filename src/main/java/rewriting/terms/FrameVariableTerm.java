package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by matt on 7/26/17.
 */
public class FrameVariableTerm implements Term{

    private final VariableTerm variableTerm;
    private final int role;

    public FrameVariableTerm(VariableTerm variableTerm, int role) {

        Objects.requireNonNull(variableTerm);

        this.variableTerm = variableTerm;
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public VariableTerm getName() {
        return variableTerm;
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof VariableTerm)) {
            return false;
        }

        if(!(variableTerm.equals(((FrameVariableTerm) o).variableTerm))) return false;
        if(!(role == ((FrameVariableTerm) o).role)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableTerm, role);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variable term", variableTerm.toString())
                .add("role", role)
                .toString();
    }
}
