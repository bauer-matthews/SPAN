package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import process.ActionFactory;
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
public class ActionFactoryCache {

    private static final LoadingCache<Integer, Collection<Term>> recipes;

    static {

        recipes = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .build(
                        new CacheLoader<Integer, Collection<Term>>() {
                            // TODO: tighten exception
                            public Collection<Term> load(Integer numFrameVariables) throws Exception {
                                return ActionFactory.getAllRecipes(numFrameVariables);
                            }
                        });
    }

    public static Collection<Term> getRecipes(int numFrameVariables) throws ExecutionException {
        return recipes.get(numFrameVariables);
    }
}
