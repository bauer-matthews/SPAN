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

    private final List<Action> actionList;
    private final Apfloat attackProb;

    public Interleaving(List<Action> actionList, Apfloat attackProb) {

        Objects.requireNonNull(actionList);
        Objects.requireNonNull(attackProb);

        this.actionList = actionList;
        this.attackProb = attackProb;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public Apfloat getAttackProb() {
        return attackProb;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Interleaving)) return false;
        if (!(this.actionList.equals(((Interleaving) o).actionList))) return false;
        if (!(this.attackProb.equals(((Interleaving) o).attackProb))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionList, attackProb);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("action list", actionList)
                .add("attack prob", attackProb.toString(true))
                .toString();
    }
}
