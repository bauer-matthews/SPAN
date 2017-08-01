package rewriting;

import com.google.common.base.MoreObjects;
import rewriting.terms.Term;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Equality {

    private Term lhs;
    private Term rhs;

    public Equality(Term lhs, Term rhs) {

        Objects.requireNonNull(lhs);
        Objects.requireNonNull(rhs);

        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Term getLhs() {
        return lhs;
    }

    public Term getRhs() {
        return rhs;
    }

    public void setLhs(Term lhs) {
        this.lhs = lhs;
    }

    public void setRhs(Term rhs) {
        this.rhs = rhs;
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof Equality)) {
            return false;
        }

        if(!this.lhs.equals(((Equality) o).lhs)) return false;
        if(!this.rhs.equals(((Equality) o).rhs)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("lhs", lhs.toMathString())
                .add("rhs", rhs.toMathString())
                .toString();
    }
}
