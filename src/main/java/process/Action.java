package process;

import com.google.common.base.MoreObjects;
import rewriting.terms.Term;

import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/26/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Action {

    private final Term recipe;
    private final int roleIndex;

    Action(Term recipe, int roleIndex) {

        this.recipe = recipe;
        this.roleIndex = roleIndex;
    }

    @Override
    public boolean equals(Object o) {

        if (! (o instanceof Action)) {
            return false;
        }

        if(!recipe.equals(((Action) o).recipe)) return false;
        if(roleIndex != ((Action) o).roleIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, roleIndex);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("recipe", recipe)
                .add("role index", roleIndex)
                .toString();
    }
}
