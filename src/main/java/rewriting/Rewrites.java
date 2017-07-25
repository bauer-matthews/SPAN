package rewriting;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/23/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Rewrites {

    private final Collection<Rewrite> rewrites = new ArrayList<>();

    public Rewrites(Collection<Rewrite> rewrites) {

        Objects.requireNonNull(rewrites);

        this.rewrites.addAll(rewrites);
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof Rewrites)) {
            return false;
        }

        return rewrites.equals(((Rewrites) o).rewrites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rewrites);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rewrites", rewrites)
                .toString();
    }

}
