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

    private static final int DEFAULT_CACHE_SIZE = 20000;

    private static long cacheLoads;
    private static long cacheCalls;
    private static int cacheSize;

    private static final LoadingCache<Term, Term> reducedTerms;

    static {

        cacheLoads = 0;
        cacheCalls = 0;

        if (RunConfiguration.getRewritingCacheSize().isPresent()) {
            cacheSize = RunConfiguration.getRewritingCacheSize().get();
        } else {
            cacheSize = DEFAULT_CACHE_SIZE;
        }

        reducedTerms = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<Term, Term>() {
                            // TODO: tighten exception
                            public Term load(Term term) throws Exception {
                                cacheLoads++;
                                return RewriteEngine.reduce(term,
                                        GlobalDataCache.getProtocol().getRewrites(), true);
                            }
                        });
    }

    public static Term reduce(Term term) throws ExecutionException {
        cacheCalls++;
        return reducedTerms.get(term);
    }

    public static long getCacheLoads() {
        return cacheLoads;
    }

    public static long getCacheCalls() {
        return cacheCalls;
    }

    public static int getCacheSize() {
        return cacheSize;
    }
}
