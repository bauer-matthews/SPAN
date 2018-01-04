package mc.indistinguishability;

import cache.EquivalenceCache;
import cache.GlobalDataCache;
import org.apfloat.Aprational;
import process.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 12/27/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class OnTheFlyModelChecker extends AbstractModelChecker {

    private final State initialState1;
    private final State initialState2;

    public OnTheFlyModelChecker(State initialState1, State initialState2) {

        Objects.requireNonNull(initialState1);
        Objects.requireNonNull(initialState2);

        this.initialState1 = initialState1;
        this.initialState2 = initialState2;
    }

    @Override
    public boolean check(State state1, State state2) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        Belief belief1 = new Belief(state1, Aprational.ONE);
        BeliefState beliefState1 = new BeliefState(
                Collections.singletonList(belief1), Collections.emptyList());

        Belief belief2 = new Belief(state2, Aprational.ONE);
        BeliefState beliefState2 = new BeliefState(
                Collections.singletonList(belief2), Collections.emptyList());

        return equivalent(beliefState1, beliefState2);
    }

    private boolean equivalent(BeliefState beliefState1, BeliefState beliefState2)
            throws InvalidActionException, InterruptedException, IOException, ExecutionException {

        GlobalDataCache.incrementBeliefStateCounter();

        if (!EquivalenceCache.checkEquivalence(beliefState1.getStateRepresentative(),
                beliefState2.getStateRepresentative()).isEquivalent()) {

            return false;
        }

        for (Action action : BeliefTransitionSystem.getEnabledActions(beliefState1)) {

            List<BeliefTransition> transitions1 = BeliefTransitionSystem.applyAction(beliefState1, action);
            List<BeliefTransition> transitions2 = BeliefTransitionSystem.applyAction(beliefState2, action);

            if (transitions1.size() != transitions2.size()) {
                return false;
            }

            boolean match;
            for (BeliefTransition transition1 : transitions1) {

                match = false;
                for (BeliefTransition transition2 : transitions2) {

                    if (transition1.getTransitionProbability().equals(transition2.getTransitionProbability())) {

                        if (EquivalenceCache.checkEquivalence(transition1.getBeliefState().getStateRepresentative(),
                                transition2.getBeliefState().getStateRepresentative()).isEquivalent()) {

                            match = true;
                            if(!equivalent(transition1.getBeliefState(), transition2.getBeliefState())) {
                                return false;
                            }
                            break;
                        }
                    }
                }

                if (!match) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void run() throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {
        setEquivalent(check(initialState1, initialState2));
    }
}
