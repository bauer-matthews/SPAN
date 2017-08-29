package mc;

import cache.*;
import dot.DotEncoder;
import log.Console;
import log.Severity;
import org.apfloat.Aprational;
import process.InvalidActionException;
import process.State;
import util.ExitCode;

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
public abstract class AbstractModelChecker implements ModelChecker {

    private long startTime;
    private long stopTime;

    public abstract Aprational check(State initialState) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException;

    public abstract boolean findMaximum();

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public void printResults() {

        Aprational attackProbFound = GlobalDataCache.getAttackTree().getAttackProbability();
        boolean attackFound = GlobalDataCache.getAttackTree().attackFound();

        if(RunConfiguration.getDebug()) {
            System.out.println();
            System.out.println();
        }

        System.out.println("------------------Results------------------");
        System.out.println();

        if (!attackFound) {
            System.out.println("No attack found!");
            System.out.print("Maximum attack probability: ");

        } else {

            System.out.println("Attack found!");
            if (findMaximum()) {
                System.out.print("Maximum attack probability: ");
            } else {
                System.out.print("Attack probability: ");
            }
        }

        System.out.println(attackProbFound.toString(true));
        System.out.println("Running time: " + (stopTime - startTime) + " milliseconds");
        System.out.println("Paths explored: " + GlobalDataCache.getInterleavingsExplored());
        System.out.println();

        if (RunConfiguration.getDebug()) {

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

            System.out.println("Action factory cache (Call/Load): "
                    + ActionFactory2Cache.getCacheCalls() + " / " + ActionFactory2Cache.getCacheLoads());

            System.out.println("Max Action Set Size: " + GlobalDataCache.getMaxActionSetSize());

            System.out.println();
        }

        if ((RunConfiguration.printAttack() && attackFound)) {

            System.out.println("----------------Attack Tree----------------");
            System.out.println();
            System.out.println(GlobalDataCache.getAttackTree().toString());
        }

        System.out.println("-------------------------------------------");

        if ((RunConfiguration.outputToDot() && attackFound) ||
                (RunConfiguration.findMaxAttack() &&
                        GlobalDataCache.getAttackTree().getAttackProbability().compareTo(Aprational.ZERO) > 0)) {
            try {
                DotEncoder.printToDotFile(RunConfiguration.getDotFile(), GlobalDataCache.getAttackTree());
            } catch (IOException ex) {
                Console.printError(Severity.ERROR, ex.getMessage());
            }
        }

        System.exit(ExitCode.GOOD.getValue());
    }
}
