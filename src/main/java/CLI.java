import cache.GlobalDataCache;
import cache.RunConfiguration;
import log.Console;
import log.Severity;
import mc.DfsModelChecker;
import org.apache.commons.cli.*;
import org.apfloat.Apfloat;
import parser.*;
import parser.Parser;
import process.InvalidActionException;
import process.State;

import java.util.Collections;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class CLI {

    public static void main(String[] args) {

        Parser.parseOptions(args);
        Parser.parseProtocol();

        long startTime = System.currentTimeMillis();

        // Construct initial state from the now loaded cache
        State initialState = new State(Collections.emptyList(), Collections.emptyList(),
                GlobalDataCache.getProtocol().getRoles());
        try {
            Apfloat maxAttackProb = DfsModelChecker.check(initialState);
            System.out.println("Max attack Prob: " + maxAttackProb.toString(true));

            long stopTime = System.currentTimeMillis();
            System.out.println("Running Time: " + (stopTime - startTime) + " milliseconds");
            System.out.println("Interleavings explored: " + GlobalDataCache.getInterleavingsExplored());

            if(RunConfiguration.getDebug()) {
                System.out.println("Partial order reduction on interleavings: " +
                        GlobalDataCache.getInterleavingsReduction());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Console.printError(Severity.ERROR, "Model checking failed\n" + ex.getMessage());
        }
    }
}
