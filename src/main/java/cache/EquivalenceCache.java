package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import process.EquivalenceCheckResult;
import process.EquivalenceChecker;
import process.State;
import util.Pair;

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

    private static final int DEFAULT_CACHE_SIZE = 20000;

    private static long cacheLoads;
    private static long cacheCalls;
    private static int cacheSize;

    private static final LoadingCache<Pair<State, State>, EquivalenceCheckResult> equivalenceChecks;

    static {

        cacheLoads = 0;
        cacheCalls = 0;

        if (RunConfiguration.getDefaultEquivalenceCacheSize().isPresent()) {
            cacheSize = RunConfiguration.getDefaultEquivalenceCacheSize().get();
        } else {
            cacheSize = DEFAULT_CACHE_SIZE;
        }

        equivalenceChecks = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<Pair<State, State>, EquivalenceCheckResult>() {
                            // TODO: tighten exception
                            public EquivalenceCheckResult load(Pair<State, State> statePair) throws Exception {

                                cacheLoads++;
                                return EquivalenceChecker.check(statePair.getKey(), statePair.getValue());
                            }
                        });
    }


    public static EquivalenceCheckResult checkEquivalence(State state1, State state2) throws ExecutionException {

        cacheCalls++;
        return equivalenceChecks.get(new Pair<>(state1, state2));
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
