package protocol.role;

import cache.RewritingCache;
import cache.RunConfiguration;
import cache.SubstitutionCache;
import com.google.common.base.MoreObjects;
import rewriting.Equality;
import rewriting.terms.Term;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/11/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Guard {

    private final boolean positiveTest;
    private final Equality equality;

    public Guard(Equality equality, boolean positiveTest) {

        Objects.requireNonNull(equality);

        this.equality = equality;
        this.positiveTest = positiveTest;
    }

    public Equality getEquality() {
        return equality;
    }

    public boolean isPositiveTest() {
        return positiveTest;
    }

    public boolean check(List<Equality> substitution) throws ExecutionException {

        Equality groundGuard = SubstitutionCache.applySubstitution(equality, substitution);

        Term lhsNormalForm = RewritingCache.reduce(groundGuard.getLhs());

        Term rhsNormalForm = RewritingCache.reduce(groundGuard.getRhs());

        if (positiveTest && lhsNormalForm.equals(rhsNormalForm)) {
            return true;
        }

        if (!positiveTest && !lhsNormalForm.equals(rhsNormalForm)) {
            return true;
        }

        if (RunConfiguration.getTrace()) {
            System.out.println("GUARD TEST FAILED: " + lhsNormalForm.toMathString() + (positiveTest ? "==" : "!=") +
                    rhsNormalForm.toMathString());
            System.out.println();
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Guard)) {
            return false;
        }

        if (!equality.equals(((Guard) o).equality)) return false;
        if (positiveTest != ((Guard) o).positiveTest) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equality, positiveTest);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("equality", equality)
                .add("positive test", positiveTest)
                .toString();
    }
}
