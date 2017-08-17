package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import rewriting.RewriteEngine;
import rewriting.terms.Term;

import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/16/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class RewritingCache {

    private static final LoadingCache<Term, Term> reducedTerms;

    static {
        reducedTerms = CacheBuilder.newBuilder()
                .maximumSize(20000)
                .build(
                        new CacheLoader<Term, Term>() {
                            // TODO: tighten exception
                            public Term load(Term term) throws Exception {
                                return RewriteEngine.reduce(term, GlobalDataCache.getProtocol().getRewrites());
                            }
                        });
    }

    public static Term reduce(Term term) throws ExecutionException {
        return reducedTerms.get(term);
    }
}
