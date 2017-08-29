package protocol.role;

import com.google.common.base.MoreObjects;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Objects.equal;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Role {

    private final List<AtomicProcess> atomicProcesses;

    public Role(List<AtomicProcess> atomicProcesses) {
        Objects.requireNonNull(atomicProcesses);
        this.atomicProcesses = atomicProcesses;
    }

    public List<AtomicProcess> getAtomicProcesses() {
        return atomicProcesses;
    }

    public AtomicProcess getHead() {
        return atomicProcesses.get(0);
    }

    public Role removeHead() {

        if (atomicProcesses.isEmpty()) {
            throw new UnsupportedOperationException("Cannot remove head of empty Role.");
        }

        List<AtomicProcess> newProcesses = new ArrayList<>();

        for (AtomicProcess process : atomicProcesses) {

            // NOTE: AtomicProcess is immutable
            newProcesses.add(process);
        }

        newProcesses.remove(0);
        return new Role(newProcesses);
    }

    public List<VariableTerm> getOutputVariables() {

        List<VariableTerm> variableTerms = new ArrayList<>();
        for (AtomicProcess process : atomicProcesses) {
            if (process.isOutput()) {
                variableTerms.addAll(process.getVariables());
            }
        }
        return variableTerms;
    }

    public List<VariableTerm> getInputVariables() {

        List<VariableTerm> variableTerms = new ArrayList<>();
        for (AtomicProcess process : atomicProcesses) {

            if (process.isInput()) {
                variableTerms.addAll(process.getVariables());
            }
        }
        return variableTerms;
    }

    public Collection<VariableTerm> getVariables() {

        List<VariableTerm> variableTerms = new ArrayList<>();
        for (AtomicProcess process : atomicProcesses) {
            variableTerms.addAll(process.getVariables());
        }
        return variableTerms;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Role)) {
            return false;
        }

        return equal(atomicProcesses, ((Role) o).atomicProcesses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicProcesses);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("atomic processes", atomicProcesses.toString())
                .toString();
    }
}
