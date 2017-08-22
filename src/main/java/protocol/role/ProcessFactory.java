package protocol.role;

import org.apfloat.Apfloat;
import parser.protocol.ProtocolParseException;
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

        if ((!actionString.contains("}")) || (!actionString.startsWith("{"))) {
            throw new ActionParseException("Phase is not valid for action: " + actionString);
        }

        int delimiter = actionString.indexOf("}") + 1;

        int phase = parsePhase(actionString.substring(0, delimiter).trim());


        String processActionString = actionString.substring(delimiter).trim();

        if (processActionString.startsWith("in")) {
            return parseInput(processActionString, phase);
        } else if (processActionString.startsWith("[")) {
            return parseOutput(processActionString, phase);
        } else {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        }
    }

    private static int parsePhase(String phaseString) throws ActionParseException {

        String number = phaseString.substring(phaseString.indexOf("{")+1, phaseString.indexOf("}"));

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            throw new ActionParseException("Unable to parse phase: " + phaseString);
        }
    }

    private static AtomicProcess parseInput(String actionString, int phase)
            throws TermParseException, ActionParseException {

        String inp = actionString.substring(actionString.indexOf("(") + 1, actionString.length() - 1);

        if (!(inp.contains("{") && inp.contains("}"))) {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        }

        String[] pieces = inp.split("\\{");
        Term var = TermFactory.buildTerm(pieces[0].trim());

        Optional<Term> guard;
        String guardString = pieces[1].substring(0, pieces[1].length() - 1).trim();

        if (guardString.isEmpty()) {
            guard = Optional.empty();
        } else {
            guard = Optional.of(TermFactory.buildTerm(guardString));
        }


        if (!(var instanceof VariableTerm)) {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        } else {
            return new InputProcess((VariableTerm) var, guard, phase);
        }
    }

    private static AtomicProcess parseOutput(String actionString, int phase) throws TermParseException, ActionParseException {

        String guardString = actionString.substring(actionString.indexOf("[") + 1, actionString.indexOf("]"));
        String outString = actionString.substring(actionString.indexOf("]") + 1);

        Collection<Guard> guards = new ArrayList<>();
        String[] guardStrings = guardString.split(Resources.TEST_DELIMITER);

        for (String guardString1 : guardStrings) {
            if (!guardString1.trim().equalsIgnoreCase(Resources.NULL_TEST)) {
                guards.add(parseGuard(guardString1.trim()));
            }
        }

        Collection<ProbOutput> probOutputs = new ArrayList<>();

        String[] outStrings = outString.substring(outString.indexOf("(") + 1, outString.length() - 1).split("\\+");

        for (String probOutString : outStrings) {
            probOutputs.add(parseProbOutputs(probOutString));
        }

        return new OutputProcess(guards, probOutputs, phase);
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

        String[] outPieces = probPieces[1].split("\\#");

        for (String outPiece : outPieces) {
            outputTerms.add(TermFactory.buildTerm(outPiece.trim()));
        }

        return new ProbOutput(fraction, outputTerms);
    }

    private static Guard parseGuard(String guard) throws TermParseException, ActionParseException {

        if (guard.contains("==")) {

            String[] pieces = guard.split("==");

            if (pieces.length != 2) {
                throw new ActionParseException(Resources.BAD_EQUALITY.evaluate(Collections.singletonList(guard)));
            }

            Term lhs = TermFactory.buildTerm(pieces[0].trim());
            Term rhs = TermFactory.buildTerm(pieces[1].trim());

            return new Guard(new Equality(lhs, rhs), true);

        } else {

            String[] pieces = guard.split("!=");

            if (pieces.length != 2) {
                throw new ActionParseException(Resources.BAD_EQUALITY.evaluate(Collections.singletonList(guard)));
            }

            Term lhs = TermFactory.buildTerm(pieces[0].trim());
            Term rhs = TermFactory.buildTerm(pieces[1].trim());

            return new Guard(new Equality(lhs, rhs), false);
        }
    }
}
