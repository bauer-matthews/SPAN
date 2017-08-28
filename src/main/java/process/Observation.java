package process;

import cache.RewritingCache;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import protocol.role.RoleView;
import rewriting.Equality;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/21/2017.
 */
public class Observation {

    private static final Joiner COMMA_JOINER;

    static {
        COMMA_JOINER = Joiner.on(", \n");
    }

    private final List<RoleView> roleViews;
    private final List<Equality> frame;

    Observation(List<RoleView> roleViews, List<Equality> frame) {

        Objects.requireNonNull(roleViews);
        Objects.requireNonNull(frame);

        this.roleViews = roleViews;
        this.frame = frame;
    }

    public String toMathString() throws ExecutionException {

        StringBuilder sb = new StringBuilder();

        sb.append("Views{");

        List<String> roleViewStrings = new ArrayList<>();
        for (RoleView view : roleViews) {
            roleViewStrings.add(view.toMathString());
        }

        sb.append(COMMA_JOINER.join(roleViewStrings));
        sb.append("} ");

        List<String> frameValues = new ArrayList<>();
        for (Equality equality : frame) {
            frameValues.add(equality.getLhs().toMathString() + "=" +
                    RewritingCache.reduce(equality.getRhs()).toMathString());
        }

        sb.append("\n\nFrame{");
        sb.append(COMMA_JOINER.join(frameValues));
        sb.append("}");

        return sb.toString();
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
