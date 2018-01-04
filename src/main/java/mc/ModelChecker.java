package mc;

import process.InvalidActionException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 12/27/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public interface ModelChecker {

    void run() throws InvalidActionException, InterruptedException, IOException, ExecutionException;

    void printResults();

    void setStartTime(long startTime);

    void setStopTime(long stopTime);
}
