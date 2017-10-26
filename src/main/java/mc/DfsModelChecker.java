package mc;

import attacker.AttackTree;
import attacker.Node;
import attacker.TransitionNode;
import attacker.ViewNode;
import cache.GlobalDataCache;
import cache.RunConfiguration;
import org.apfloat.Aprational;
import process.*;
import protocol.Interleaving;
import protocol.role.Role;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/9/2017.
 */
public class DfsModelChecker extends AbstractModelChecker {

    private final boolean max;

    public DfsModelChecker(boolean max) {
        this.max = max;
    }

    @Override
    public boolean findMaximum() {
        return max;
    }

    @Override
    public Aprational check(State initialState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        Belief initialBelief = new Belief(initialState, Aprational.ONE);
        BeliefState initialBeliefState = new BeliefState(
                Collections.singletonList(initialBelief), Collections.emptyList());

        ViewNode root = new ViewNode(initialBeliefState.getObservation());
        root.setAttackProb(Aprational.ZERO);

        AttackTree attackTree = new AttackTree(root, Collections.singletonList(root));
        GlobalDataCache.initializeAttackTree(attackTree);

        return getMaximumAttackProb(initialBeliefState, root);
    }

    private Aprational getMaximumAttackProb(BeliefState beliefState, Node parentAttackNode)
            throws InvalidActionException, InterruptedException, IOException, ExecutionException {

        if(RunConfiguration.getDebug()) {
            GlobalDataCache.incrimentBeliefStateCounter();
            GlobalDataCache.incrementStateCounter(beliefState.getBeliefs().size());
        }

        Optional<Aprational> attackProb = GlobalDataCache.hasPartialOrderReduction(
                new Interleaving(beliefState.getActionHistory(), beliefState.getStateAttackProb()));

        if (attackProb.isPresent()) {
            return attackProb.get();
        }

        Aprational maxProb = beliefState.getStateAttackProb();

        if (maxProb.equals(Aprational.ONE)) {
            GlobalDataCache.addInterleaving(new Interleaving(beliefState.getActionHistory(),
                    beliefState.getStateAttackProb()));
            return Aprational.ONE;
        }

        List<Action> enabledActions = BeliefTransitionSystem.getEnabledActions(beliefState);

        if (enabledActions.size() == 0) {
            return maxProb;
        }

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
        Map<Node, Aprational> attackNodeProbMap = new HashMap<>();
        Action attackAction = null;

        for (Action action : enabledActions) {

            List<Node> actionNodes = new ArrayList<>();
            Map<Node, Aprational> actionNodeProbMap = new HashMap<>();

            if (RunConfiguration.getTrace()) {
                System.out.println("CHOSEN ACTION: " + action.getRecipe().toMathString());
                System.out.println();
            }

            Aprational maxActionProb = Aprational.ZERO;
            List<BeliefTransition> transitions = BeliefTransitionSystem.applyAction(beliefState, action);

            for (BeliefTransition transition : transitions) {

                ViewNode node = new ViewNode(transition.getBeliefState().getObservation());
                node.setAttackProb(transition.getBeliefState().getStateAttackProb());
                actionNodes.add(node);
                actionNodeProbMap.put(node, transition.getTransitionProbability());

                maxActionProb = maxActionProb.add((transition.getTransitionProbability()
                        .multiply(getMaximumAttackProb(transition.getBeliefState(), node))));
            }

            if (maxActionProb.equals(Aprational.ONE)) {

                storeAttack(parentAttackNode, actionNodes, action, actionNodeProbMap);
                return Aprational.ONE;

            } else {
                if (maxActionProb.compareTo(maxProb) > 0) {
                    attackAction = action;
                    maxProb = maxActionProb;
                    attackNodes = actionNodes;
                    attackNodeProbMap = actionNodeProbMap;
                }
            }
        }

        storeAttack(parentAttackNode, attackNodes, attackAction, attackNodeProbMap);

        GlobalDataCache.addInterleaving(new Interleaving(beliefState.getActionHistory(),
                beliefState.getStateAttackProb()));

        if (!findMaximum()) {

            if (GlobalDataCache.getAttackTree().attackFound()) {

                this.setStopTime(System.currentTimeMillis());
                printResults();
            }
        }

        return maxProb;
    }

    private static void storeAttack(Node parentNode, List<Node> children, Action action,
                                    Map<Node, Aprational> attackNodeProbMap) {

        for (Node node : children) {

            TransitionNode transitionNode = new TransitionNode(action, attackNodeProbMap.get(node));
            parentNode.addChild(transitionNode);
            transitionNode.addChild(node);

            GlobalDataCache.addAttackNode(transitionNode);
            GlobalDataCache.addAttackNode(node);
        }
    }
}
