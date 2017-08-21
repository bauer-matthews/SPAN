package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apfloat.Apfloat;
import process.*;
import process.Resources;
import protocol.Interleaving;
import protocol.Protocol;
import rewriting.terms.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

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

    private static final List<Interleaving> interleavings;
    private static int interleavingsExplored;

    static {

        interleavings = new ArrayList<>();
        interleavingsExplored = 0;

        recipes = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                        new CacheLoader<Integer, Collection<Term>>() {
                            // TODO: tighten exception
                            public Collection<Term> load(Integer numFrameVariables) throws Exception {
                                return ActionFactory.getAllRecipes(numFrameVariables);
                            }
                        });
    }

    public static void addInterleaving(Interleaving interleaving) {

        interleavingsExplored++;
        interleavings.add(interleaving);
    }

    public static int getInterleavingsExplored() {
        return interleavingsExplored;
    }

    public static int getInterleavingsReduction() {
        return interleavings.size();
    }

    public static Optional<Apfloat> hasPartialOrderReduction(Interleaving interleaving) {

        for (Interleaving explored : interleavings) {
            if (explored.getInterleaving().size() >= interleaving.getInterleaving().size()) {

                for (int i = 1; i < interleaving.getInterleaving().size(); i++) {

                    // Back-to-back outputs can be swapped
                    if (interleaving.getInterleaving().get(i).getRecipe().equals(Resources.TAU_ACTION) &&
                            interleaving.getInterleaving().get(i - 1).getRecipe().equals(Resources.TAU_ACTION)) {

                        if (explored.getInterleaving().get(i).equals(interleaving.getInterleaving().get(i - 1)) &&
                                explored.getInterleaving().get(i - 1).equals(interleaving.getInterleaving().get(i))) {

                            interleavingsExplored++;
                            return Optional.of(explored.getAttackProb());
                        }
                    }

                    // Back-to-back inputs can be swapped
                    if ((!interleaving.getInterleaving().get(i).getRecipe().equals(Resources.TAU_ACTION)) &&
                            (!interleaving.getInterleaving().get(i - 1).getRecipe().equals(Resources.TAU_ACTION))) {

                        // NOTE: This only allows swapping of identical recipes.
                        if (explored.getInterleaving().get(i).equals(interleaving.getInterleaving().get(i - 1)) &&
                                explored.getInterleaving().get(i - 1).equals(interleaving.getInterleaving().get(i))) {

                            interleavingsExplored++;
                            return Optional.of(explored.getAttackProb());
                        }
                    }
                }
            }
        }

        return Optional.empty();
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
