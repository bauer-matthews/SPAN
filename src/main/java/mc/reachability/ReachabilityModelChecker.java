package mc.reachability;

import mc.ModelChecker;
import org.apfloat.Aprational;
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
public interface ReachabilityModelChecker extends ModelChecker {

    Aprational check(State initialState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException;

    boolean findMaximum();
}
