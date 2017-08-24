package mc;

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
public interface ModelChecker {

    Aprational check(State initialState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException;

    void printResults();

    void setStartTime(long startTime);

    void setStopTime(long stopTime);

    boolean findMaximum();
}
