package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class FunctionTerm implements Term {

    private final FunctionSymbol rootSymbol;
    private final List<Term> subterms;



    public FunctionTerm(FunctionSymbol rootSymbol, List<Term> subterms) {

        Objects.requireNonNull(rootSymbol);
        Objects.requireNonNull(subterms);

        if(subterms.size() != rootSymbol.getNumArgs()) {
            throw new IllegalArgumentException(Resources.INVALID_NUM_SUBTERMS);
        }

        this.rootSymbol = rootSymbol;
        this.subterms = subterms;
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof FunctionTerm)) {
            return false;
        }

        if(this.rootSymbol != ((FunctionTerm) o).rootSymbol) {
            return false;
        }

        return subterms.equals(((FunctionTerm) o).subterms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootSymbol, subterms);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("root symbol", rootSymbol)
                .add("subterms", subterms)
                .toString();
    }

}
