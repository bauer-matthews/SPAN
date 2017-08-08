package resources.rewritting;

import resources.signature.SymmetricKey;
import rewriting.Rewrite;

import java.util.ArrayList;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/8/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class RewriteBuilder {

    private final List<Rewrite> rewrites = new ArrayList<>();

    public RewriteBuilder() {

    }

    public RewriteBuilder addPair() {
        rewrites.addAll(PairRewrites.REWRITE_RULES);
        return this;
    }

    public RewriteBuilder addSymmetricKey() {
        rewrites.addAll(SymmetricKeyRewrites.REWRITE_RULES);
        return this;
    }

    public List<Rewrite> build() {
        return rewrites;
    }
}
