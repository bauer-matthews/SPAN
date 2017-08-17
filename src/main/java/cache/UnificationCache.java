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

    private static final LoadingCache<Pair<Term, Term>, Optional<Collection<Equality>>> unifiedTerms;

    static {
        unifiedTerms = CacheBuilder.newBuilder()
                .maximumSize(100000)
                .build(
                        new CacheLoader<Pair<Term, Term>, Optional<Collection<Equality>>>() {
                            // TODO: tighten exception
                            public Optional<Collection<Equality>> load(Pair<Term, Term> pair) throws Exception {
                                return Unify.unify(pair.getKey(), pair.getValue());
                            }
                        });
    }

    public static Optional<Collection<Equality>> unify(Term term1, Term term2) throws ExecutionException {

        return unifiedTerms.get(new Pair<>(term1, term2));
    }
}
