package process;

import org.apfloat.Apfloat;
import protocol.role.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by matt on 7/26/17.
 */
public class TransitionSystem {

    static Collection<Transition> applyAction(State state, Action action) throws InvalidActionException {

        if (!(action.getRoleIndex() < state.getRoles().size())) {
            throw new InvalidActionException(Resources.BAD_ROLE_INDEX);
        }

        if (action.getRecipe() == null) {
            return executeOutput(state, action);
        } else {
            return executeInput(state, action);
        }
    }

    private static Collection<Transition> executeOutput(State state, Action action) throws InvalidActionException {

        AtomicProcess atomic = state.getRoles().get(action.getRoleIndex()).getHead();

        if(!atomic.isOutput()) {
            throw new InvalidActionException(Resources.BAD_ROLE_RECIPE);
        }

        Collection<Transition> transitions = new ArrayList<>();
        for (ProbOutput output : ((OutputProcess) atomic).getProbOutputs()) {
            new Transition(output.getProbability(),
                    state.outputTerms(output.getOutputTerms(), action.getRoleIndex()));
        }

        return transitions;
    }

    private static Collection<Transition> executeInput(State state, Action action) throws InvalidActionException {

        AtomicProcess atomic = state.getRoles().get(action.getRoleIndex()).getHead();

        if(!atomic.isInput()) {
            throw new InvalidActionException(Resources.BAD_ROLE_RECIPE);
        }

        Transition transition = new Transition(
                Apfloat.ONE, state.inputTerm(((InputProcess) atomic).getVariable(),
                action.getRecipe(), action.getRoleIndex()));

        return Collections.singleton(transition);
    }
}
