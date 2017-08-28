package process;

import com.google.common.base.MoreObjects;
import rewriting.terms.FrameVariableTerm;
import rewriting.terms.Sort;
import rewriting.terms.Term;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/28/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class RecipeParameter {

    private List<FrameVariableTerm> frameVariables;
    private Map<FrameVariableTerm, Term> frameValues;
    private Sort sort;
    private int depth;
    private Optional<Term> guard;

    RecipeParameter(List<FrameVariableTerm> frameVariables, Map<FrameVariableTerm, Term> frameValues,
                    Sort sort, int depth, Optional<Term> guard) {

        Objects.requireNonNull(frameVariables);
        Objects.requireNonNull(frameValues);
        Objects.requireNonNull(sort);
        Objects.requireNonNull(guard);

        this.frameVariables = frameVariables;
        this.frameValues = frameValues;
        this.sort = sort;
        this.depth = depth;
        this.guard = guard;
    }

    public List<FrameVariableTerm> getFrameVariables() {
        return frameVariables;
    }

    public Map<FrameVariableTerm, Term> getFrameValues() {
        return frameValues;
    }

    public Optional<Term> getGuard() {
        return guard;
    }

    public Sort getSort() {
        return sort;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof RecipeParameter)) {
            return false;
        }

        if (!frameValues.equals(((RecipeParameter) o).frameValues)) return false;
        if (!frameVariables.equals(((RecipeParameter) o).frameVariables)) return false;
        if (!guard.equals(((RecipeParameter) o).guard)) return false;
        if (!sort.equals(((RecipeParameter) o).sort)) return false;
        if (depth != (((RecipeParameter) o).depth)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frameValues, frameVariables, guard, sort, depth);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("frame values", frameValues.toString())
                .add("frame variables", frameVariables.toString())
                .add("guard", guard.toString())
                .add("sort", sort.toString())
                .add("depth", depth)
                .toString();
    }
}
