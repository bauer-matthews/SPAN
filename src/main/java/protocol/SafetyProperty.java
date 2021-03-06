package protocol;

import com.google.common.base.MoreObjects;
import org.apfloat.Aprational;
import rewriting.terms.Term;

import java.util.List;
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

    private final List<Term> secrets;
    private final Aprational probability;

    public SafetyProperty(List<Term> secrets, Aprational probability) {

        Objects.requireNonNull(secrets);
        Objects.requireNonNull(probability);

        this.secrets = secrets;
        this.probability = probability;
    }

    public Aprational getProbability() {
        return probability;
    }

    public List<Term> getSecrets() {
        return secrets;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof SafetyProperty)) {
            return false;
        }

        if (!(probability.equals(((SafetyProperty) o).probability))) return false;

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
