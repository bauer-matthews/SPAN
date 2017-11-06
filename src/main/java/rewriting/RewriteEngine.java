package rewriting;

import rewriting.terms.Term;
import rewriting.terms.TermParseException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 10/31/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface RewriteEngine {

    Term reduce(Term term, boolean useCache) throws ExecutionException, IOException, TermParseException;

    void shutdown() throws IOException;
}
