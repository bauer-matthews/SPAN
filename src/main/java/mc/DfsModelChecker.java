package mc;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import process.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbauer on 8/9/2017.
 */
public class DfsModelChecker {

    public static Apfloat check(State initialState) throws InvalidActionException,
            InterruptedException, IOException {

        Belief initialBelief = new Belief(initialState, Apfloat.ONE);
        BeliefState initialBeliefState = new BeliefState(Collections.singletonList(initialBelief));

        return getMaximumAttackProb(initialBeliefState);
    }

    private static Apfloat getMaximumAttackProb(BeliefState beliefState) throws InvalidActionException,
            InterruptedException, IOException {

        if (beliefState.getStateAttackProb().equals(Apfloat.ONE)) {
            return Apfloat.ONE;
        }

        Apfloat maxProb = Apfloat.ZERO;

        List<Action> enabledActions = BeliefTransitionSystem.getEnabledActions(beliefState);
        for (Action action : enabledActions) {

            Apfloat maxActionProb = Apfloat.ZERO;

            List<BeliefTransition> transitions = BeliefTransitionSystem.applyAction(beliefState, action);
            for (BeliefTransition transition : transitions) {
                maxActionProb.add((transition.getTransitionProbability().multiply(getMaximumAttackProb(transition.getBeliefState()))));
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
