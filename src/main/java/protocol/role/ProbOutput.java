package protocol.role;

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
public class ProbOutput {

    private final Aprational probability;
    private final List<Term> outputTerms;
    private final Role subrole;

    ProbOutput(Aprational probability, List<Term> outputTerms, Role subrole) {

        Objects.requireNonNull(probability);
        Objects.requireNonNull(outputTerms);
        Objects.requireNonNull(subrole);

        this.probability = probability;
        this.outputTerms = outputTerms;
        this.subrole = subrole;
    }

    public Aprational getProbability() {
        return probability;
    }

    public List<Term> getOutputTerms() {
        return outputTerms;
    }

    public Role getSubrole() {
        return subrole;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof ProbOutput)) {
            return false;
        }

        if (!probability.equals(((ProbOutput) o).probability)) return false;
        if (!outputTerms.equals(((ProbOutput) o).outputTerms)) return false;
        if (!subrole.equals(((ProbOutput) o).subrole)) return false;


        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(probability, outputTerms, subrole);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("probability", probability.toString())
                .add("output terms", outputTerms.toString())
                .add("subrole", subrole.toString())
                .toString();
    }
}
