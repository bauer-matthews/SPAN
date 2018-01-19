package mc.indistinguishability;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import lp.PfaEquivLp;
import lp.glpk.CplexCodec;
import lp.glpk.GlpkEngine;
import models.ModelPairFactory;
import models.pfa.Pfa;
import process.InvalidActionException;
import process.State;
import util.Pair;
import util.ParametricString;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SynchronousQueue;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class PfaModelChecker extends AbstractModelChecker {

    private final State initialState1;
    private final State initialState2;

    public PfaModelChecker(State initialState1, State initialState2) {

        Objects.requireNonNull(initialState1);
        Objects.requireNonNull(initialState2);

        this.initialState1 = initialState1;
        this.initialState2 = initialState2;
    }

    @Override
    public boolean check(State state1, State state2) throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {


        Pair<Pfa, Pfa> pfas = ModelPairFactory.generatePfaPair(state1, state2);
        Pfa pfa1 = pfas.getKey();
        Pfa pfa2 = pfas.getValue();

        long start = System.currentTimeMillis();

        PfaEquivLp lp = new PfaEquivLp(pfa1, pfa2);

        for (int i = 0; i < Math.max(pfa1.getLength(), pfa2.getLength()); i++) {

            lp.updateConstraints();
            GlobalDataCache.setNumConstrainsts(lp.getConstraints().size());

            if(true) {
                if(!lp.constraintsPass()) {
                    return false;
                }
            } else {
                File lpFile = CplexCodec.encodeLinearProg(lp);
                if (!CplexCodec.decode(GlpkEngine.invoke(lpFile))) {
                    return false;
                }
            }

        }

        long stop = System.currentTimeMillis();
        GlobalDataCache.incrimentConstraintUpdatetime(stop - start);

        return true;
    }

    @Override
    public void run() throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        setEquivalent(check(initialState1, initialState2));
    }
}
