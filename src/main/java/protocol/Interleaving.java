package protocol;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;
import process.Action;

import java.util.List;
import java.util.Objects;

/**
 * Created by mbauer on 8/21/2017.
 */
public class Interleaving {

    private final List<Action> interleaving;
    private final Apfloat attackProb;

    public Interleaving(List<Action> interleaving, Apfloat attackProb) {

        Objects.requireNonNull(interleaving);
        Objects.requireNonNull(attackProb);

        this.interleaving = interleaving;
        this.attackProb = attackProb;
    }

    public List<Action> getInterleaving() {
        return interleaving;
    }

    public Apfloat getAttackProb() {
        return attackProb;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Interleaving)) return false;
        if (!(this.interleaving.equals(((Interleaving) o).interleaving))) return false;
        if (!(this.attackProb.equals(((Interleaving) o).attackProb))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(interleaving, attackProb);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("interleaving", interleaving)
                .add("attack prob", attackProb.toString(true))
                .toString();
    }
}
