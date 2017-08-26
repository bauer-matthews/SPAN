package cache;

import attacker.AttackTree;
import attacker.Node;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apfloat.Aprational;
import process.ActionFactory;
import process.Resources;
import protocol.Interleaving;
import protocol.Protocol;
import rewriting.terms.Term;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    private static int maxActionSetSize;

    private static AttackTree attackTree;

    static {

        interleavings = new ArrayList<>();
        interleavingsExplored = 0;
        maxActionSetSize = 0;

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

    public static void reportActionSetSize(int size) {
        if (size > GlobalDataCache.maxActionSetSize) {
            GlobalDataCache.maxActionSetSize = size;
        }
    }

    public static int getMaxActionSetSize() {
        return maxActionSetSize;
    }

    public static void initializeAttackTree(AttackTree attackTree) {
        GlobalDataCache.attackTree = attackTree;
    }

    public static void addAttackNode(Node node) {
        attackTree.addNode(node);
    }

    public static AttackTree getAttackTree() {
        return attackTree;
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

    public static Optional<Aprational> hasPartialOrderReduction(Interleaving interleaving) {

        int size = interleaving.getActionList().size();

        for (Interleaving explored : interleavings) {
            if (explored.getActionList().size() == size && size >= 2) {


                // Back-to-back outputs can be swapped
                if (interleaving.getActionList().get(size - 1).getRecipe().equals(Resources.TAU_ACTION) &&
                        interleaving.getActionList().get(size - 2).getRecipe().equals(Resources.TAU_ACTION)) {

                    if (explored.getActionList().get(size - 1).equals(interleaving.getActionList().get(size - 2)) &&
                            explored.getActionList().get(size - 2).equals(interleaving.getActionList().get(size - 1))) {

                        interleavingsExplored++;
                        return Optional.of(explored.getAttackProb());
                    }
                }

                // Back-to-back inputs can be swapped
                if ((!interleaving.getActionList().get(size - 1).getRecipe().equals(Resources.TAU_ACTION)) &&
                        (!interleaving.getActionList().get(size - 2).getRecipe().equals(Resources.TAU_ACTION))) {

                    // NOTE: This only allows swapping of identical recipes.
                    if (explored.getActionList().get(size - 1).equals(interleaving.getActionList().get(size - 2)) &&
                            explored.getActionList().get(size - 2).equals(interleaving.getActionList().get(size - 1))) {

                        interleavingsExplored++;
                        return Optional.of(explored.getAttackProb());
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
