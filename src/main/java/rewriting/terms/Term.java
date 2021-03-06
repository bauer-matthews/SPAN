package rewriting.terms;

import java.util.Collection;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface Term {

    Collection<VariableTerm> getVariables();

    Collection<NameTerm> getPrivateNames();

    Term substitute(VariableTerm var, Term term);

    boolean isNameTerm();

    boolean isGroundTerm();

    boolean isVariableTerm();

    boolean isCompoundTerm();

    boolean hasSort(Sort sort);

    String toMathString();

    Sort getSort();

    int getSize();

    Term appendBranchIndexToVars(int index);
}
