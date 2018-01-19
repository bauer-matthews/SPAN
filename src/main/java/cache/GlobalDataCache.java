package cache;

import attacker.AttackTree;
import attacker.Node;
import org.apfloat.Aprational;
import parser.protocol.ProtocolType;
import protocol.IndistinguishabilityProtocol;
import protocol.Interleaving;
import protocol.Metadata;
import protocol.ReachabilityProtocol;
import rewriting.Rewrite;
import rewriting.Signature;

import java.util.ArrayList;
import java.util.Collection;
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
    private static int numConstrainsts;

    private static long protcol1StateCounter;
    private static long protcol2StateCounter;
    private static long beliefStateCounter;
    private static long pfaConstructionTime;
    private static long constraintUpdatetime;

    private static AttackTree attackTree;
    private static ReachabilityProtocol reachabilityProtocol;
    private static IndistinguishabilityProtocol indistinguishabilityProtocol;
    private static ProtocolType protocolType;

    static {
        interleavings = new ArrayList<>();
        interleavingsExplored = 0;
        maxActionSetSize = 0;
        branchIndexCounter = 0;
        protcol1StateCounter = 0;
        protcol2StateCounter = 0;
        beliefStateCounter = 0;
        constraintUpdatetime = 0;
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

        if (RunConfiguration.getDebug()) {
            System.out.print("\rPaths explored: " + interleavingsExplored);
        }

        int size = interleaving.getActionList().size();

//        for (Interleaving explored : interleavings) {
//            if (explored.getActionList().size() == size && size >= 2) {
//
//                // Back-to-back outputs produce identical runs
//                if (interleaving.getActionList().get(size - 1).getRecipe().equals(Resources.TAU_ACTION) &&
//                        interleaving.getActionList().get(size - 2).getRecipe().equals(Resources.TAU_ACTION)) {
//
//                    if (explored.getActionList().get(size - 1).equals(interleaving.getActionList().get(size - 2)) &&
//                            explored.getActionList().get(size - 2).equals(interleaving.getActionList().get(size - 1))) {
//
//                        // If there are empty outputs, the reduction is only sound if the
//                        // outputs are in different roles
//                        if (!RunConfiguration.containsEmptyOutputs()) {
//
//                            interleavingsExplored++;
//                            return Optional.of(explored.getAttackProb());
//
//                        } else {
//                            if (interleaving.getActionList().get(size - 1).getRoleIndex() !=
//                                    interleaving.getActionList().get(size - 2).getRoleIndex()) {
//
//                                interleavingsExplored++;
//                                return Optional.of(explored.getAttackProb());
//                            }
//                        }
//                    }
//                }
//
//                // Back-to-back inputs from different roles can be swapped
//                if ((!interleaving.getActionList().get(size - 1).getRecipe().equals(Resources.TAU_ACTION)) &&
//                        (!interleaving.getActionList().get(size - 2).getRecipe().equals(Resources.TAU_ACTION))) {
//
//                    // NOTE: This only allows swapping of identical recipes.
//                    if (explored.getActionList().get(size - 1).equals(interleaving.getActionList().get(size - 2)) &&
//                            explored.getActionList().get(size - 2).equals(interleaving.getActionList().get(size - 1))) {
//
//                        if (interleaving.getActionList().get(size - 1).getRoleIndex() !=
//                                interleaving.getActionList().get(size - 2).getRoleIndex()) {
//
//                            interleavingsExplored++;
//                            return Optional.of(explored.getAttackProb());
//                        }
//                    }
//                }
//            }
//        }

        return Optional.empty();
    }

    public static void setReachabilityProtocol(ReachabilityProtocol protocolInstance) {
        reachabilityProtocol = protocolInstance;
    }

    public static ReachabilityProtocol getReachabilityProtocol() {
        return reachabilityProtocol;
    }

    public static IndistinguishabilityProtocol getIndistinguishabilityProtocol() {
        return indistinguishabilityProtocol;
    }

    public static void setIndistinguishabilityProtocol(IndistinguishabilityProtocol indistinguishabilityProtocol) {
        GlobalDataCache.indistinguishabilityProtocol = indistinguishabilityProtocol;
    }

    public static Signature getSignature() {

        if (protocolType.equals(ProtocolType.REACHABILITY)) {
            return reachabilityProtocol.getSignature();
        } else {
            return indistinguishabilityProtocol.getSignature();
        }
    }

    public static Metadata getMetadata() {

        if (protocolType.equals(ProtocolType.REACHABILITY)) {
            return reachabilityProtocol.getMetadata();
        } else {
            return indistinguishabilityProtocol.getMetadata();
        }
    }

    public static Collection<Rewrite> getRewrites() {

        if (protocolType.equals(ProtocolType.REACHABILITY)) {
            return reachabilityProtocol.getRewrites();
        } else {
            return indistinguishabilityProtocol.getRewrites();
        }
    }

    public static int getFreshBranchIndex() {
        branchIndexCounter++;
        return branchIndexCounter;
    }

    public static long getBeliefStateCounter() {
        return beliefStateCounter;
    }

    public static long getProtcol1StateCounter() {
        return protcol1StateCounter;
    }

    public static long getProtcol2StateCounter() {
        return protcol2StateCounter;
    }

    public static void setBeliefStateCounter(long beliefStatesVisited) {
        GlobalDataCache.beliefStateCounter = beliefStatesVisited;
    }

    public static void setProtcol1StateCounter(long numStates) {
        GlobalDataCache.protcol1StateCounter = numStates;
    }

    public static void setProtcol2StateCounter(long numStates) {
        GlobalDataCache.protcol2StateCounter = numStates;
    }

    public static ProtocolType getProtocolType() {
        return protocolType;
    }

    public static void setProtocolType(ProtocolType protocolType) {
        GlobalDataCache.protocolType = protocolType;
    }

    public static void setNumConstrainsts(int numConstrainsts) {
        GlobalDataCache.numConstrainsts = numConstrainsts;
    }

    public static int getNumConstrainsts() {
        return numConstrainsts;
    }

    public static long getPfaConstructionTime() {
        return pfaConstructionTime;
    }

    public static long getConstraintUpdatetime() {
        return constraintUpdatetime;
    }

    public static void setPfaConstructionTime(long pfaConstructionTime) {
        GlobalDataCache.pfaConstructionTime = pfaConstructionTime;
    }

    public static void incrimentConstraintUpdatetime(long additionalConstraintUpdatetime) {
        GlobalDataCache.constraintUpdatetime = GlobalDataCache.constraintUpdatetime + additionalConstraintUpdatetime;
    }
}
