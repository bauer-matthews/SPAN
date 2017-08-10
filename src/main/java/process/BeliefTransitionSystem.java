package process;

import org.apfloat.Apfloat;

import java.io.IOException;
import java.util.*;

/**
 * Created by mbauer on 8/8/2017.
 */
public class BeliefTransitionSystem {


    public static List<Action> getEnabledActions(BeliefState beliefState) {

        // NOTE: every state in a belief state has the same enabled actions because
        // enabled actions are part of the observable state.
        return beliefState.getBeliefs().get(0).getState().getEnabledActions();
    }

    public static List<BeliefTransition> applyAction(BeliefState beliefState, Action action)
            throws InvalidActionException, IOException, InterruptedException {

        List<BeliefTransition> beliefTransitions = new ArrayList<>();
        List<Transition> transitions = new ArrayList<>();

        for (Belief belief : beliefState.getBeliefs()) {
            transitions.addAll(TransitionSystem.applyAction(belief.getState(), action));
        }

        Map<Integer, ArrayList<Transition>> observations = new HashMap<>();

        int numObservations = 0;
        for (Transition transition : transitions) {

            boolean match = false;

            EquivalenceCheckResult result = null;

            innerloop:
            for (int i = 0; i < numObservations; i++) {

                result = EquivalenceChecker.check(transition.getNewState(),
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
                    result = EquivalenceChecker.check(transition.getNewState(), transition.getNewState());
                    transition.getNewState().setAttackState(result.isPhi1Attack());
                }

                ArrayList<Transition> newList = new ArrayList<>();
                newList.add(transition);

                observations.put(numObservations, newList);
                numObservations++;
            }
        }

        for (int i = 0; i < numObservations; i++) {

            State[] states = observations.get(i).stream()
                    .map(Transition::getNewState).distinct().toArray(State[]::new);

            Apfloat bottomSum = Apfloat.ZERO;

            for(Transition transition : observations.get(i)) {

                bottomSum = bottomSum.add(transition.getTransitionProbability()
                        .multiply(beliefState.getStateProb(transition.getOriginalState())));
            }

            List<Belief> beliefs = new ArrayList<>();
            for (State state : states) {

                // Sum of b(s) * P(s,a)(s') for all states s from original belief state b,
                // where s' (newState) has observation o
                Apfloat topSum = Apfloat.ZERO;

                for(Transition transition : observations.get(i)) {
                    if(transition.getNewState().equals(state)) {
                        topSum = topSum.add(transition.getTransitionProbability()
                                .multiply(beliefState.getStateProb(transition.getOriginalState())));
                    }
                }

                if (!bottomSum.equals(Apfloat.ZERO) && !(topSum.equals(Apfloat.ZERO))) {
                    beliefs.add(new Belief(state, topSum.divide(bottomSum)));
                }
            }

            beliefTransitions.add(new BeliefTransition(bottomSum, new BeliefState(beliefs)));
        }

        return beliefTransitions;
    }
}

