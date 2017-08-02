package process;

import cache.GlobalDataCache;
import com.google.common.base.MoreObjects;
import protocol.role.OutputProcess;
import protocol.role.Role;
import rewriting.Equality;
import rewriting.terms.FrameVariableTerm;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.*;

/**
 * Created by matt on 7/26/17.
 */
public class State {


    private final Map<VariableTerm, Term> substitution;
    private final Map<FrameVariableTerm, Term> frame;
    private final List<Role> roles;

    State(Map<VariableTerm, Term> substitution, Map<FrameVariableTerm, Term> frame, List<Role> roles) {

        Objects.requireNonNull(substitution);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(roles);

        this.substitution = substitution;
        this.frame = frame;
        this.roles = roles;
    }

    public List<Action> getEnabledActions() {

        List<Action> actions = new ArrayList<>();

        for (int i = 0; i < roles.size(); i++) {

            if (roles.get(i).getAtomicProcesses().get(0) instanceof OutputProcess) {

                // TODO check that the guard is satisfied (apply substitution, reduce and check for equality)

                Collection<Equality> guards = ((OutputProcess) roles.get(i).getAtomicProcesses().get(0)).getGuards();
                actions.add(new Action(null, i));

            } else {
                for (Term recipe : GlobalDataCache.getRecipes(frame.size())) {
                    actions.add(new Action(recipe, i));
                }
            }
        }

        return actions;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof State)) {
            return false;
        }

        if (!substitution.equals(((State) o).substitution)) return false;
        if (!frame.equals(((State) o).frame)) return false;
        if (!roles.equals(((State) o).roles)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(substitution, frame, roles);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("substitution", substitution.toString())
                .add("frame", frame.toString())
                .add("roles", roles.toString())
                .toString();
    }
}
