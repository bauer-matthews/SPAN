package models;

import cache.GlobalDataCache;
import models.pfa.Pfa;
import models.pfa.PfaBuilder;
import process.InvalidActionException;
import process.State;
import util.Pair;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/9/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ModelPairFactory {

    public static Pair<Pfa, Pfa> generatePfaPair(State state1, State state2) throws InvalidActionException,
            ExecutionException, InterruptedException, IOException {

        PfaBuilder builder1 = ModelFactory.exploreEnabledTransitions(state1);
        PfaBuilder builder2 = ModelFactory.exploreEnabledTransitions(state2);

        builder1.numActions(ModelFactory.numActions());
        builder1.numObservations(ModelFactory.numObservations());

        builder2.numActions(ModelFactory.numActions());
        builder2.numObservations(ModelFactory.numObservations());

        Pfa pfa1 = builder1.buildPfa();
        Pfa pfa2 = builder2.buildPfa();

        GlobalDataCache.setProtcol1StateCounter(pfa1.getNumStates());
        GlobalDataCache.setProtcol2StateCounter(pfa2.getNumStates());

        return new Pair<>(pfa1, pfa2);
    }
}
