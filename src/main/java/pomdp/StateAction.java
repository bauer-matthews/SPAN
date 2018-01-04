package pomdp;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/4/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class StateAction {

    private final long stateIndex;
    private final long actionIndex;

    public StateAction(long stateIndex, long actionIndex) {

        this.stateIndex = stateIndex;
        this.actionIndex = actionIndex;
    }

    public long getActionIndex() {
        return actionIndex;
    }

    public long getStateIndex() {
        return stateIndex;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof StateAction)) {
            return false;
        }

        if (stateIndex != ((StateAction) o).stateIndex) return false;
        if (actionIndex != ((StateAction) o).actionIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateIndex, actionIndex);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("state index", stateIndex)
                .add("action index", actionIndex)
                .toString();
    }
}
