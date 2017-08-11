package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.util.Pair;
import kiss.EquivalenceResult;
import process.ActionFactory;
import process.EquivalenceCheckResult;
import process.EquivalenceChecker;
import process.State;
import protocol.Protocol;
import rewriting.terms.Term;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/10/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class EquivalenceCache {

    private static final LoadingCache<Pair<State, State>, EquivalenceCheckResult> equivalenceChecks;

    static {
        equivalenceChecks = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                        new CacheLoader<Pair<State, State>, EquivalenceCheckResult>() {
                            // TODO: tighten exception
                            public EquivalenceCheckResult load(Pair<State, State> statePair) throws Exception {
                                return EquivalenceChecker.check(statePair.getKey(), statePair.getValue());
                            }
                        });
    }


    public static EquivalenceCheckResult checkEquivalence(State state1, State state2) throws ExecutionException {
        return equivalenceChecks.get(new Pair<>(state1, state2));
    }
}
