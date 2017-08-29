package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rewriting.Equality;
import rewriting.terms.Term;
import util.Pair;
import util.rewrite.RewriteUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/10/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SubstitutionCache {

    private static final int DEFAULT_CACHE_SIZE = 30000;

    private static long cacheLoads;
    private static long cacheCalls;
    private static int cacheSize;

    private static final LoadingCache<Pair<Term, Collection<Equality>>, Term> solvedTerms;

    static {

        cacheLoads = 0;
        cacheCalls = 0;

        if (RunConfiguration.getSubstitutionCacheSize().isPresent()) {
            cacheSize = RunConfiguration.getSubstitutionCacheSize().get();
        } else {
            cacheSize = DEFAULT_CACHE_SIZE;
        }

        solvedTerms = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<Pair<Term, Collection<Equality>>, Term>() {
                            // TODO: tighten exception
                            public Term load(Pair<Term, Collection<Equality>> sub) throws Exception {

                                cacheLoads++;
                                return RewriteUtils.applySubstitution(sub.getKey(), sub.getValue());
                            }
                        });
    }

    public static Term applySubstitution(Term term, Collection<Equality> equalities) throws ExecutionException {

        cacheCalls++;
        return solvedTerms.get(new Pair<>(term, equalities));
    }

    public static Equality applySubstitution(Equality equality, Collection<Equality> equalities)
            throws ExecutionException {

        Term subLhs = applySubstitution(equality.getLhs(), equalities);
        Term subRhs = applySubstitution(equality.getRhs(), equalities);

        return new Equality(subLhs, subRhs);
    }

    public static List<Term> applySubstitution(List<Term> terms, Collection<Equality> equalities)
            throws ExecutionException {

        List<Term> newTerms = new ArrayList<>();

        for (Term term : terms) {
            newTerms.add(applySubstitution(term, equalities));
        }

        return newTerms;
    }

    public static long getCacheCalls() {
        return cacheCalls;
    }

    public static long getCacheLoads() {
        return cacheLoads;
    }

    public static int getCacheSize() {
        return cacheSize;
    }
}
