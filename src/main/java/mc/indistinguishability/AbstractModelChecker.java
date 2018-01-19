package mc.indistinguishability;

import cache.*;
import process.InvalidActionException;
import process.State;
import util.ExitCode;

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
public abstract class AbstractModelChecker implements IndistinguishabilityModelChecker {

    private long startTime;
    private long stopTime;

    private boolean equivalent;

    void setEquivalent(boolean equivalent) {
        this.equivalent = equivalent;
    }

    @Override
    public abstract boolean check(State initialState1, State initialState2) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException;

    @Override
    public void printResults() {

        if (RunConfiguration.getDebug()) {
            System.out.println();
            System.out.println();
        }

        System.out.println("------------------Results------------------");
        System.out.println();

        System.out.println("Equivalent: " + equivalent);
        System.out.println("Running time: " + (stopTime - startTime) + " milliseconds");
        System.out.println("Indistinguishability method: " +
                GlobalDataCache.getMetadata().getIndistinguishabilityMethod());

        //System.out.println("Paths explored: " + GlobalDataCache.getInterleavingsExplored());

        if (GlobalDataCache.getMetadata().getIndistinguishabilityMethod().equals(IndistinguishabilityMethod.OTF)) {
            System.out.println("Belief states explored: " + GlobalDataCache.getBeliefStateCounter());
        }

        if (GlobalDataCache.getMetadata().getIndistinguishabilityMethod().equals(IndistinguishabilityMethod.PFA)) {
            System.out.println("Protocol 1 states explored: " + GlobalDataCache.getProtcol1StateCounter());
            System.out.println("Protocol 2 states explored: " + GlobalDataCache.getProtcol2StateCounter());
            System.out.println("Constrainsts: " + GlobalDataCache.getNumConstrainsts() );
            System.out.println("Constraint update time: " + GlobalDataCache.getConstraintUpdatetime());
            System.out.println("PFA model construction time: " + GlobalDataCache.getPfaConstructionTime());
        }

        //System.out.println("States explored: " + GlobalDataCache.getStateCounter());
        System.out.println();

        if (RunConfiguration.getDebug()) {

            System.out.println("----------------Debug Info-----------------");
            System.out.println();

            //System.out.println("Partial order reduction on paths: " +
            //        GlobalDataCache.getInterleavingsReduction());

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

        System.out.println("-------------------------------------------");

        System.exit(ExitCode.GOOD.getValue());
    }

    @Override
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

}
