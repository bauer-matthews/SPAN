package process;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import com.google.common.base.MoreObjects;
import protocol.role.OutputProcess;
import protocol.role.Role;
import rewriting.Equality;
import rewriting.RewriteEngine;
import rewriting.terms.FrameVariableTerm;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;
import util.rewrite.RewriteUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by matt on 7/26/17.
 */
public class State {


    private final List<Equality> substitution;
    private final List<Equality> frame;
    private final List<Role> roles;
    private boolean attackState;

    public State(List<Equality> substitution, List<Equality> frame, List<Role> roles) {

        Objects.requireNonNull(substitution);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(roles);

        this.substitution = substitution;
        this.frame = frame;
        this.roles = roles;
        attackState = false;
    }

    public List<Action> getEnabledActions() throws ExecutionException {

        List<Action> actions = new ArrayList<>();

        for (int i = 0; i < roles.size(); i++) {

            if (!roles.get(i).getAtomicProcesses().isEmpty()) {

                if (roles.get(i).getAtomicProcesses().get(0) instanceof OutputProcess) {

                    Collection<Equality> guards = ((OutputProcess) roles.get(i).getAtomicProcesses().get(0)).getGuards();
                    boolean guardPassed = true;

                    for (Equality equality : guards) {

                        Equality groundGuard = RewriteUtils.applySubstitution(equality, substitution);

                        Term lhsNormalForm = RewriteEngine.reduce(groundGuard.getLhs(),
                                GlobalDataCache.getProtocol().getRewrites());

                        Term rhsNormalForm = RewriteEngine.reduce(groundGuard.getRhs(),
                                GlobalDataCache.getProtocol().getRewrites());

                        if (!lhsNormalForm.equals(rhsNormalForm)) {

                            if(RunConfiguration.getDebug()) {
                                System.out.println("GUARD TEST FAILED: " + lhsNormalForm.toMathString() + " = " +
                                        rhsNormalForm.toMathString());
                                System.out.println();
                            }

                            guardPassed = false;
                        }
                    }

                    if (guardPassed) {
                        actions.add(new Action(Resources.TAU_ACTION, i));
                    }

                } else {
                    for (Term recipe : GlobalDataCache.getRecipes(frame.size())) {
                        actions.add(new Action(recipe, i));
                    }
                }
            }
        }

        if(actions.size() == 1) {
            int i;
        }

        return actions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Equality> getFrame() {
        return frame;
    }

    public List<Equality> getSubstitution() {
        return substitution;
    }

    public boolean isAttackState() {
        return this.attackState;
    }

    void setAttackState(boolean attackState) {
        this.attackState = attackState;
    }

    State outputTerms(List<Term> terms, int roleIndex) {

        List<Role> newRoles = removeHead(roleIndex);

        List<Equality> newFrame = new ArrayList<>();
        for(Equality equality : frame) {
            newFrame.add(equality);
        }

        int index = newFrame.size();
        for (Term term : terms) {
            newFrame.add(new Equality(new FrameVariableTerm("W", index),
                    RewriteUtils.applySubstitution(term, substitution)));
            index++;
        }

        return new State(substitution, newFrame, newRoles);
    }

    State inputTerm(VariableTerm variable, Term value, int roleIndex) {

        List<Role> newRoles = removeHead(roleIndex);

        List<Equality> newSubstitution = new ArrayList<>();
        for(Equality equality : substitution) {
            newSubstitution.add(equality);
        }

        newSubstitution.add(new Equality(variable, RewriteUtils.applySubstitution(value, frame)));

        return new State(newSubstitution, frame, newRoles);
    }

    private List<Role> removeHead(int roleIndex) {

        ArrayList<Role> newRoles = new ArrayList<>();
        for(int i=0; i < roles.size(); i++) {
            if(i == roleIndex) {
                newRoles.add(roles.get(i).removeHead());
            } else {
                newRoles.add(roles.get(i));
            }
        }

        return newRoles;
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
