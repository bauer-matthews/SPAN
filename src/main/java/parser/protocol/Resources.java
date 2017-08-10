package parser.protocol;

import util.ParametricString;

import java.util.Collections;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class Resources {

    static final ParametricString INVALID_STATEMENT = new ParametricString("Invalid statement: " +
            "[#1]", Collections.singletonList("#1"));

    static final ParametricString UNRECOGNIZED_COMMAND = new ParametricString("Ignoring unrecognized " +
            "command: #1", Collections.singletonList("#1"));

    static final ParametricString INVALID_COMMAND = new ParametricString("Invalid command: " +
            "[#1]", Collections.singletonList("#1"));

    static final ParametricString INVALID_FUNCTION_SYMBOL = new ParametricString("Invalid function definition: " +
            "#1", Collections.singletonList("#1"));

    static final ParametricString INVALID_REWRITE_RULE = new ParametricString("Invalid rewrite rule: " +
            "#1", Collections.singletonList("#1"));

    static final ParametricString INVALID_RECIPE_SIZE = new ParametricString("Unable to parse recipe size: " +
            "#1", Collections.singletonList("#1"));

    static final ParametricString INVALID_SECRECY_PROPERTY = new ParametricString("Invalid secrecy property defintion: " +
            "#1", Collections.singletonList("#1"));

    static final String NO_VERSION = "No protocol version given";

    static final String NO_RECIPE_SIZE = "No max recipe size given";

    static final String NO_SECRECY_PROPERTY = "No secrecy property given";

    static final String MISSING_INVALID_SECTIONS = "Missing or invalid sections in protocol definition";

    static final String SECRECY_PROP_DELIMITER = ">=";
}
