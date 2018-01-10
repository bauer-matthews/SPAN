package models.pfa;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import models.AlphabetIndexer;
import org.apfloat.Aprational;
import util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/9/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class PfaBuilder {

    // Required Parameters
    private Map<Integer, Map<Integer, models.pfa.SymbolicTransition>> transitionMap;
    private Map<Integer, Integer> terminalStates;
    private Integer numStates;
    private Integer numSymbols;
    private AlphabetIndexer alphabetIndexer;
    private Integer numActions;
    private Integer numObservations;
    private Integer length;

    public PfaBuilder() {
        terminalStates = new HashMap<>();
    }

    public PfaBuilder transitionMap(Map<Integer, Map<Integer, models.pfa.SymbolicTransition>> transitionMap) {
        this.transitionMap = transitionMap;
        return this;
    }

    public PfaBuilder numStates(int numStates) {
        this.numStates = numStates;
        return this;
    }

    public PfaBuilder numActions(int numActions) {
        this.numActions = numActions;
        return this;
    }

    public PfaBuilder numObservations(int numObservations) {
        this.numObservations = numObservations;
        return this;
    }

    public PfaBuilder length(int length) {
        this.length = length;
        return this;
    }

    public PfaBuilder alphabetIndexer(AlphabetIndexer alphabetIndexer) {
        this.alphabetIndexer = alphabetIndexer;
        return this;
    }

    public PfaBuilder addTerminalState(int state, int observation) {
        terminalStates.put(state, observation);
        return this;
    }

    public Pfa buildPfa() {

        if (transitionMap == null || numStates == null || alphabetIndexer == null ||
                numObservations == null || numActions == null || length == null) {
            throw new IllegalStateException("Builder is missing required fields");
        }

        transitionMap.put(numStates - 1, new HashMap<>());

        generateAllSymbols();
        addUnenabledTransitions();
        addBadTransitions();

        return new Pfa(numStates, numSymbols, length, transitionMap);
    }

    private void addUnenabledTransitions() {

        for (int stateIndex = 0; stateIndex < numStates; stateIndex++) {

            Integer obsIndex = terminalStates.get(stateIndex);
            if (obsIndex != null) {

                Map<Integer, SymbolicTransition> symbolicTransitions = transitionMap.get(stateIndex);

                StateProb stateProb = new StateProb(stateIndex, Aprational.ONE);
                Multiset<StateProb> stateProbSet = HashMultiset.create();
                stateProbSet.add(stateProb);
                models.pfa.SymbolicTransition selfLoop = new models.pfa.SymbolicTransition(stateProbSet);

                for (int actionIndex = 0; actionIndex < numActions; actionIndex++) {
                    symbolicTransitions.put(alphabetIndexer.getSymbolIndex(
                            new Pair<>(actionIndex, obsIndex)), selfLoop);
                }
            }
        }
    }

    private void addBadTransitions() {

        for (int stateIndex = 0; stateIndex < numStates; stateIndex++) {

            Map<Integer, SymbolicTransition> symbolicTransitions = transitionMap.get(stateIndex);

            StateProb stateProb = new StateProb(numStates - 1, Aprational.ONE);
            Multiset<StateProb> stateProbSet = HashMultiset.create();
            stateProbSet.add(stateProb);
            models.pfa.SymbolicTransition badTransition = new models.pfa.SymbolicTransition(stateProbSet);

            for (int symbolIndex = 0; symbolIndex < numSymbols; symbolIndex++) {
                symbolicTransitions.putIfAbsent(symbolIndex, badTransition);
            }
        }
    }

    private void generateAllSymbols() {

        for (int actionIndex = 0; actionIndex < numActions; actionIndex++) {
            for (int observationIndex = 0; observationIndex < numObservations; observationIndex++) {
                alphabetIndexer.getSymbolIndex(new Pair<>(actionIndex, observationIndex));
            }
        }

        numSymbols = alphabetIndexer.getAlphabetSize();
    }
}
