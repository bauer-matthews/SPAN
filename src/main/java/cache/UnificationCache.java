package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.util.Pair;
import rewriting.Equality;
import rewriting.terms.Term;
import rewriting.unification.Unify;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/16/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class UnificationCache {

    private static final int DEFAULT_CACHE_SIZE = 20000;

    private static long cacheLoads;
    private static long cacheCalls;
    private static long cacheSize;

    private static final LoadingCache<Pair<Term, Term>, Optional<Collection<Equality>>> unifiedTerms;

    static {

        cacheLoads = 0;
        cacheCalls = 0;

        if (RunConfiguration.getUnificationCacheSize().isPresent()) {
            cacheSize = RunConfiguration.getUnificationCacheSize().get().intValue();
        } else {
            cacheSize = DEFAULT_CACHE_SIZE;
        }

        unifiedTerms = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<Pair<Term, Term>, Optional<Collection<Equality>>>() {
                            // TODO: tighten exception
                            public Optional<Collection<Equality>> load(Pair<Term, Term> pair) throws Exception {

                                cacheLoads++;
                                return Unify.unify(pair.getKey(), pair.getValue());
                            }
                        });
    }

    public static Optional<Collection<Equality>> unify(Term term1, Term term2) throws ExecutionException {

        cacheCalls++;
        return unifiedTerms.get(new Pair<>(term1, term2));
    }

    public static long getCacheLoads() {
        return cacheLoads;
    }

    public static long getCacheCalls() {
        return cacheCalls;
    }

    public static long getCacheSize() {
        return cacheSize;
    }
}
