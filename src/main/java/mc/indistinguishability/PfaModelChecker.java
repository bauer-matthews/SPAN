package mc.indistinguishability;

import cache.GlobalDataCache;
import pomdp.Pomdp;
import pomdp.PomdpFactory;
import pomdp.StateObservationAction;
import process.InvalidActionException;
import process.State;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class PfaModelChecker extends AbstractModelChecker {

    private final State initialState1;
    private final State initialState2;

    public PfaModelChecker(State initialState1, State initialState2) {

        Objects.requireNonNull(initialState1);
        Objects.requireNonNull(initialState2);

        this.initialState1 = initialState1;
        this.initialState2 = initialState2;
    }

    @Override
    public boolean check(State state1, State state2) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        Pomdp Pomdp1 = PomdpFactory.generate(state1);
        GlobalDataCache.setProtcol1StateCounter(PomdpFactory.getNumStates());

        Pomdp Pomdp2 = PomdpFactory.generate(state2);
        GlobalDataCache.setProtcol2StateCounter(PomdpFactory.getNumStates());

        // TODO: Simple check - action size/obs size are different

        for(StateObservationAction sa : Pomdp1.getTransitions().keySet()) {
            System.out.println("S" + sa.getStateIndex() + " O" + sa.getObservationIndex() + " A" + sa.getActionIndex());
        }

        System.out.println();

        for(StateObservationAction sa : Pomdp2.getTransitions().keySet()) {
            System.out.println("S" + sa.getStateIndex() + " O" + sa.getObservationIndex() + " A" + sa.getActionIndex());
        }

        return true;
    }

    @Override
    public void run() throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        setEquivalent(check(initialState1, initialState2));
    }
}
