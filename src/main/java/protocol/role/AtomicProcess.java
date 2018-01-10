package protocol.role;

import rewriting.terms.VariableTerm;

import java.util.Collection;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface AtomicProcess {

    boolean isConditionalOutput();

    boolean isOutput();

    boolean isInput();

    int getPhase();

    Collection<VariableTerm> getVariables();

    AtomicProcess appendBranchIndexToVars(int index);

}
