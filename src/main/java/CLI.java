import cache.*;
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

            System.out.println("------------------Results------------------");
            System.out.println();

            System.out.println("Maximum attack probability: " + maxAttackProb.toString(true));

            long stopTime = System.currentTimeMillis();
            System.out.println("Running time: " + (stopTime - startTime) + " milliseconds");
            System.out.println("Paths explored: " + GlobalDataCache.getInterleavingsExplored());
            System.out.println();

            if(RunConfiguration.getDebug()) {

                System.out.println("----------------Debug Info-----------------");
                System.out.println();

                System.out.println("Partial order reduction on paths: " +
                        GlobalDataCache.getInterleavingsReduction());

                System.out.println("Equivalence cache (Call/Load): "
                        + EquivalenceCache.getCacheCalls() + " / " + EquivalenceCache.getCacheLoads());

                System.out.println("Rewriting cache (Call/Load): "
                        + RewritingCache.getCacheCalls() + " / " + RewritingCache.getCacheLoads());

                System.out.println("Substitution cache (Call/Load): "
                        + SubstitutionCache.getCacheCalls() + " / " + SubstitutionCache.getCacheLoads());

                System.out.println("Unification cache (Call/Load): "
                        + UnificationCache.getCacheCalls() + " / " + UnificationCache.getCacheLoads());

                System.out.println();
            }

            if(RunConfiguration.printAttack()) {
                System.out.println("----------------Attack Tree----------------");
                System.out.println();
                System.out.println(GlobalDataCache.getAttackTree().toString());
            }

            System.out.println("-------------------------------------------");


        } catch (Exception ex) {
            ex.printStackTrace();
            Console.printError(Severity.ERROR, "Model checking failed\n" + ex.getMessage());
        }
    }
}
