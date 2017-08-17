package mc;

import cache.RunConfiguration;
import org.apfloat.Apfloat;
import process.*;
import protocol.role.Role;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/9/2017.
 */
public class DfsModelChecker {

    public static Apfloat check(State initialState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        Belief initialBelief = new Belief(initialState, Apfloat.ONE);
        BeliefState initialBeliefState = new BeliefState(Collections.singletonList(initialBelief));

        return getMaximumAttackProb(initialBeliefState);
    }

    private static Apfloat getMaximumAttackProb(BeliefState beliefState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        Apfloat maxProb = beliefState.getStateAttackProb();

        if (maxProb.equals(Apfloat.ONE)) {
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

        for (Action action : enabledActions) {

            if (RunConfiguration.getTrace()) {
                System.out.println("CHOSEN ACTION: " + action.getRecipe().toMathString());
                System.out.println();
            }

            Apfloat maxActionProb = Apfloat.ZERO;

            List<BeliefTransition> transitions = BeliefTransitionSystem.applyAction(beliefState, action);
            for (BeliefTransition transition : transitions) {
                maxActionProb = maxActionProb.add((transition.getTransitionProbability().multiply(getMaximumAttackProb(transition.getBeliefState()))));
            }

            if (maxActionProb.equals(Apfloat.ONE)) {
                return Apfloat.ONE;
            } else {
                if (maxActionProb.compareTo(maxProb) > 0) {
                    maxProb = maxActionProb;
                }
            }
        }

        return maxProb;
    }
}
