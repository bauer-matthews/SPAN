package process;

import protocol.role.Role;
import rewriting.terms.FrameVariableTerm;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by matt on 7/26/17.
 */
public class State {


    private final Map<VariableTerm, Term> substitution;
    private final Map<FrameVariableTerm, Term> frame;
    private final Collection<Role> roles;

    State(Map<VariableTerm, Term> substitution, Map<FrameVariableTerm, Term> frame, Collection<Role> roles) {

        Objects.requireNonNull(substitution);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(roles);

        this.substitution = substitution;
        this.frame = frame;
        this.roles = roles;
    }

    // TODO hash, tostring equals

}
