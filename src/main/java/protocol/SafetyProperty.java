package protocol;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;
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
    private final Apfloat probability;

    public SafetyProperty(Collection secrets, Apfloat probability) {

        Objects.requireNonNull(secrets);
        Objects.requireNonNull(probability);

        this.secrets = secrets;
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {

        if(! (o instanceof SafetyProperty)) {
            return false;
        }

        if(!(probability.equals(((SafetyProperty) o).probability))) return false;

        return secrets.equals(((SafetyProperty) o).secrets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secrets, probability);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("secrets", secrets)
                .add("probability", probability.toString())
                .toString();
    }

}
