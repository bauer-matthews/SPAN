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

    public void removeHead() {
        atomicProcesses.remove(0);
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
