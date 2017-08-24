package mc;

import attacker.AttackTree;
import attacker.Node;
import attacker.TransitionNode;
import attacker.ViewNode;
import cache.GlobalDataCache;
import cache.RunConfiguration;
import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import process.*;
import protocol.Interleaving;
import protocol.role.Role;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/9/2017.
 */
public class DfsModelChecker {

    public static Apfloat check(State initialState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        Belief initialBelief = new Belief(initialState, Apfloat.ONE);
        BeliefState initialBeliefState = new BeliefState(
                Collections.singletonList(initialBelief), Collections.emptyList());

        ViewNode root = new ViewNode(initialBeliefState.getObservation());
        root.setAttackProb(Apcomplex.ZERO);

        AttackTree attackTree = new AttackTree(root, Collections.singletonList(root));
        GlobalDataCache.initializeAttackTree(attackTree);

        return getMaximumAttackProb(initialBeliefState, root);
    }

    private static Apfloat getMaximumAttackProb(BeliefState beliefState, Node parentAttackNode)
            throws InvalidActionException, InterruptedException, IOException, ExecutionException {

        Optional<Apfloat> attackProb = GlobalDataCache.hasPartialOrderReduction(
                new Interleaving(beliefState.getActionHistory(), beliefState.getStateAttackProb()));

        if (attackProb.isPresent()) {
            return attackProb.get();
        }

        Apfloat maxProb = beliefState.getStateAttackProb();

        if (maxProb.equals(Apfloat.ONE)) {
            GlobalDataCache.addInterleaving(new Interleaving(beliefState.getActionHistory(),
                    beliefState.getStateAttackProb()));
            return Apfloat.ONE;
        }

        List<Action> enabledActions = BeliefTransitionSystem.getEnabledActions(beliefState);

        if (RunConfiguration.getTrace()) {

            System.out.println("BELIEF STATE: ");
            for (Belief belief : beliefState.getBeliefs()) {

                System.out.println("\tBELIEF: " + belief.getProb().toString(true));
                for (Role role : belief.getState().getRoles()) {
                    System.out.println("\t\tROLE: " + role.toString());
                }
                System.out.println("\t\tFRAME: " + belief.getState().getFrame().toString());
            }

            System.out.println("ENABLED ACTIONS: " + enabledActions);
        }

        List<Node> attackNodes = new ArrayList<>();
        Map<Node, Apfloat> attackNodeProbMap = new HashMap<>();
        Action attackAction = null;

        for (Action action : enabledActions) {

            attackNodes.clear();
            attackNodeProbMap.clear();

            if (RunConfiguration.getTrace()) {
                System.out.println("CHOSEN ACTION: " + action.getRecipe().toMathString());
                System.out.println();
            }

            Apfloat maxActionProb = Apfloat.ZERO;
            List<BeliefTransition> transitions = BeliefTransitionSystem.applyAction(beliefState, action);

            for (BeliefTransition transition : transitions) {

                ViewNode node = new ViewNode(transition.getBeliefState().getObservation());
                node.setAttackProb(transition.getBeliefState().getStateAttackProb());
                attackNodes.add(node);
                attackNodeProbMap.put(node, transition.getTransitionProbability());

                maxActionProb = maxActionProb.add((transition.getTransitionProbability()
                        .multiply(getMaximumAttackProb(transition.getBeliefState(), node))));
            }

            if (maxActionProb.equals(Apfloat.ONE)) {

                storeAttack(parentAttackNode, attackNodes, action, attackNodeProbMap);
                return Apfloat.ONE;

            } else {
                if (maxActionProb.compareTo(maxProb) > 0) {
                    attackAction = action;
                    maxProb = maxActionProb;
                }
            }
        }

        storeAttack(parentAttackNode, attackNodes, attackAction, attackNodeProbMap);

        GlobalDataCache.addInterleaving(new Interleaving(beliefState.getActionHistory(),
                beliefState.getStateAttackProb()));

        return maxProb;
    }

    private static void storeAttack(Node parentNode, List<Node> children, Action action,
                                    Map<Node, Apfloat> attackNodeProbMap) {

        for (Node node : children) {

            TransitionNode transitionNode = new TransitionNode(action, attackNodeProbMap.get(node));
            parentNode.addChild(transitionNode);
            transitionNode.addChild(node);

            GlobalDataCache.addAttackNode(transitionNode);
            GlobalDataCache.addAttackNode(node);
        }
    }
}
