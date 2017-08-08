package rewriting.terms;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Collection;
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

        if (subterms.size() != rootSymbol.getArity()) {
            throw new IllegalArgumentException(Resources.INVALID_NUM_SUBTERMS);
        }

        this.rootSymbol = rootSymbol;
        this.subterms = subterms;
    }

    public FunctionSymbol getRootSymbol() {
        return rootSymbol;
    }

    public List<Term> getSubterms() {
        return subterms;
    }

    @Override
    public Collection<VariableTerm> getVariables() {

        Collection<VariableTerm> variableTerms = new ArrayList<>();
        for (Term term : subterms) {
            variableTerms.addAll(term.getVariables());
        }

        return variableTerms;
    }

    @Override
    public Collection<NameTerm> getPrivateNames() {

        Collection<NameTerm> nameTerms = new ArrayList<>();
        for (Term term : subterms) {
            nameTerms.addAll(term.getPrivateNames());
        }

        return nameTerms;
    }

    @Override
    public Term substitute(VariableTerm var, Term term) {

        List<Term> newSubterms = new ArrayList<>();
        for (Term subterm : subterms) {
            newSubterms.add(subterm.substitute(var, term));
        }

        return new FunctionTerm(this.rootSymbol, newSubterms);
    }

    @Override
    public boolean isNameTerm() {
        return false;
    }

    @Override
    public boolean isVariableTerm() {
        return false;
    }

    @Override
    public boolean isCompoundTerm() {
        return true;
    }

    @Override
    public String toMathString() {

        StringBuilder sb = new StringBuilder();
        sb.append(rootSymbol.getSymbol());
        sb.append("(");

        boolean first = true;
        for (Term term : subterms) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(term.toMathString());
            first = false;
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof FunctionTerm)) {
            return false;
        }

        if (this.rootSymbol != ((FunctionTerm) o).rootSymbol) {
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
