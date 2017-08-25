package protocol.role;

import com.google.common.base.MoreObjects;

import java.util.Objects;


/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/16/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class RoleView {

    public enum Status {
        INPUT, OUTPUT, NONE
    }

    private final int roleIndex;
    private final Status roleStatus;

    public RoleView(int roleIndex, Status roleStatus) {

        this.roleIndex = roleIndex;
        this.roleStatus = roleStatus;
    }

    public Status getRoleStatus() {
        return roleStatus;
    }

    public int getRoleIndex() {
        return roleIndex;
    }

    public String toMathString() {
        return roleIndex + "-" + roleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoleView)) {
            return false;
        }

        if(roleIndex != ((RoleView) o).roleIndex) return  false;
        if(!(roleStatus.equals(((RoleView) o).roleStatus))) return  false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleIndex, roleStatus);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("role", roleIndex)
                .add("status", roleStatus)
                .toString();
    }
}
