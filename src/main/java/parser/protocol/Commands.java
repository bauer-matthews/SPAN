package parser.protocol;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class Commands {

    // Metadata Commands
    static final String VERSION = "version";
    static final String RECIPE_DEPTH = "recipe depth";

    // Hidden Metadata Command
    static final String REWRITING_CACHE = "rewriting cache";
    static final String EQUIVALENCE_CACHE = "equivalence cache";
    static final String SUBSTITUTION_CACHE = "substitution cache";
    static final String UNIFICATION_CACHE = "unification cache";
    static final String ACTION_FACTORY_CACHE = "action factory cache";

    // Constants Commands
    static final String FRACTION = "fraction";

    // Signature Commands
    static final String FUNCTION = "function";
    static final String VARIABLES = "variables";
    static final String PUBLIC_NAMES = "public names";
    static final String PRIVATE_NAMES = "private names";
    static final String SORT = "sort";
    static final String SUBSORT = "subsort";

    // Rewrite Commands
    static final String REWRITE = "rewrite";

    // Protocol Commands
    static final String ROLE = "role";
    static final String SUBROLE = "subrole";

    // Safety Commands
    static final String SECRECY = "secrecy";
}
