package cache;

import attacker.AttackTree;
import attacker.Node;
import org.apfloat.Aprational;
import process.Resources;
import protocol.Interleaving;
import protocol.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/26/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class GlobalDataCache {

    private static final List<Interleaving> interleavings;

    private static int interleavingsExplored;
    private static int maxActionSetSize;
    private static int branchIndexCounter;

    private static AttackTree attackTree;
    private static Protocol protocol;


    static {
        interleavings = new ArrayList<>();
        interleavingsExplored = 0;
        maxActionSetSize = 0;
        branchIndexCounter = 0;
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

                // Back-to-back outputs produce identical runs
                if (interleaving.getActionList().get(size - 1).getRecipe().equals(Resources.TAU_ACTION) &&
                        interleaving.getActionList().get(size - 2).getRecipe().equals(Resources.TAU_ACTION)) {

                    if (explored.getActionList().get(size - 1).equals(interleaving.getActionList().get(size - 2)) &&
                            explored.getActionList().get(size - 2).equals(interleaving.getActionList().get(size - 1))) {

                        // If there are empty outputs, the reduction is only sound if the
                        // outputs are in different roles
                        if (!RunConfiguration.containsEmptyOutputs()) {

                            interleavingsExplored++;
                            return Optional.of(explored.getAttackProb());

                        } else {
                            if (interleaving.getActionList().get(size - 1).getRoleIndex() !=
                                    interleaving.getActionList().get(size - 2).getRoleIndex()) {

                                interleavingsExplored++;
                                return Optional.of(explored.getAttackProb());
                            }
                        }
                    }
                }

                // Back-to-back inputs from different roles can be swapped
                if ((!interleaving.getActionList().get(size - 1).getRecipe().equals(Resources.TAU_ACTION)) &&
                        (!interleaving.getActionList().get(size - 2).getRecipe().equals(Resources.TAU_ACTION))) {

                    // NOTE: This only allows swapping of identical recipes.
                    if (explored.getActionList().get(size - 1).equals(interleaving.getActionList().get(size - 2)) &&
                            explored.getActionList().get(size - 2).equals(interleaving.getActionList().get(size - 1))) {

                        if (interleaving.getActionList().get(size - 1).getRoleIndex() !=
                                interleaving.getActionList().get(size - 2).getRoleIndex()) {

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

    public static int getFreshBranchIndex() {
        branchIndexCounter++;
        return branchIndexCounter;
    }
}
