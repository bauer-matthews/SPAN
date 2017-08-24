package process;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/8/2017.
 */
public class BeliefState {

    private final List<Belief> beliefs;
    private final List<Action> actionHistory;

    public BeliefState(List<Belief> beliefs, List<Action> actionHistory) {

        Objects.requireNonNull(beliefs);

        this.beliefs = beliefs;
        this.actionHistory = actionHistory;
    }

    public List<Belief> getBeliefs() {
        return beliefs;
    }

    public List<Action> getActionHistory() {
        return actionHistory;
    }

    public Observation getObservation() throws ExecutionException {
        return beliefs.get(0).getState().getObservation();
    }

    public Apfloat getStateProb(State state) {
        for (Belief belief : beliefs) {
            if (belief.getState().equals(state)) {
                return belief.getProb();
            }
        }

        return Apfloat.ZERO;
    }

    public Apfloat getStateAttackProb() {

        Apfloat prob = Apfloat.ZERO;

        for (Belief belief : beliefs) {
            if (belief.getState().isAttackState()) {
                prob = prob.add(belief.getProb());
            }
        }

        return prob;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof BeliefState)) {
            return false;
        }

        if (!beliefs.equals(((BeliefState) o).beliefs)) return false;
        if (!actionHistory.equals(((BeliefState) o).actionHistory)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beliefs, actionHistory);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("beliefs", beliefs.toString())
                .add("action history", actionHistory)
                .toString();
    }
}
