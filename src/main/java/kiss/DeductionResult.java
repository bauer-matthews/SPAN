package kiss;

import com.google.common.base.MoreObjects;
import org.apache.commons.cli.Option;
import rewriting.Rewrite;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.Objects;
import java.util.Optional;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/25/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class DeductionResult {

    private final Optional<String> recipe;
    private final String term;
    private final String frame;
    private final boolean isDeducible;

    public DeductionResult(String term, boolean isDeducible, String frame, Optional<String> recipe) {

        Objects.requireNonNull(term);
        Objects.requireNonNull(recipe);
        Objects.requireNonNull(frame);

        this.term = term;
        this.isDeducible = isDeducible;
        this.frame = frame;
        this.recipe = recipe;
    }

    public String getTerm() {
        return term;
    }

    public boolean isDeducible() {
        return isDeducible;
    }

    public Optional<String> getRecipe() {
        return recipe;
    }

    public String getFrame() {
        return frame;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof DeductionResult)) {
            return false;
        }

        if (!this.term.equals(((DeductionResult) o).term)) return false;
        if (!this.recipe.equals(((DeductionResult) o).recipe)) return false;
        if (!this.frame.equals(((DeductionResult) o).frame)) return false;
        if (!this.isDeducible == ((DeductionResult) o).isDeducible) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, recipe, isDeducible, frame);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("term", term.toString())
                .add("deducible", isDeducible)
                .add("frame", frame)
                .add("recipe", recipe.toString())
                .toString();
    }
}
