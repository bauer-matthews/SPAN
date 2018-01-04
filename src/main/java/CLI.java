import cache.GlobalDataCache;
import cache.RunConfiguration;
import log.Console;
import log.Severity;
import mc.ModelChecker;
import mc.indistinguishability.IndistinguishabilityModelChecker;
import mc.reachability.OnTheFlyModelChecker;
import mc.reachability.ReachabilityModelChecker;
import parser.Parser;
import parser.protocol.ProtocolType;
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

        if (GlobalDataCache.getProtocolType().equals(ProtocolType.REACHABILITY)) {

            // Construct initial state from the now loaded cache
            State initialState = new State(Collections.emptyList(), Collections.emptyList(),
                    GlobalDataCache.getReachabilityProtocol().getRoles());

            ReachabilityModelChecker mc = new OnTheFlyModelChecker(initialState, RunConfiguration.findMaxAttack());
            runModelChecker(mc);
        }

        if(GlobalDataCache.getProtocolType().equals(ProtocolType.INDISTINGUISHABILITY)) {

            // Construct initial states from the now loaded cache
            State initialState1 = new State(Collections.emptyList(), Collections.emptyList(),
                    GlobalDataCache.getIndistinguishabilityProtocol().getRoles1());

            State initialState2 = new State(Collections.emptyList(), Collections.emptyList(),
                    GlobalDataCache.getIndistinguishabilityProtocol().getRoles2());

            IndistinguishabilityModelChecker mc = new mc.indistinguishability.
                    OnTheFlyModelChecker(initialState1, initialState2);
            runModelChecker(mc);
        }
    }

    private static void runModelChecker(ModelChecker mc) {

        try {
            mc.setStartTime(System.currentTimeMillis());
            mc.run();
            mc.setStopTime(System.currentTimeMillis());
            mc.printResults();

        } catch (Exception ex) {
            ex.printStackTrace();
            Console.printError(Severity.ERROR, "Model checking failed\n" + ex.getMessage());
        }
    }
}
