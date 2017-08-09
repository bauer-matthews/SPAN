package process;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by mbauer on 8/8/2017.
 */
public class EquivalenceCheckResult {

    private final boolean equivalent;
    private final boolean phi1Attack;
    private final boolean phi2attack;

    EquivalenceCheckResult(boolean equivalent, boolean phi1Attack, boolean phi2attack) {

        this.equivalent = equivalent;
        this.phi1Attack = phi1Attack;
        this.phi2attack = phi2attack;
    }

    public boolean isEquivalent() {
        return equivalent;
    }

    public boolean isPhi1Attack() {
        return phi1Attack;
    }

    public boolean isPhi2attack() {
        return phi2attack;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof EquivalenceCheckResult)) {
            return false;
        }

        if (equivalent != (((EquivalenceCheckResult) o).equivalent)) return false;
        if (phi1Attack != (((EquivalenceCheckResult) o).phi1Attack)) return false;
        if (phi2attack != (((EquivalenceCheckResult) o).phi2attack)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equivalent, phi1Attack, phi2attack);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("equivalent", equivalent)
                .add("frame 1 attack", phi1Attack)
                .add("frame 2 attack", phi2attack)
                .toString();
    }
}
