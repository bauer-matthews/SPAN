package protocol.role;

import com.google.common.base.MoreObjects;
import org.apfloat.Apfloat;
import rewriting.terms.Term;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Objects.equal;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ProbOutput {

    private final Apfloat probability;
    private final List<Term> outputTerms;

    ProbOutput(Apfloat probability, List<Term> outputTerms) {

        Objects.requireNonNull(probability);
        Objects.requireNonNull(outputTerms);

        this.probability = probability;
        this.outputTerms = outputTerms;
    }

    Apfloat getProbability() {
        return probability;
    }

    public List<Term> getOutputTerms() {
        return outputTerms;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof ProbOutput)) {
            return false;
        }

        if (!probability.equals(((ProbOutput) o).probability)) return false;
        if (!outputTerms.equals(((ProbOutput) o).outputTerms)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(probability, outputTerms);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("probability", probability.toString())
                .add("output terms", outputTerms.toString())
                .toString();
    }
}
