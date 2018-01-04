package process;

import org.apfloat.Aprational;
import protocol.role.AtomicProcess;
import protocol.role.InputProcess;
import protocol.role.OutputProcess;
import protocol.role.ProbOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * Created by matt on 7/26/17.
 */
public class TransitionSystem {

   public static Collection<Transition> applyAction(State state, Action action)
            throws InvalidActionException, ExecutionException {

        if (!(action.getRoleIndex() < state.getRoles().size())) {
            throw new InvalidActionException(Resources.BAD_ROLE_INDEX);
        }

        if (action.getRecipe() == Resources.TAU_ACTION) {
            return executeOutput(state, action);
        } else {
            return executeInput(state, action);
        }
    }

    private static Collection<Transition> executeOutput(State state, Action action)
            throws InvalidActionException, ExecutionException {

        AtomicProcess atomic = state.getRoles().get(action.getRoleIndex()).getHead();

        if (!atomic.isOutput()) {
            throw new InvalidActionException(Resources.BAD_ROLE_RECIPE);
        }

        Collection<Transition> transitions = new ArrayList<>();
        for (ProbOutput output : ((OutputProcess) atomic).getProbOutputs()) {

            transitions.add(new Transition(output.getProbability(), state,
                    state.outputTerms(output.getOutputTerms(), action.getRoleIndex(), output.getSubrole())));
        }

        return transitions;
    }

    private static Collection<Transition> executeInput(State state, Action action)
            throws InvalidActionException, ExecutionException {

        AtomicProcess atomic = state.getRoles().get(action.getRoleIndex()).getHead();

        if (!atomic.isInput()) {
            throw new InvalidActionException(Resources.BAD_ROLE_RECIPE);
        }

        Transition transition = new Transition(
                Aprational.ONE, state, state.inputTerm(((InputProcess) atomic).getVariable(),
                action.getRecipe(), action.getRoleIndex()));

        return Collections.singleton(transition);
    }
}
