import cache.GlobalDataCache;
import cache.RunConfiguration;
import log.Console;
import log.Severity;
import mc.DfsModelChecker;
import mc.ModelChecker;
import parser.Parser;
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

    private static long startTime;
    private static long stopTime;

    public static void main(String[] args) {

        Parser.parseOptions(args);
        Parser.parseProtocol();

        // Construct initial state from the now loaded cache
        State initialState = new State(Collections.emptyList(), Collections.emptyList(),
                GlobalDataCache.getProtocol().getRoles());
        try {

            ModelChecker mc = new DfsModelChecker(RunConfiguration.findMaxAttack());
            mc.setStartTime(System.currentTimeMillis());
            mc.check(initialState);
            mc.setStopTime(System.currentTimeMillis());
            mc.printResults();

        } catch (Exception ex) {
            ex.printStackTrace();
            Console.printError(Severity.ERROR, "Model checking failed\n" + ex.getMessage());
        }
    }
}
