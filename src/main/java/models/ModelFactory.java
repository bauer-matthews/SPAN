package models;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import models.pfa.PfaBuilder;
import models.pfa.StateProb;
import models.pomdp.Pomdp;
import models.pomdp.StateObsProb;
import models.pomdp.StateObservationAction;
import models.pomdp.SymbolicTransition;
import process.*;
import util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ModelFactory {

    private static final ActionIndexer ACTION_INDEXER;
    private static final StateIndexer STATE_INDEXER;
    private static final ObservationIndexer OBSERVATION_INDEXER;
    private static final AlphabetIndexer ALPHABET_INDEXER;
    private static final Collection<Integer> STATES_EXPLORED;
    private static int length;
    private static int maxLength;

    static {
        ACTION_INDEXER = new ActionIndexer();
        STATE_INDEXER = new StateIndexer();
        OBSERVATION_INDEXER = new ObservationIndexer();
        ALPHABET_INDEXER = new AlphabetIndexer();
        STATES_EXPLORED = new HashSet<>();
        length = 0;
        maxLength = 0;
    }

    static int getLength() {
        return length;
    }

    static int numActions() {
        return ModelFactory.ACTION_INDEXER.getNumActions();
    }

    static int numObservations() {
        return ModelFactory.OBSERVATION_INDEXER.getNumObservations();
    }

    private static void resetModelFactory() {
        ACTION_INDEXER.reset();
        ALPHABET_INDEXER.reset();
        STATE_INDEXER.reset();
        OBSERVATION_INDEXER.reset();
        STATES_EXPLORED.clear();
        length = 0;
    }

    public static Pomdp generatePomdp(State initialState)
            throws ExecutionException, InvalidActionException, InterruptedException, IOException {

        resetModelFactory();

        Map<Integer, List<SymbolicTransition>> transitionMap = new HashMap<>();
        addPomdpTransitions(initialState, transitionMap);

        return new Pomdp(STATE_INDEXER.getNumStates(), ACTION_INDEXER.getNumActions(),
                OBSERVATION_INDEXER.getNumObservations(), transitionMap);
    }

    private static void addPomdpTransitions(State state, Map<Integer, List<SymbolicTransition>> transitionMap)
            throws ExecutionException, InvalidActionException, InterruptedException, IOException {

        length++;
        updateMaxLength();
        Integer stateIndex = STATE_INDEXER.getStateIndex(state);

        if (!STATES_EXPLORED.contains(stateIndex)) {

            STATES_EXPLORED.add(stateIndex);
            List<SymbolicTransition> symbolicTransitions = new ArrayList<>();

            for (Action action : state.getEnabledActions()) {

                StateObservationAction stateObservationAction = new StateObservationAction(stateIndex,
                        OBSERVATION_INDEXER.getObservationIndex(state),
                        ACTION_INDEXER.getActionIndex(action));

                Multiset<StateObsProb> stateProbs = HashMultiset.create();

                for (Transition transition : TransitionSystem.applyAction(state, action)) {

                    stateProbs.add(new StateObsProb(STATE_INDEXER.getStateIndex(transition.getNewState()),
                            OBSERVATION_INDEXER.getObservationIndex(transition.getNewState()),
                            transition.getTransitionProbability()));

                    addPomdpTransitions(transition.getNewState(), transitionMap);
                    symbolicTransitions.add(new SymbolicTransition(stateObservationAction, stateProbs));
                }
            }

            transitionMap.put(stateIndex, symbolicTransitions);
        }

        length--;
    }

    static PfaBuilder exploreEnabledTransitions(State initialState)
            throws ExecutionException, InvalidActionException, InterruptedException, IOException {

        STATE_INDEXER.reset();
        STATES_EXPLORED.clear();

        PfaBuilder builder = new PfaBuilder();
        Map<Integer, Map<Integer, models.pfa.SymbolicTransition>> transitionMap = new HashMap<>();
        addPfaTransitions(initialState, builder, transitionMap);

        builder.transitionMap(transitionMap)
                .numStates(STATE_INDEXER.getNumStates() + 1)
                .length(maxLength)
                .alphabetIndexer(ALPHABET_INDEXER);

        return builder;
    }

    private static void addPfaTransitions(State state, PfaBuilder builder,
                                          Map<Integer, Map<Integer, models.pfa.SymbolicTransition>> transitionMap)
            throws ExecutionException, InvalidActionException, InterruptedException, IOException {

        length++;
        updateMaxLength();
        Integer stateIndex = STATE_INDEXER.getStateIndex(state);

        if (!STATES_EXPLORED.contains(stateIndex)) {

            STATES_EXPLORED.add(stateIndex);

            transitionMap.put(stateIndex, new HashMap<>());

            int observationIndex = OBSERVATION_INDEXER.getObservationIndex(state);

            if (state.getEnabledActions().isEmpty()) {
                builder.addTerminalState(stateIndex, observationIndex);
            }

            for (Action action : state.getEnabledActions()) {

                int actionIndex = ACTION_INDEXER.getActionIndex(action);
                int inputSymbol = ALPHABET_INDEXER.getSymbolIndex(new Pair<>(actionIndex, observationIndex));

                Multiset<StateProb> stateProbs = HashMultiset.create();

                for (Transition transition : TransitionSystem.applyAction(state, action)) {

                    stateProbs.add(new StateProb(STATE_INDEXER.getStateIndex(transition.getNewState()),
                            transition.getTransitionProbability()));

                    addPfaTransitions(transition.getNewState(), builder, transitionMap);
                }

                transitionMap.get(stateIndex).put(inputSymbol, new models.pfa.SymbolicTransition(stateProbs));
            }

        }
        length--;
    }

    private static void updateMaxLength() {
        if (length > maxLength) {
            maxLength = length;
        }
    }
}
