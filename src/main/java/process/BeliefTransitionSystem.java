package process;

import org.apfloat.Apfloat;

import java.io.IOException;
import java.util.*;

/**
 * Created by mbauer on 8/8/2017.
 */
public class BeliefTransitionSystem {

    static Collection<BeliefTransition> applyAction(BeliefState beliefState, Action action)
            throws InvalidActionException, IOException, InterruptedException {

        Collection<BeliefTransition> beliefTransitions = new ArrayList<>();
        Collection<Transition> transitions = new ArrayList<>();

        for (Belief belief : beliefState.getBeliefs()) {
            transitions.addAll(TransitionSystem.applyAction(belief.getState(), action));
        }

        Map<Integer, List<Transition>> observations = new HashMap<>();

        int numObservations = 0;
        for (Transition transition : transitions) {

            boolean match = false;

            innerloop:
            for (int i = 0; i < numObservations; i++) {
                if (EquivalenceChecker.check(transition.getNewState(), observations.get(i).get(0).getNewState())) {
                    observations.get(i).add(transition);
                    match = true;
                    break innerloop;
                }
            }

            if (!match) {
                observations.put(numObservations, Collections.singletonList(transition));
                numObservations++;
            }
        }

        for (int i = 0; i < numObservations; i++) {

            State[] states = observations.get(i).stream()
                    .map(Transition::getNewState).distinct().toArray(State[]::new);

            Apfloat bottomSum = Apfloat.ZERO;
            observations.get(i).forEach(transition -> bottomSum.add(transition.getTransitionProbability()
                    .multiply(beliefState.getProb(transition.getOriginalState()))));

            List<Belief> beliefs = new ArrayList<>();
            for (State state : states) {

                // Sum of b(s) * P(s,a)(s') for all states s from original belief state b,
                // where s' (newState) has observation o
                Apfloat topSum = Apfloat.ZERO;
                observations.get(i).stream().filter(transition -> transition.getNewState().equals(state))
                        .forEach(transition -> topSum.add(transition.getTransitionProbability()
                                .multiply(beliefState.getProb(transition.getOriginalState()))));

                if (!bottomSum.equals(Apfloat.ZERO) && !(topSum.equals(Apfloat.ZERO))) {
                    beliefs.add(new Belief(state, topSum.divide(bottomSum)));
                }
            }

            beliefTransitions.add(new BeliefTransition(bottomSum, new BeliefState(beliefs)));
        }

        return beliefTransitions;
    }
}

