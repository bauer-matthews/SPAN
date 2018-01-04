package pomdp;

import process.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class PomdpFactory {

    private static final ActionIndexer ACTION_INDEXER;
    private static final StateIndexer STATE_INDEXER;
    private static final ObservationIndexer OBSERVATION_INDEXER;
    private static final Collection<Long> STATES_EXPLORED;

    static {
        ACTION_INDEXER = new ActionIndexer();
        STATE_INDEXER = new StateIndexer();
        OBSERVATION_INDEXER = new ObservationIndexer();
        STATES_EXPLORED = new HashSet<>();
    }

    public static Map<StateAction, Collection<StateProb>> generate(State initialState)
            throws ExecutionException, InvalidActionException, InterruptedException, IOException {

        ACTION_INDEXER.reset();
        STATE_INDEXER.reset();
        OBSERVATION_INDEXER.reset();
        STATES_EXPLORED.clear();

        Map<StateAction, Collection<StateProb>> transitionMap = new HashMap<>();
        addTransitions(initialState, transitionMap);

        return transitionMap;
    }

    public static long getNumStates() {
        return STATE_INDEXER.getNumStates();
    }

    private static void addTransitions(State state, Map<StateAction, Collection<StateProb>> transitionMap)
            throws ExecutionException, InvalidActionException, InterruptedException, IOException {

        Long stateIndex = STATE_INDEXER.getStateIndex(state);

        if (!STATES_EXPLORED.contains(stateIndex)) {

            STATES_EXPLORED.add(stateIndex);

            for (Action action : state.getEnabledActions()) {

                StateAction stateAction = new StateAction(stateIndex,
                        ACTION_INDEXER.getActionIndex(action));

                Collection<StateProb> stateProbs = new HashSet<>();

                for (Transition transition : TransitionSystem.applyAction(state, action)) {

                    stateProbs.add(new StateProb(STATE_INDEXER.getStateIndex(transition.getNewState()),
                            transition.getTransitionProbability()));

                    OBSERVATION_INDEXER.getObservationIndex(transition.getNewState());
                    addTransitions(transition.getNewState(), transitionMap);
                }

                transitionMap.put(stateAction, stateProbs);
            }
        }
    }
}
