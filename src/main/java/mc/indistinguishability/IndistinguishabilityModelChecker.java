package mc.indistinguishability;

import mc.ModelChecker;
import process.InvalidActionException;
import process.State;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface IndistinguishabilityModelChecker extends ModelChecker {

    boolean check(State initialState1, State initialState2) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException;
}
