package rewriting.terms;

import rewriting.Signature;
import util.ParametricString;

import java.util.*;
import java.util.function.Function;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class TermFactory {

    private static Collection<String> PRIVATE_NAMES = new ArrayList<>();
    private static Collection<String> PUBLIC_NAMES = new ArrayList<>();
    private static Collection<String> VARIABLES = new ArrayList<>();
    private static Collection<FunctionSymbol> FUNCTIONS = new ArrayList<>();

    public static void initTermBuilder(Signature signature) {

        for(NameTerm name : signature.getPrivateNames()) {
            PRIVATE_NAMES.add(name.getName());
        }

        for(NameTerm name : signature.getPublicNames()) {
            PUBLIC_NAMES.add(name.getName());
        }

        for(VariableTerm var : signature.getVariables()) {
            VARIABLES.add(var.getName());
        }

        for(FunctionSymbol functionSymbol : signature.getFunctions()) {
            FUNCTIONS.add(functionSymbol);
        }
    }

    public static Term buildTerm(String termString) throws TermParseException {

        if(!termString.contains("(")) {

            if(VARIABLES.contains(termString)) {
                return new VariableTerm(termString);
            }

            if(PRIVATE_NAMES.contains(termString)) {
                return new NameTerm(termString, true);
            }

            if(PUBLIC_NAMES.contains(termString)) {
                return new NameTerm(termString, false);
            }

            throw new TermParseException(Resources.BAD_PARSE.evaluate(Collections.singletonList(termString)));

        } else {

            if(!termString.endsWith(")")) {
                throw new TermParseException(Resources.BAD_PARSE.evaluate(Collections.singletonList(termString)));
            } else {
                String functionSymbol = termString.substring(0, termString.indexOf("(")).trim();

                String subtermString = termString.substring(termString.indexOf("(") + 1, termString.length()-1);
                Collection<String> subterms = new ArrayList<>();

                int openCount = 0;
                int closedCount = 0;
                int startPosition = 0;

                for(int i=0; i < subtermString.length(); i++) {

                    if(subtermString.charAt(i) ==  '(') {
                        openCount++;
                    }

                    if(subtermString.charAt(i) ==  ')') {
                        closedCount++;
                    }

                    if(subtermString.charAt(i) ==  ',') {

                        if(openCount == closedCount) {
                            subterms.add(subtermString.substring(startPosition, i).trim());
                            startPosition = i+1;
                        }
                    }
                }

                subterms.add(subtermString.substring(startPosition).trim());

                if(openCount != closedCount) {
                    throw new TermParseException(Resources.UNBALANCED_PARENS
                            .evaluate(Collections.singletonList(termString)));
                }

                FunctionSymbol root = new FunctionSymbol(functionSymbol, subterms.size());

                if(!FUNCTIONS.contains(root)) {
                    throw new TermParseException(Resources.UNKNOWN_ROOT_SYMBOL
                            .evaluate(new ArrayList<>(Arrays.asList(termString, functionSymbol))));
                } else {

                    List<Term> parsedSubterms = new ArrayList<>();

                    for(String subterm : subterms) {
                        parsedSubterms.add(buildTerm(subterm));
                    }

                    return new FunctionTerm(root, parsedSubterms);
                }
            }
        }
    }
}
