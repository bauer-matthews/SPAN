package protocol.role;

import com.google.common.base.MoreObjects;

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

    private final List<AtomicProcess> atomicProcess;

    public Role(List<AtomicProcess> atomicProcess) {
        Objects.requireNonNull(atomicProcess);
        this.atomicProcess = atomicProcess;
    }

    public List<AtomicProcess> getAtomicProcesses() {
        return atomicProcess;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Role)) {
            return false;
        }

        return equal(atomicProcess, ((Role) o).atomicProcess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicProcess);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("actions", atomicProcess.toString())
                .toString();
    }
}
