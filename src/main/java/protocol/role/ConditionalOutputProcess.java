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
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/10/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ConditionalOutputProcess implements AtomicProcess {

    private final Collection<Guard> guards;
    private final Collection<ProbOutput> ifProbOutputs;
    private final Collection<ProbOutput> thenProbOutputs;
    private final int phase;

    ConditionalOutputProcess(Collection<Guard> guards, Collection<ProbOutput> ifProbOutputs,
                             Collection<ProbOutput> thenProbOutputs, int phase) {

        Objects.requireNonNull(guards);
        Objects.requireNonNull(ifProbOutputs);
        Objects.requireNonNull(thenProbOutputs);

        this.guards = guards;
        this.ifProbOutputs = ifProbOutputs;
        this.thenProbOutputs = thenProbOutputs;
        this.phase = phase;

        Aprational ifSum = Aprational.ZERO;
        for (ProbOutput pout : ifProbOutputs) {
            ifSum = ifSum.add(pout.getProbability());
        }

        Aprational thenSum = Aprational.ZERO;
        for (ProbOutput pout : thenProbOutputs) {
            thenSum = thenSum.add(pout.getProbability());
        }

        if ((!ifSum.equals(Aprational.ONE)) || (!thenSum.equals(Aprational.ONE))) {
            throw new IllegalArgumentException(Resources.INVALID_PROBS);
        }
    }

    public Collection<Guard> getGuards() {
        return guards;
    }

    public Collection<ProbOutput> getIfProbOutputs() {
        return ifProbOutputs;
    }

    public Collection<ProbOutput> getThenProbOutputs() {
        return thenProbOutputs;
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

        for (ProbOutput output : ifProbOutputs) {
            addProbOutputVariables(output, variableTerms);
        }

        for (ProbOutput output : thenProbOutputs) {
            addProbOutputVariables(output, variableTerms);
        }

        return variableTerms;
    }

    private void addProbOutputVariables(ProbOutput output, Collection<VariableTerm> variableTerms) {

        for (Term term : output.getOutputTerms()) {
            variableTerms.addAll(term.getVariables());
            variableTerms.addAll(term.getVariables());
        }

        for (AtomicProcess atomicProcess : output.getSubrole().getAtomicProcesses()) {
            variableTerms.addAll(atomicProcess.getVariables());
        }
    }

    @Override
    public AtomicProcess appendBranchIndexToVars(int index) {

        Collection<Guard> newGuards = new ArrayList<>();
        for (Guard guard : guards) {

            newGuards.add(new Guard(new Equality(guard.getEquality().getLhs().appendBranchIndexToVars(index),
                    guard.getEquality().getRhs().appendBranchIndexToVars(index)), guard.isPositiveTest()));
        }

        Collection<ProbOutput> newIfProbOutputs = new ArrayList<>();
        for (ProbOutput output : ifProbOutputs) {
            appendBranchIndexProbOutput(newIfProbOutputs, output, index);
        }

        Collection<ProbOutput> newThenProbOutputs = new ArrayList<>();
        for (ProbOutput output : thenProbOutputs) {
            appendBranchIndexProbOutput(newThenProbOutputs, output, index);
        }

        return new ConditionalOutputProcess(newGuards, newIfProbOutputs, newThenProbOutputs, phase);
    }

    public boolean checkGuards(List<Equality> substitution) throws ExecutionException {

        boolean guardPassed = true;

        for (Guard guard : guards) {
            if (!guard.check(substitution)) {
                guardPassed = false;
                break;
            }
        }
        return guardPassed;
    }

    private void appendBranchIndexProbOutput(Collection<ProbOutput> probOutputs, ProbOutput output, int index) {

        List<Term> newOutputTerms = new ArrayList<>();
        for (Term term : output.getOutputTerms()) {
            newOutputTerms.add(term.appendBranchIndexToVars(index));
        }

        if (output.getSubrole().getAtomicProcesses().isEmpty()) {
            probOutputs.add(new ProbOutput(output.getProbability(), newOutputTerms, output.getSubrole()));
        } else {
            probOutputs.add(new ProbOutput(output.getProbability(), newOutputTerms,
                    output.getSubrole().appendBranchIndexToVars(GlobalDataCache.getFreshBranchIndex())));
        }
    }

    @Override
    public boolean isConditionalOutput() {
        return true;
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

        if (!(o instanceof ConditionalOutputProcess)) {
            return false;
        }

        if (!guards.equals(((ConditionalOutputProcess) o).guards)) return false;
        if (!ifProbOutputs.equals(((ConditionalOutputProcess) o).ifProbOutputs)) return false;
        if (!thenProbOutputs.equals(((ConditionalOutputProcess) o).thenProbOutputs)) return false;
        if (phase != (((ConditionalOutputProcess) o).phase)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guards, ifProbOutputs, thenProbOutputs, phase);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("guards", guards.toString())
                .add("if probabilistic outputs", ifProbOutputs.toString())
                .add("then probabilistic outputs", thenProbOutputs.toString())
                .add("phase", phase)
                .toString();
    }
}
