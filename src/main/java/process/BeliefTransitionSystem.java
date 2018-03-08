package process;

import cache.EquivalenceCache;
import cache.GlobalDataCache;
import equivalence.EquivalenceCheckResult;
import org.apfloat.Aprational;
import parser.protocol.ProtocolType;
import util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/8/2017.
 */
public class BeliefTransitionSystem {

    public static List<Action> getEnabledActions(BeliefState beliefState) throws ExecutionException {

        Set<Action> enabledActionSet = new HashSet<>();

        for (Belief belief : beliefState.getBeliefs()) {
            enabledActionSet.addAll(belief.getState().getEnabledActions());
        }

        List<Action> enabledActions = new ArrayList<>();
        enabledActions.addAll(enabledActionSet);

        return enabledActions;
    }

    public static List<BeliefTransition> applyAction(BeliefState beliefState, Action action)
            throws InvalidActionException, IOException, InterruptedException, ExecutionException {

        List<BeliefTransition> beliefTransitions = new ArrayList<>();
        List<Transition> transitions = new ArrayList<>();

        for (Belief belief : beliefState.getBeliefs()) {
            transitions.addAll(TransitionSystem.applyAction(belief.getState(), action));
        }

        Map<Integer, ArrayList<Transition>> observations;
        if (GlobalDataCache.getProtocolType().equals(ProtocolType.REACHABILITY)) {
            observations = groupTransitionsReach(transitions);
        } else {
            observations = groupTransitionsEquiv(transitions);
        }

        for (Integer obsIndex : observations.keySet()) {

            State[] states = observations.get(obsIndex).stream()
                    .map(Transition::getNewState).distinct().toArray(State[]::new);

            Aprational bottomSum = Aprational.ZERO;

            for (Transition transition : observations.get(obsIndex)) {

                bottomSum = bottomSum.add(transition.getTransitionProbability()
                        .multiply(beliefState.getStateProb(transition.getOriginalState())));
            }

            List<Belief> beliefs = new ArrayList<>();
            for (State state : states) {

                // Sum of b(s) * P(s,a)(s') for all states s from original belief state b,
                // where s' (newState) has observation o
                Aprational topSum = Aprational.ZERO;

                for (Transition transition : observations.get(obsIndex)) {
                    if (transition.getNewState().equals(state)) {
                        topSum = topSum.add(transition.getTransitionProbability()
                                .multiply(beliefState.getStateProb(transition.getOriginalState())));
                    }
                }

                if (!bottomSum.equals(Aprational.ZERO) && !(topSum.equals(Aprational.ZERO))) {
                    beliefs.add(new Belief(state, topSum.divide(bottomSum)));
                }
            }

            List<Action> newActionHistory = new ArrayList<>();
            newActionHistory.addAll(beliefState.getActionHistory());
            newActionHistory.add(action);

            beliefTransitions.add(new BeliefTransition(bottomSum, new BeliefState(beliefs, newActionHistory)));
        }

        return beliefTransitions;
    }

    private static Map<Integer, ArrayList<Transition>> groupTransitionsEquiv(
            List<Transition> transitions) throws ExecutionException, IOException, InterruptedException {

        Map<Integer, ArrayList<Transition>> observations = new HashMap<>();

        for (Transition transition : transitions) {

            Integer index = GlobalDataCache.getObservationIndexer().getObservationIndex(transition.getNewState());

            observations.computeIfAbsent(index, k -> new ArrayList<>());
            observations.get(index).add(transition);
        }

        return observations;
    }

    private static Map<Integer, ArrayList<Transition>> groupTransitionsReach2(
            List<Transition> transitions) throws ExecutionException, IOException, InterruptedException {

        Map<Integer, ArrayList<Transition>> observations = new HashMap<>();

        for (Transition transition : transitions) {

            Pair<Integer, Boolean> obsAttackPair =
                    GlobalDataCache.getObsAttackIndexer().getObservationAttackPair(transition.getNewState());

            observations.computeIfAbsent(obsAttackPair.getKey(), k -> new ArrayList<>());
            observations.get(obsAttackPair.getKey()).add(transition);
            transition.getNewState().setAttackState(obsAttackPair.getValue());
        }

        return observations;
    }

    private static Map<Integer, ArrayList<Transition>> groupTransitionsReach(
            List<Transition> transitions) throws ExecutionException {

        Map<Integer, ArrayList<Transition>> observations = new HashMap<>();

        int numObservations = 0;
        for (Transition transition : transitions) {

            boolean match = false;

            EquivalenceCheckResult result = null;

            innerloop:
            for (int i = 0; i < numObservations; i++) {

                result = EquivalenceCache.checkEquivalence(transition.getNewState(),
                        observations.get(i).get(0).getNewState());

                transition.getNewState().setAttackState(result.isPhi1Attack());

                if (result.isEquivalent()) {
                    observations.get(i).add(transition);
                    match = true;
                    break innerloop;
                }
            }

            if (!match) {

                if (result == null) {
                    result = EquivalenceCache.checkEquivalence(transition.getNewState(), transition.getNewState());
                    transition.getNewState().setAttackState(result.isPhi1Attack());
                }

                ArrayList<Transition> newList = new ArrayList<>();
                newList.add(transition);

                observations.put(numObservations, newList);
                numObservations++;
            }
        }

        return observations;
    }
}

