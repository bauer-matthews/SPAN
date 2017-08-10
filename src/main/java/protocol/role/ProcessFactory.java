package protocol.role;

import org.apfloat.Apfloat;
import rewriting.Equality;
import rewriting.terms.*;

import java.util.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ProcessFactory {

    private static Map<String, Apfloat> FRACTION_CONSTANTS;

    public static void initActionBuilder(Map<String, Apfloat> fractionConstants) {
        FRACTION_CONSTANTS = fractionConstants;
    }

    public static AtomicProcess buildAction(String actionString) throws TermParseException, ActionParseException {

        if (actionString.startsWith("in")) {
            return parseInput(actionString);
        } else if (actionString.startsWith("[")) {
            return parseOutput(actionString);
        } else {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        }
    }

    private static AtomicProcess parseInput(String actionString) throws TermParseException, ActionParseException {
        String variable = actionString.substring(actionString.indexOf("(") + 1, actionString.length() - 1);

        Term var = TermFactory.buildTerm(variable);

        if (!(var instanceof VariableTerm)) {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        } else {
            return new InputProcess((VariableTerm) var);
        }
    }

    private static AtomicProcess parseOutput(String actionString) throws TermParseException, ActionParseException {

        String guardString = actionString.substring(actionString.indexOf("[") + 1, actionString.indexOf("]"));
        String outString = actionString.substring(actionString.indexOf("]") + 1);

        Collection<Equality> guards = new ArrayList<>();
        String[] guardStrings = guardString.split(Resources.TEST_DELIMITER);

        for (String guardString1 : guardStrings) {
            if (!guardString1.trim().equalsIgnoreCase(Resources.NULL_TEST)) {
                guards.add(parseEquality(guardString1.trim()));
            }
        }

        Collection<ProbOutput> probOutputs = new ArrayList<>();

        String[] outStrings = outString.substring(outString.indexOf("(") + 1, outString.length() - 1).split("\\+");

        for (String probOutString : outStrings) {
            probOutputs.add(parseProbOutputs(probOutString));
        }

        return new OutputProcess(guards, probOutputs);
    }

    private static ProbOutput parseProbOutputs(String probOutput) throws TermParseException, ActionParseException {

        List<Term> outputTerms = new ArrayList<>();

        String[] probPieces = probOutput.split("->");

        if (probPieces.length != 2) {
            throw new ActionParseException(Resources.BAD_OUTPUT.evaluate(Collections.singletonList(probOutput)));
        }

        Apfloat fraction = FRACTION_CONSTANTS.get(probPieces[0].trim());

        if (fraction == null) {
            try {
                fraction = new Apfloat(probPieces[0].trim());
            } catch (NumberFormatException ex) {
                throw new ActionParseException(Resources.BAD_PROB
                        .evaluate(Collections.singletonList(probPieces[0].trim())));
            }
        }

        // TODO: localize symbol below
        String[] outPieces = probPieces[1].split("\\|");

        for (String outPiece : outPieces) {
            outputTerms.add(TermFactory.buildTerm(outPiece.trim()));
        }

        return new ProbOutput(fraction, outputTerms);
    }

    private static Equality parseEquality(String guard) throws TermParseException, ActionParseException {
        String[] pieces = guard.split("=");

        if (pieces.length != 2) {
            throw new ActionParseException(Resources.BAD_EQUALITY.evaluate(Collections.singletonList(guard)));
        }

        Term lhs = TermFactory.buildTerm(pieces[0].trim());
        Term rhs = TermFactory.buildTerm(pieces[1].trim());

        return new Equality(lhs, rhs);
    }
}