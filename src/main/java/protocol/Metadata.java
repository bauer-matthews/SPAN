package protocol;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import parser.protocol.Statement;

import java.util.Objects;

import static com.google.common.base.Objects.equal;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Metadata {

    private final String version;
    private int recipeDepth;

    public Metadata(String version, int recipeDepth) {

        Objects.requireNonNull(version);

        this.version = version;
        this.recipeDepth = recipeDepth;
    }

    public String getVersion() {
        return this.version;
    }

    public int getRecipeDepth() {
        return recipeDepth;
    }

    @VisibleForTesting
    public void setRecipeDepth(int recipeDepth) {
        this.recipeDepth = recipeDepth;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Metadata)) {
            return false;
        }

        if (!(recipeDepth == ((Metadata) o).recipeDepth)) return false;

        return equal(version, ((Metadata) o).getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, recipeDepth);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("recipe size", recipeDepth)
                .toString();
    }
}
