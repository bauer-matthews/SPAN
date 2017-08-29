package protocol.role;

import util.ParametricString;

import java.util.Collections;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Resources {

    static final String INVALID_PROBS = "Output probabilities do not sum to one.";

    static final ParametricString BAD_ACTION = new ParametricString("Unable to parse action from string: " +
            "#1", Collections.singletonList("#1"));

    static final ParametricString BAD_EQUALITY = new ParametricString("Unable to parse equality test from string: " +
            "#1", Collections.singletonList("#1"));

    static final ParametricString BAD_OUTPUT = new ParametricString("Unable to parse probabilistic output" +
            " from string: #1", Collections.singletonList("#1"));

    static final ParametricString BAD_PROB = new ParametricString("Unable to parse probability" +
            " from string: #1", Collections.singletonList("#1"));

    static final String NULL_TEST = "T";

    static final String TEST_DELIMITER = "&";

    static final String EMPTY_OUTPUT = "T";
}
