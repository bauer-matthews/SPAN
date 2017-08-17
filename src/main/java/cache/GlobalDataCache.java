package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apfloat.Apfloat;
import process.ActionFactory;
import protocol.Protocol;
import rewriting.terms.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/26/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class GlobalDataCache {

    private static final LoadingCache<Integer, Collection<Term>> recipes;
    private static Protocol protocol;

    static {
        recipes = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                        new CacheLoader<Integer, Collection<Term>>() {
                            // TODO: tighten exception
                            public Collection<Term> load(Integer numFrameVariables) throws Exception {
                                return ActionFactory.getRecipes(numFrameVariables);
                            }
                        });
    }

    public static void setProtocol(Protocol protocolInstance) {
        protocol = protocolInstance;
    }

    public static Protocol getProtocol() {
        return protocol;
    }

    public static Collection<Term> getRecipes(int numFrameVariables) throws ExecutionException {
        return recipes.get(numFrameVariables);
    }
}
