package protocol.role;

import cache.RunConfiguration;
import org.apfloat.Aprational;
import rewriting.Equality;
import rewriting.terms.Term;
import rewriting.terms.TermFactory;
import rewriting.terms.TermParseException;
import rewriting.terms.VariableTerm;
import util.aprational.AprationalFactory;

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

    private static Map<String, Aprational> FRACTION_CONSTANTS;
    private static List<VariableTerm> REWRITE_VARIABLES;
    private static Map<String, Role> SUBROLE_MAP;


    public static void initFractionConstants(Map<String, Aprational> fractionConstants) {
        FRACTION_CONSTANTS = fractionConstants;
    }

    public static void initRewriteVariables(List<VariableTerm> variables) {
        REWRITE_VARIABLES = variables;
    }

    public static void setSubRolesMap(Map<String, Role> subroleMap) {
        SUBROLE_MAP = subroleMap;
    }

    public static AtomicProcess buildAction(String actionString) throws TermParseException, ActionParseException {

        if (FRACTION_CONSTANTS == null) {
            throw new UnsupportedOperationException("Fraction constants have not been initialized.");
        }

        if (REWRITE_VARIABLES == null) {
            throw new UnsupportedOperationException("Rewrite variables have not been initialized.");
        }

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
        } else if (processActionString.startsWith("IF")) {
            return parseConditionalOutput(actionString, phase);
        } else {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        }
    }

    private static int parsePhase(String phaseString) throws ActionParseException {

        String number = phaseString.substring(phaseString.indexOf("{") + 1, phaseString.indexOf("}"));

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
        Term var = validateTerm(TermFactory.buildTerm(pieces[0].trim()));

        Optional<Term> guard;
        String guardString = pieces[1].substring(0, pieces[1].length() - 1).trim();

        if (guardString.isEmpty()) {
            guard = Optional.empty();
        } else {
            guard = Optional.of(validateTerm(TermFactory.buildTerm(guardString)));
        }

        if (!(var instanceof VariableTerm)) {
            throw new ActionParseException(Resources.BAD_ACTION.evaluate(Collections.singletonList(actionString)));
        } else {
            return new InputProcess((VariableTerm) var, guard, phase);
        }
    }

    private static Term validateTerm(Term term) throws ActionParseException {

        for (VariableTerm variableTerm : term.getVariables()) {
            if (REWRITE_VARIABLES.contains(variableTerm)) {
                throw new ActionParseException(Resources.NO_REWRITE_VARS);
            }
        }

        return term;
    }

    private static AtomicProcess parseConditionalOutput(String actionString, int phase)
            throws TermParseException, ActionParseException {

        String[] guardAndRemainder = actionString.split("THEN");
        String guardString = extractGuardString(guardAndRemainder[0]);

        String[] ifAndRemainder = guardAndRemainder[1].split("ELSE");
        String ifString = ifAndRemainder[0].trim();
        String thenString = ifAndRemainder[1].trim();

        Collection<Guard> guards = generateGuards(guardString);
        Collection<ProbOutput> ifProbOutputs = extractProbOutputs(ifString);
        Collection<ProbOutput> thenProbOutputs = extractProbOutputs(thenString);

        return new ConditionalOutputProcess(guards, ifProbOutputs, thenProbOutputs, phase);
    }

    private static AtomicProcess parseOutput(String actionString, int phase)
            throws TermParseException, ActionParseException {

        String guardString = extractGuardString(actionString);
        String outString = actionString.substring(actionString.indexOf("]") + 1);

        Collection<Guard> guards = generateGuards(guardString);
        Collection<ProbOutput> probOutputs = extractProbOutputs(outString);

        return new OutputProcess(guards, probOutputs, phase);
    }

    private static Collection<ProbOutput> extractProbOutputs(String outString) throws TermParseException,
            ActionParseException {

        Collection<ProbOutput> probOutputs = new ArrayList<>();

        if (outString.trim().startsWith("permute")) {

            List<Term> permuteTerms = parsePermuteTerms(
                    outString.substring(outString.indexOf("(") + 1, outString.length() - 1));

            List<List<Term>> permutations = generatePermutations(permuteTerms);
            Aprational fraction = AprationalFactory.fromString("1/" + permutations.size());

            for (List<Term> permutation : permutations) {
                probOutputs.add(new ProbOutput(fraction, permutation, new Role(Collections.emptyList())));
            }

        } else if (outString.trim().startsWith("out")) {

            String[] outStrings = outString.substring(outString.indexOf("(") + 1, outString.length() - 1).split("\\+");

            for (String probOutString : outStrings) {
                probOutputs.add(parseProbOutputs(probOutString));
            }

        } else {
            throw new ActionParseException("Unable to parse output command: " + outString);
        }

        return probOutputs;
    }

    private static String extractGuardString(String string) {
        return string.substring(string.indexOf("[") + 1, string.indexOf("]"));
    }

    private static Collection<Guard> generateGuards(String guardString) throws TermParseException, ActionParseException {

        Collection<Guard> guards = new ArrayList<>();

        String[] guardStrings = guardString.split(Resources.TEST_DELIMITER);

        for (String guardString1 : guardStrings) {
            if (!guardString1.trim().equalsIgnoreCase(Resources.NULL_TEST)) {
                guards.add(parseGuard(guardString1.trim()));
            }
        }

        return guards;
    }

    private static List<List<Term>> generatePermutations(List<Term> terms) {

        List<List<Term>> permutations = new ArrayList<>();

        if (terms.size() == 1) {
            permutations.add(terms);
        } else {

            for (Term term : terms) {
                List<Term> subList = new ArrayList<>();
                subList.addAll(terms);
                subList.remove(term);

                for (List<Term> subListPerm : generatePermutations(subList)) {
                    List<Term> perm = new ArrayList<>();
                    perm.add(term);
                    perm.addAll(subListPerm);
                    permutations.add(perm);
                }
            }
        }

        return permutations;
    }

    private static ProbOutput parseProbOutputs(String probOutput) throws TermParseException, ActionParseException {

        List<Term> outputTerms = new ArrayList<>();

        String[] probPieces = probOutput.split("->");

        if (probPieces.length != 2) {
            throw new ActionParseException(Resources.BAD_OUTPUT.evaluate(Collections.singletonList(probOutput)));
        }

        Aprational fraction = FRACTION_CONSTANTS.get(probPieces[0].trim());

        if (fraction == null) {
            try {
                fraction = AprationalFactory.fromString(probPieces[0].trim());
            } catch (NumberFormatException ex) {
                throw new ActionParseException(Resources.BAD_PROB
                        .evaluate(Collections.singletonList(probPieces[0].trim())));
            }
        }

        String[] outRolePieces = probPieces[1].split("\\#");

        Role subrole;
        if (outRolePieces.length == 1) {
            subrole = new Role(Collections.emptyList());
        } else {
            subrole = getSubrole(outRolePieces[1].trim());
        }

        if (!outRolePieces[0].trim().equalsIgnoreCase(Resources.EMPTY_OUTPUT)) {
            String[] outPieces = outRolePieces[0].split("\\@");
            for (String outPiece : outPieces) {
                outputTerms.add(validateTerm(TermFactory.buildTerm(outPiece.trim())));
            }
        } else {
            RunConfiguration.enableEmptyOutputs();
        }

        return new ProbOutput(fraction, outputTerms, subrole);
    }

    private static List<Term> parsePermuteTerms(String permuteTermsString) throws TermParseException,
            ActionParseException {

        List<Term> outputTerms = new ArrayList<>();

        String[] outPieces = permuteTermsString.split("\\@");
        for (String outPiece : outPieces) {
            outputTerms.add(validateTerm(TermFactory.buildTerm(outPiece.trim())));
        }

        return outputTerms;
    }

    private static Role getSubrole(String roleName) throws ActionParseException {

        if (SUBROLE_MAP == null) {
            throw new UnsupportedOperationException("Subroles have not been initialized.");
        }

        if (SUBROLE_MAP.get(roleName) == null) {
            throw new ActionParseException("Action contains a reference to an undefined subrole");
        }

        return SUBROLE_MAP.get(roleName);
    }

    private static Guard parseGuard(String guard) throws TermParseException, ActionParseException {

        if (guard.contains("==")) {

            String[] pieces = guard.split("==");

            if (pieces.length != 2) {
                throw new ActionParseException(Resources.BAD_EQUALITY.evaluate(Collections.singletonList(guard)));
            }

            Term lhs = validateTerm(TermFactory.buildTerm(pieces[0].trim()));
            Term rhs = validateTerm(TermFactory.buildTerm(pieces[1].trim()));

            return new Guard(new Equality(lhs, rhs), true);

        } else {

            String[] pieces = guard.split("!=");

            if (pieces.length != 2) {
                throw new ActionParseException(Resources.BAD_EQUALITY.evaluate(Collections.singletonList(guard)));
            }

            Term lhs = validateTerm(TermFactory.buildTerm(pieces[0].trim()));
            Term rhs = validateTerm(TermFactory.buildTerm(pieces[1].trim()));

            return new Guard(new Equality(lhs, rhs), false);
        }
    }
}
