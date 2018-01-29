package mc.indistinguishability;

import cache.EquivalenceCache;
import cache.GlobalDataCache;
import models.ObservationIndexer;
import org.apfloat.Aprational;
import process.*;
import util.Pair;

import java.io.IOException;
import java.util.*;
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

    private static ObservationIndexer INDEXER = GlobalDataCache.getObservationIndexer();

    private final State initialState1;
    private final State initialState2;
    private final Map<Pair<List<Belief>, List<Belief>>, Boolean> beliefPairsVisited;

    public OnTheFlyModelChecker(State initialState1, State initialState2) {

        Objects.requireNonNull(initialState1);
        Objects.requireNonNull(initialState2);

        this.initialState1 = initialState1;
        this.initialState2 = initialState2;
        this.beliefPairsVisited = new HashMap<>();
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

        boolean equiv =  equivalent(beliefState1, beliefState2);
        GlobalDataCache.setBeliefStateCounter(beliefPairsVisited.size());

        return equiv;
    }

    private boolean equivalent(BeliefState beliefState1, BeliefState beliefState2)
            throws InvalidActionException, InterruptedException, IOException, ExecutionException {

        if(beliefPairsVisited.get(new Pair<>(beliefState1.getBeliefs(), beliefState2.getBeliefs())) != null) {
            return true;
        }
        beliefPairsVisited.put(new Pair<>(beliefState1.getBeliefs(), beliefState2.getBeliefs()), Boolean.TRUE);

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

                        if(INDEXER.getObservationIndex(transition1.getBeliefState().getStateRepresentative()) ==
                                INDEXER.getObservationIndex(transition2.getBeliefState().getStateRepresentative())) {

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
