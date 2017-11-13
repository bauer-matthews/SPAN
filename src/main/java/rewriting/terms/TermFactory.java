package rewriting.terms;

import cache.RunConfiguration;
import rewriting.Signature;
import theories.Xor;

import java.util.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class TermFactory {

    private static final String TOP = "T";
    private static final String FRAME_VAR = "W";
    private static final String UNDERSCORE = "_";

    private static final List<String> RESERVED_STRINGS = new ArrayList<>();

    private static Collection<NameTerm> PRIVATE_NAMES = new ArrayList<>();
    private static Collection<NameTerm> PUBLIC_NAMES = new ArrayList<>();
    private static Collection<VariableTerm> VARIABLES = new ArrayList<>();
    private static Collection<FunctionSymbol> USER_FUNCTIONS = new ArrayList<>();
    private static Collection<FunctionSymbol> ALL_FUNCTIONS = new ArrayList<>();

    static {
        RESERVED_STRINGS.add(TOP);
        RESERVED_STRINGS.add(FRAME_VAR);
        RESERVED_STRINGS.add(UNDERSCORE);
    }

    public static void initTermBuilder(Signature signature) {

        PRIVATE_NAMES = signature.getPrivateNames();
        PUBLIC_NAMES = signature.getPublicNames();
        VARIABLES = signature.getVariables();
        USER_FUNCTIONS = signature.getFunctions();
        ALL_FUNCTIONS.addAll(USER_FUNCTIONS);

        if (RunConfiguration.isXor()) {
            Xor.addXorFunctions(ALL_FUNCTIONS);
        }
    }

    public static List<String> getReservedStrings() {
        return RESERVED_STRINGS;
    }

    public static Term buildTerm(String termString) throws TermParseException {

        if (!termString.contains("(")) {

            for (FunctionSymbol functionSymbol : ALL_FUNCTIONS) {
                if ((functionSymbol.getArity() == 0) && (termString.equals(functionSymbol.getSymbol()))) {
                    return new FunctionTerm(functionSymbol, Collections.emptyList());
                }
            }

            for (VariableTerm variableTerm : VARIABLES) {
                if (termString.equals(variableTerm.getName())) return variableTerm;
            }

            for (NameTerm nameTerm : PRIVATE_NAMES) {
                if (termString.equals(nameTerm.getName())) return nameTerm;
            }

            for (NameTerm nameTerm : PUBLIC_NAMES) {
                if (termString.equals(nameTerm.getName())) return nameTerm;
            }

            throw new TermParseException(Resources.BAD_PARSE.evaluate(Collections.singletonList(termString)));

        } else {

            if (!termString.endsWith(")")) {
                throw new TermParseException(Resources.BAD_PARSE.evaluate(Collections.singletonList(termString)));
            } else {
                String functionSymbol = termString.substring(0, termString.indexOf("(")).trim();

                String subtermString = termString.substring(termString.indexOf("(") + 1, termString.length() - 1);
                List<String> subterms = new ArrayList<>();

                int openCount = 0;
                int closedCount = 0;
                int startPosition = 0;

                for (int i = 0; i < subtermString.length(); i++) {

                    if (subtermString.charAt(i) == '(') {
                        openCount++;
                    }

                    if (subtermString.charAt(i) == ')') {
                        closedCount++;
                    }

                    if (subtermString.charAt(i) == ',') {

                        if (openCount == closedCount) {
                            subterms.add(subtermString.substring(startPosition, i).trim());
                            startPosition = i + 1;
                        }
                    }
                }

                subterms.add(subtermString.substring(startPosition).trim());

                if (openCount != closedCount) {
                    throw new TermParseException(Resources.UNBALANCED_PARENS
                            .evaluate(Collections.singletonList(termString)));
                }

                FunctionSymbol root = null;
                for (FunctionSymbol fs : ALL_FUNCTIONS) {
                    if (fs.getSymbol().equals(functionSymbol)) {
                        root = fs;
                        break;
                    }
                }

                if (root == null) {
                    throw new TermParseException(Resources.UNKNOWN_ROOT_SYMBOL
                            .evaluate(new ArrayList<>(Arrays.asList(termString, functionSymbol))));
                }

                List<Term> parsedSubterms = new ArrayList<>();

                if (root.equals(Xor.getPLUS()) && subterms.size() > 2) {

                    parsedSubterms.add(buildTerm(subterms.get(0)));
                    subterms.remove(0);
                    parsedSubterms.add(createPlusSubterm(subterms));

                } else {
                    for (String subterm : subterms) {
                        parsedSubterms.add(buildTerm(subterm));
                    }
                }

                return new FunctionTerm(root, parsedSubterms);
            }
        }
    }

    private static Term createPlusSubterm(List<String> subterms) throws TermParseException {

        if (subterms.size() == 1) {
            return buildTerm(subterms.get(0));
        }

        List<Term> terms = new ArrayList<>();

        if (subterms.size() == 2) {

            terms.add(buildTerm(subterms.get(0)));
            terms.add(buildTerm(subterms.get(1)));
            return new FunctionTerm(Xor.getPLUS(), terms);

        } else {

            terms.add(buildTerm(subterms.get(0)));
            subterms.remove(0);
            terms.add(createPlusSubterm(subterms));
            return new FunctionTerm(Xor.getPLUS(), terms);
        }
    }
}
