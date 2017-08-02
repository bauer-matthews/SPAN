package util;

import com.google.common.base.MoreObjects;
import log.Console;
import log.Severity;
import parser.protocol.Statement;

import java.util.ArrayList;
import java.util.List;
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
public class ParametricString {

    private final String string;
    private final List<String> parameters;

    public ParametricString(String string, List<String> parameters) {

        Objects.requireNonNull(string);
        Objects.requireNonNull(parameters);

        this.string = string;
        this.parameters = parameters;
    }

    public String evaluate(List<String> values) {

        String evalString = string;

        if (parameters.size() != values.size()) {
            Console.printMessage(Severity.WARNING, "Evaluating parametric string with " +
                    "an incorrect number of arguments");
        }

        int size = Math.min(parameters.size(), values.size());

        for (int i = 0; i < size; i++) {
            evalString = evalString.replaceAll(parameters.get(i), values.get(i));
        }

        return evalString;
    }

    public String getString() {
        return string;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof ParametricString)) {
            return false;
        }

        if (!equal(string, ((ParametricString) o).getString())) return false;
        if (equal(parameters, ((ParametricString) o).getParameters())) return true;

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, parameters);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("string", string)
                .add("parameters", parameters.toString())
                .toString();
    }
}
