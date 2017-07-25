package protocol;

import com.google.common.base.MoreObjects;
import rewriting.terms.VariableTerm;

import java.util.Collection;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SafetyProperty {

    private final Collection<VariableTerm> secrets;

    public SafetyProperty(Collection secrets) {

        Objects.requireNonNull(secrets);

        this.secrets = secrets;
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof SafetyProperty)) {
            return false;
        }

        return secrets.equals(((SafetyProperty) o).secrets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secrets);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("secrets", secrets)
                .toString();
    }

}
