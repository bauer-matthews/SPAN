package rewriting.terms;

import util.ParametricString;

import java.util.ArrayList;
import java.util.Arrays;
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

    static final String INVALID_NUM_SUBTERMS = "Invalid number of subterms for function symbol";

    static final ParametricString BAD_PARSE = new ParametricString("Unable to parse term from string: " +
            "#1", Collections.singletonList("#1"));

    static final ParametricString UNBALANCED_PARENS = new ParametricString("Unable to parse term from string: " +
            "#1 (unbalanced parens)", Collections.singletonList("#1"));

    static final ParametricString UNKNOWN_ROOT_SYMBOL = new ParametricString("Unable to parse term from string: " +
            "#1 (unknown function symbol #2)", new ArrayList<>(Arrays.asList("#1", "#2")));

}
