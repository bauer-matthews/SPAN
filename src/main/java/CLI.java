import cache.GlobalDataCache;
import log.Console;
import log.Severity;
import mc.DfsModelChecker;
import org.apache.commons.cli.*;
import org.apfloat.Apfloat;
import parser.*;
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

    public static void main(String[] args) {

        Parser.parseOptions(args);
        Parser.parseProtocol();

        // Construct initial state from the now loaded cache
        State initialState = new State(Collections.emptyList(), Collections.emptyList(),
                GlobalDataCache.getProtocol().getRoles());
        try {

            Apfloat maxAttackProb = DfsModelChecker.check(initialState);
            System.out.println("Max attack Prob: " + maxAttackProb);

        } catch (Exception ex) {
            Console.printError(Severity.ERROR, "Model checking failed\n" + ex.getMessage());
        }
    }
}
