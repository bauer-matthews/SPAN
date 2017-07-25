package protocol.role;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Objects.equal;

/**
 * SPAN - Stochastic Protocol Analyzer
 *
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Role {

    List<Action> actions;

    public Role(List<Action> actions)
    {
        Objects.requireNonNull(actions);
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {

        if (! (o instanceof Role)) {
            return false;
        }

        return equal(actions, ((Role) o).actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("actions", actions.toString())
                .toString();
    }
}
