package parser.protocol;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Objects.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Statement {

    private final String command;
    private final String value;

    Statement(String command, String value) {
        this.command = command;
        this.value = value;
    }

    String getCommand() {
        return this.command;
    }

    String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Statement) {
            if (!equal(command, ((Statement) o).getCommand())) return false;
            if (equal(value, ((Statement) o).getValue())) return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("command", command)
                .add("value", value)
                .toString();
    }
}
