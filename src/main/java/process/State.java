package process;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import cache.SubstitutionCache;
import com.google.common.base.MoreObjects;
import protocol.role.*;
import rewriting.Equality;
import rewriting.terms.*;

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

    public Observation getObservation() throws ExecutionException {
        return new Observation(getRoleViews(), frame);
    }

    List<RoleView> getRoleViews() throws ExecutionException {

        List<RoleView> roleViews = new ArrayList<>();

        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getAtomicProcesses().isEmpty()) {
                roleViews.add(new RoleView(i, RoleView.Status.NONE));
            } else if (roles.get(i).getAtomicProcesses().get(0) instanceof OutputProcess) {
                if (checkGuards(((OutputProcess) roles.get(i).getAtomicProcesses().get(0)).getGuards())) {
                    roleViews.add(new RoleView(i, RoleView.Status.OUTPUT));
                } else {
                    roleViews.add(new RoleView(i, RoleView.Status.NONE));
                }
            } else {
                roleViews.add(new RoleView(i, RoleView.Status.INPUT));
            }
        }

        return roleViews;
    }

    private int getMinPhase() {

        int minPhase = Integer.MAX_VALUE;

        for (Role role : roles) {

            if (!role.getAtomicProcesses().isEmpty()) {

                if (role.getHead().getPhase() < minPhase) {
                    minPhase = role.getHead().getPhase();
                }
            }
        }

        return minPhase;
    }

    public List<Action> getEnabledActions() throws ExecutionException {

        int minPhase = getMinPhase();
        List<Action> actions = new ArrayList<>();

        for (int i = 0; i < roles.size(); i++) {

            if (!roles.get(i).getAtomicProcesses().isEmpty()) {

                if (roles.get(i).getHead().getPhase() == minPhase) {

                    if (roles.get(i).getAtomicProcesses().get(0) instanceof OutputProcess) {

                        if (checkGuards(((OutputProcess) roles.get(i).getAtomicProcesses().get(0)).getGuards())) {
                            actions.add(new Action(Resources.TAU_ACTION, i));
                        }

                    } else {

                        Optional<Term> guard = ((InputProcess) roles.get(i).getAtomicProcesses().get(0)).getInputGuard();

                        // TODO: move this code to action factory
                        List<FrameVariableTerm> frameVariableTerms = new ArrayList<>();
                        Map<FrameVariableTerm, Term> frameVariableTermMap = new HashMap<>();
                        for (Equality equality : frame) {
                            // TODO: Tighten type
                            frameVariableTerms.add((FrameVariableTerm) equality.getLhs());
                            frameVariableTermMap.put((FrameVariableTerm) equality.getLhs(),
                                    SubstitutionCache.applySubstitution(equality.getRhs(), substitution));
                        }

                        Sort guardSort = SortFactory.KIND;
                        if (guard.isPresent()) {
                            guardSort = guard.get().getSort();
                        }


                        for (Term recipe : ActionFactory2.getAllRecipes(frameVariableTerms, frameVariableTermMap,
                                guardSort, GlobalDataCache.getProtocol().getMetadata().getRecipeDepth(), guard)) {

                            actions.add(new Action(recipe, i));
                        }

                        // OLD ACTION FACTORY CODE
//                        if (guard.isPresent()) {
//
//                            for (Term recipe : ActionFactory.getRecipesWithGuard(frame.size(), guard.get(), frame)) {
//                                actions.add(new Action(recipe, i));
//                            }
//
//
//
//                        } else {
//                            for (Term recipe : GlobalDataCache.getRecipes(frame.size())) {
//                                actions.add(new Action(recipe, i));
//                            }
//                        }
                    }
                }
            }
        }

        if (RunConfiguration.getTrace()) {
            System.out.println("FILTERED ACTIONS: " + actions.size());
            for (Action action : actions) {
                System.out.println(action.getRecipe().toMathString());
            }
            System.out.println();
        }

        return actions;
    }

    private boolean checkGuards(Collection<Guard> guards) throws ExecutionException {

        boolean guardPassed = true;

        for (Guard guard : guards) {
            if (!guard.check(substitution)) {
                guardPassed = false;
                break;
            }
        }
        return guardPassed;
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

    State outputTerms(List<Term> terms, int roleIndex) throws ExecutionException {

        List<Role> newRoles = removeHead(roleIndex);

        List<Equality> newFrame = new ArrayList<>();
        for (Equality equality : frame) {
            newFrame.add(equality);
        }

        int index = newFrame.size();
        for (Term term : terms) {
            newFrame.add(new Equality(new FrameVariableTerm("W", index),
                    SubstitutionCache.applySubstitution(term, substitution)));
            index++;
        }

        return new State(substitution, newFrame, newRoles);
    }

    State inputTerm(VariableTerm variable, Term value, int roleIndex) throws ExecutionException {

        List<Role> newRoles = removeHead(roleIndex);

        List<Equality> newSubstitution = new ArrayList<>();
        for (Equality equality : substitution) {
            newSubstitution.add(equality);
        }

        newSubstitution.add(new Equality(variable, SubstitutionCache.applySubstitution(value, frame)));

        return new State(newSubstitution, frame, newRoles);
    }

    private List<Role> removeHead(int roleIndex) {

        ArrayList<Role> newRoles = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            if (i == roleIndex) {
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
