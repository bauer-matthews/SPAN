package protocol;

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
    private final int recipeSize;

    public Metadata(String version, int recipeSize) {

        Objects.requireNonNull(version);

        this.version = version;
        this.recipeSize = recipeSize;
    }

    String getVersion() {
        return this.version;
    }

    public int getRecipeSize() {
        return recipeSize;
    }

    @Override
    public boolean equals(Object o) {

        if (! (o instanceof Metadata)) {
            return false;
        }

        if(!(recipeSize == ((Metadata) o).recipeSize)) return false;

        return equal(version, ((Metadata) o).getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, recipeSize);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("recipe size", recipeSize)
                .toString();
    }
}
