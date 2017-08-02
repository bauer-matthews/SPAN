package rewriting;

import com.google.common.base.MoreObjects;
import rewriting.terms.NameTerm;
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
public class Rewrite {

    private final Term lhs;
    private final Term rhs;

    public Rewrite(Term lhs, Term rhs) {

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

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Rewrite)) {
            return false;
        }

        if (!this.lhs.equals(((Rewrite) o).lhs)) return false;
        if (!this.rhs.equals(((Rewrite) o).rhs)) return false;

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
