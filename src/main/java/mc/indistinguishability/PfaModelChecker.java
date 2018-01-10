package mc.indistinguishability;

import lp.PfaEquivLp;
import lp.glpk.CplexCodec;
import lp.glpk.GlpkEngine;
import models.ModelPairFactory;
import models.pfa.Pfa;
import process.InvalidActionException;
import process.State;
import util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

        PfaEquivLp lp = new PfaEquivLp(pfa1, pfa2);

        for (int i = 0; i < Math.max(pfa1.getLength(), pfa2.getLength()); i++) {

            lp.updateConstraints();
            File lpFile = CplexCodec.encodeLinearProg(lp);

            if (!CplexCodec.decode(GlpkEngine.invoke(lpFile))) {
                return false;
            }

        }

        return true;
    }

    @Override
    public void run() throws InvalidActionException,
            InterruptedException, IOException, ExecutionException {

        setEquivalent(check(initialState1, initialState2));
    }
}
