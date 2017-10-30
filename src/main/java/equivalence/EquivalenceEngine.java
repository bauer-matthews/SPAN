package equivalence;

import process.State;
import rewriting.Rewrite;
import rewriting.Signature;
import rewriting.terms.Term;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 10/30/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface EquivalenceEngine {

    String invoke(String input) throws InterruptedException, IOException;

    String encode(Signature signature, Collection<Rewrite> rewrites, State state1, State state2,
                  List<Term> frame1Secrets, List<Term> frame2Secrets);

    List<DeductionResult> decodeDeductionResults(String output);

    List<EquivalenceResult> decodeEquivalenceResults(String output);
}
