package util;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/25/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public enum ExitCode {

    GOOD(0), OPTION_ERROR(100), PROTOCOL_PARSE_ERROR(101), EQUIVALENCE_ERROR(202);

    private final int id;

    ExitCode(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

}
