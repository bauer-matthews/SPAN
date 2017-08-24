package mc;

import cache.*;
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

        System.out.println("------------------Results------------------");
        System.out.println();

        if(findMaximum()) {
            System.out.print("Maximum attack probability: ");
        } else {
            System.out.print("Attack found with probability: ");
        }

        System.out.println( attackProbFound.toString(true));
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

            System.out.println();
        }

        if (RunConfiguration.printAttack()) {
            System.out.println("----------------Attack Tree----------------");
            System.out.println();
            System.out.println(GlobalDataCache.getAttackTree().toString());
        }

        System.out.println("-------------------------------------------");

        System.exit(0);
    }
}
