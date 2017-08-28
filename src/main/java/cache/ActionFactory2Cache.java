package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import process.ActionFactory2;
import process.RecipeParameter;
import rewriting.terms.Term;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/28/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ActionFactory2Cache {

    private static final int DEFAULT_CACHE_SIZE = 10000;

    private static long cacheLoads;
    private static long cacheCalls;

    private static final LoadingCache<RecipeParameter, Collection<Term>> recipes;

    static {

        cacheCalls = 0;
        cacheLoads = 0;

        long cacheSize;
        if (RunConfiguration.getActionFactoryCacheSize().isPresent()) {
            cacheSize = RunConfiguration.getActionFactoryCacheSize().get();
        } else {
            cacheSize = DEFAULT_CACHE_SIZE;
        }


        recipes = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<RecipeParameter, Collection<Term>>() {
                            // TODO: tighten exception
                            public Collection<Term> load(RecipeParameter parameter) throws Exception {

                                cacheLoads++;
                                return ActionFactory2.getAllRecipes(parameter.getFrameVariables(),
                                        parameter.getFrameValues(), parameter.getSort(),
                                        parameter.getDepth(), parameter.getGuard());
                            }
                        });
    }

    public static Collection<Term> getRecipes(RecipeParameter parameter) throws ExecutionException {
        cacheCalls++;
        return recipes.get(parameter);
    }

    public static long getCacheCalls() {
        return cacheCalls;
    }

    public static long getCacheLoads() {
        return cacheLoads;
    }
}
