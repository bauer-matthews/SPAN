package protocol.role;

import cache.GlobalDataCache;
import com.google.common.base.MoreObjects;
import org.apfloat.Aprational;
import rewriting.Equality;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class OutputProcess implements AtomicProcess {

    private final Collection<Guard> guards;
    private final Collection<ProbOutput> probOutputs;
    private final int phase;

    OutputProcess(Collection<Guard> guards, Collection<ProbOutput> probOutputs, int phase) {

        Objects.requireNonNull(guards);
        Objects.requireNonNull(probOutputs);

        this.guards = guards;
        this.probOutputs = probOutputs;
        this.phase = phase;

        Aprational sum = Aprational.ZERO;
        for (ProbOutput pout : probOutputs) {
            sum = sum.add(pout.getProbability());
        }

        if (!sum.equals(Aprational.ONE)) {
            throw new IllegalArgumentException(Resources.INVALID_PROBS);
        }
    }

    public Collection<Guard> getGuards() {
        return guards;
    }


    public Collection<ProbOutput> getProbOutputs() {
        return probOutputs;
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public Collection<VariableTerm> getVariables() {

        Collection<VariableTerm> variableTerms = new ArrayList<>();

        for (Guard guard : guards) {
            variableTerms.addAll(guard.getEquality().getLhs().getVariables());
            variableTerms.addAll(guard.getEquality().getRhs().getVariables());
        }

        for (ProbOutput output : probOutputs) {
            for (Term term : output.getOutputTerms()) {
                variableTerms.addAll(term.getVariables());
                variableTerms.addAll(term.getVariables());
            }

            for (AtomicProcess atomicProcess : output.getSubrole().getAtomicProcesses()) {
                variableTerms.addAll(atomicProcess.getVariables());
            }
        }

        return variableTerms;
    }

    @Override
    public AtomicProcess appendBranchIndexToVars(int index) {

        Collection<Guard> newGuards = new ArrayList<>();
        for (Guard guard : guards) {

            newGuards.add(new Guard(new Equality(guard.getEquality().getLhs().appendBranchIndexToVars(index),
                    guard.getEquality().getRhs().appendBranchIndexToVars(index)), guard.isPositiveTest()));
        }

        Collection<ProbOutput> newProbOutputs = new ArrayList<>();
        for (ProbOutput output : probOutputs) {

            List<Term> newOutputTerms = new ArrayList<>();
            for (Term term : output.getOutputTerms()) {
                newOutputTerms.add(term.appendBranchIndexToVars(index));
            }

            if (output.getSubrole().getAtomicProcesses().isEmpty()) {
                newProbOutputs.add(new ProbOutput(output.getProbability(), newOutputTerms, output.getSubrole()));
            } else {
                newProbOutputs.add(new ProbOutput(output.getProbability(), newOutputTerms,
                        output.getSubrole().appendBranchIndexToVars(GlobalDataCache.getFreshBranchIndex())));
            }
        }

        return new OutputProcess(newGuards, newProbOutputs, phase);
    }

    @Override
    public boolean isOutput() {
        return true;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof OutputProcess)) {
            return false;
        }

        if (!guards.equals(((OutputProcess) o).guards)) return false;
        if (!probOutputs.equals(((OutputProcess) o).probOutputs)) return false;
        if (phase != (((OutputProcess) o).phase)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guards, probOutputs, phase);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("guards", guards.toString())
                .add("probabilistic outputs", probOutputs.toString())
                .add("phase", phase)
                .toString();
    }
}
