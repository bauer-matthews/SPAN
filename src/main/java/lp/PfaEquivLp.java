package lp;

import com.google.common.base.MoreObjects;
import models.pfa.Pfa;
import models.pfa.StateProb;
import models.pfa.SymbolicTransition;
import org.apfloat.Aprational;
import util.aprational.ApUtils;

import java.util.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class PfaEquivLp {

    private final Pfa pfa1;
    private final Pfa pfa2;
    private final VarList maximize;
    private final Set<Constraint> constraints;
    private final int numLeftVars;
    private final int numRightVars;
    private final Map<Integer, Map<Integer, Aprational[]>> predecessorMap1;
    private final Map<Integer, Map<Integer, Aprational[]>> predecessorMap2;
    private Set<Constraint> lastConstraints;

    public PfaEquivLp(Pfa pfa1, Pfa pfa2) {

        Objects.requireNonNull(pfa1);
        Objects.requireNonNull(pfa2);

        this.pfa1 = pfa1;
        this.pfa2 = pfa2;

        Aprational[] leftProbs = new Aprational[pfa1.getNumStates()];
        for (int i = 0; i < leftProbs.length - 1; i++) {
            leftProbs[i] = Aprational.ONE;
        }
        leftProbs[leftProbs.length - 1] = Aprational.ZERO;

        Aprational[] rightProbs = new Aprational[pfa2.getNumStates()];
        for (int i = 0; i < rightProbs.length - 1; i++) {
            rightProbs[i] = Aprational.ONE;
        }
        rightProbs[rightProbs.length - 1] = Aprational.ZERO;

        Constraint initialConstraint = new Constraint(new VarList(leftProbs, "x", true),
                new VarList(rightProbs, "y", false), Operation.EQ);

        this.maximize = new VarList(new Aprational[]{Aprational.ONE}, "x", true);
        this.constraints = new HashSet<>();
        constraints.add(initialConstraint);
        this.lastConstraints = new HashSet<>();
        lastConstraints.add(initialConstraint);

        this.numLeftVars = pfa1.getNumStates();
        this.numRightVars = pfa2.getNumStates();

        this.predecessorMap1 = new HashMap<>();
        this.predecessorMap2 = new HashMap<>();
        initPredecessorMap(predecessorMap1, pfa1);
        initPredecessorMap(predecessorMap2, pfa2);
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public long getNumLeftVars() {
        return numLeftVars;
    }

    public long getNumRightVars() {
        return numRightVars;
    }

    public VarList getMaximize() {
        return maximize;
    }

    private void initPredecessorMap(Map<Integer, Map<Integer, Aprational[]>> predecessorMap, Pfa pfa) {

        Map<Integer, Map<Integer, SymbolicTransition>> transitions = pfa.getTransitions();

        for (int stateIndex = 0; stateIndex < pfa.getNumStates(); stateIndex++) {
            for (int symbolIndex = 0; symbolIndex < pfa.getNumSymbols(); symbolIndex++) {
                SymbolicTransition transition = transitions.get(stateIndex).get(symbolIndex);
                for (StateProb stateProb : transition.getStateProbSet()) {

                    predecessorMap.computeIfAbsent(stateProb.getStateIndex(), k -> new HashMap<>());

                    if (predecessorMap.get(stateProb.getStateIndex()).get(symbolIndex) == null) {
                        Aprational[] defaultProbs = new Aprational[pfa1.getNumStates()];
                        Arrays.fill(defaultProbs, Aprational.ZERO);
                        predecessorMap.get(stateProb.getStateIndex()).put(symbolIndex, defaultProbs);
                    }

                    Aprational[] probs = predecessorMap.get(stateProb.getStateIndex()).get(symbolIndex);
                    probs[stateIndex] = stateProb.getProb().add(probs[stateIndex]);
                }
            }
        }
    }

    public boolean constraintsPass() {
        for(Constraint constraint : constraints) {
            if(!constraint.isValid()) {
                return false;
            }
        }
        return true;
    }

    public void updateConstraints() {

        Set<Constraint> newConstraints = new HashSet<>();

        for (Constraint constraint : lastConstraints) {

            for (int symbolIndex = 0; symbolIndex < pfa1.getNumSymbols(); symbolIndex++) {

                Constraint newConstraint = new Constraint(
                        computeVarList(constraint, symbolIndex, true),
                        computeVarList(constraint, symbolIndex, false), Operation.EQ);

                newConstraints.add(newConstraint);
            }
        }

        lastConstraints = newConstraints;
        constraints.addAll(newConstraints);

        computeIndependentConstraints();
    }

    private VarList computeVarList(Constraint constraint, int symbolIndex, boolean left) {

        int numVars;
        Aprational[] newProbs;
        Aprational[] varConsts;
        Map<Integer, Map<Integer, Aprational[]>> predecessorMap;

        if (left) {
            numVars = numLeftVars;
            newProbs = new Aprational[pfa1.getNumStates()];
            varConsts = constraint.getLeftList().getConstList();
            predecessorMap = predecessorMap1;
        } else {
            numVars = numRightVars;
            newProbs = new Aprational[pfa2.getNumStates()];
            varConsts = constraint.getRightList().getConstList();
            predecessorMap = predecessorMap2;
        }

        Arrays.fill(newProbs, Aprational.ZERO);

        for (int varIndex = 0; varIndex < numVars; varIndex++) {
            if (!varConsts[varIndex].equals(Aprational.ZERO)) {

                if (predecessorMap.get(varIndex) != null) {

                    if (predecessorMap.get(varIndex).get(symbolIndex) != null) {

                        Aprational[] predArray = predecessorMap.get(varIndex).get(symbolIndex);

                        ApUtils.vectorVectorAdd(newProbs,
                                ApUtils.deepVectorScalarMult(predArray, varConsts[varIndex]));
                    }
                }
            }
        }

        if (left) {
            return new VarList(newProbs, "x", true);
        } else {
            return new VarList(newProbs, "y", false);
        }
    }

    private void computeIndependentConstraints() {

    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof PfaEquivLp)) {
            return false;
        }

        if (!pfa1.equals(((PfaEquivLp) o).pfa1)) return false;
        if (!pfa2.equals(((PfaEquivLp) o).pfa2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pfa1, pfa2);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pfa1", pfa1)
                .add("pfa2", pfa2)
                .toString();
    }
}