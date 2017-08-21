package process;

import com.google.common.base.MoreObjects;
import protocol.role.RoleView;
import rewriting.Equality;

import java.util.List;
import java.util.Objects;

/**
 * Created by mbauer on 8/21/2017.
 */
public class Observation {

    private final List<RoleView> roleViews;
    private final List<Equality> frame;

    Observation(List<RoleView> roleViews, List<Equality> frame) {

        Objects.requireNonNull(roleViews);
        Objects.requireNonNull(frame);

        this.roleViews = roleViews;
        this.frame = frame;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Observation)) {
            return false;
        }

        if (!roleViews.equals(((Observation) o).roleViews)) return false;
        if (!frame.equals(((Observation) o).frame)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleViews, frame);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("role views", roleViews.toString())
                .add("frame", frame.toString())
                .toString();
    }
}
