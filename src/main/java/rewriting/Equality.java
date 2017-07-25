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

    private final Term lhs;
    private final Term rhs;

    public Equality(Term lhs, Term rhs) {

        Objects.requireNonNull(lhs);
        Objects.requireNonNull(rhs);

        this.lhs = lhs;
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
                .add("lhs", lhs)
                .add("rhs", rhs)
                .toString();
    }
}
