package parser.protocol;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import log.Console;
import log.Severity;
import org.apfloat.Apfloat;
import protocol.Metadata;
import protocol.Protocol;
import protocol.ProtocolBuilder;
import protocol.SafetyProperty;
import protocol.role.AtomicProcess;
import protocol.role.ProcessFactory;
import protocol.role.ActionParseException;
import protocol.role.Role;
import rewriting.*;
import rewriting.terms.*;
import util.apfloat.ApfloatFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/23/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ProtocolParser {

    private static final String LINE_DELIMITER = ";";
    private static final String STATEMENT_DELIMITER = ":";

    public static Protocol parse() throws ProtocolParseException, TermParseException, ActionParseException {

        File protocolFile = RunConfiguration.getProtocolFile();

        Map<Section, String> sectionText = parseSections(protocolFile);

        Protocol protocol = new ProtocolBuilder()
                .metadata(parseMetadata(sectionText.get(Section.METADATA)))
                .fractionConstants(parseFractionConstants(sectionText.get(Section.CONSTANTS)))
                .signature(parseSignature(sectionText.get(Section.SIGNATURE)))
                .rewrites(parseRewrites(sectionText.get(Section.REWRITES)))
                .roles(parseRoles(sectionText.get(Section.ROLES)))
                .safetyProperty(parseSafetyProperties(sectionText.get(Section.SAFETY)))
                .build();

        GlobalDataCache.setProtocol(protocol);

        return protocol;
    }

    private static Map<Section, String> parseSections(File protocolFile) throws ProtocolParseException {

        Map<Section, String> sectionText = new HashMap<>();

        Section section = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(protocolFile))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {

                //noinspection StatementWithEmptyBody
                if (line.startsWith("#")) {
                    // Comment, ignore
                } else if (line.startsWith(".")) {

                    if (section != null) {
                        sectionText.put(section, stringBuilder.toString());
                    }

                    section = getSection(line.substring(1).trim());
                    stringBuilder = new StringBuilder();
                } else {
                    stringBuilder.append(line);
                }

                line = reader.readLine();
            }

            if (section != null) {
                sectionText.put(section, stringBuilder.toString());
            }

        } catch (IOException ex) {
            Console.printError(Severity.ERROR, ex.getMessage());
            System.exit(1);
        }

        List<Section> sectionList = Arrays.asList(Section.values());
        if (!sectionText.keySet().containsAll(sectionList)) {
            throw new ProtocolParseException(Resources.MISSING_INVALID_SECTIONS);
        }

        return sectionText;
    }

    private static Section getSection(String sectionString) throws ProtocolParseException {
        switch (sectionString) {
            case "metadata":
                return Section.METADATA;
            case "constants":
                return Section.CONSTANTS;
            case "signature":
                return Section.SIGNATURE;
            case "rewrites":
                return Section.REWRITES;
            case "roles":
                return Section.ROLES;
            case "safety":
                return Section.SAFETY;
            default:
                throw new ProtocolParseException("Unknown section: " + sectionString);
        }
    }

    private static Metadata parseMetadata(String text) throws ProtocolParseException {

        Collection<Statement> statements = extractStatements(text);

        String version = null;
        Integer recipeSize = null;
        for (Statement statement : statements) {

            if (statement.getCommand().equalsIgnoreCase(Commands.VERSION)) {
                version = statement.getValue();
            } else if (statement.getCommand().equalsIgnoreCase(Commands.RECIPE_DEPTH)) {

                try {
                    recipeSize = Integer.parseInt(statement.getValue().trim());
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException(Resources.INVALID_RECIPE_SIZE
                            .evaluate(Collections.singletonList(statement.getValue().trim())));
                }

            } else if (statement.getCommand().equalsIgnoreCase(Commands.REWRITING_CACHE)) {

                try {
                    RunConfiguration.setRewritingCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Rewriting Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().equalsIgnoreCase(Commands.EQUIVALENCE_CACHE)) {

                try {
                    RunConfiguration.setEquivalenceCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Equivalence Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().equalsIgnoreCase(Commands.UNIFICATION_CACHE)) {

                try {
                    RunConfiguration.setUnificationCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Unification Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().equalsIgnoreCase(Commands.SUBSTITUTION_CACHE)) {

                try {
                    RunConfiguration.setSubstitutionCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Substitution Cache Size: " + statement.getValue().trim());
                }

            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        if (version == null) {
            throw new ProtocolParseException(Resources.NO_VERSION);
        }

        if (recipeSize == null) {
            throw new ProtocolParseException(Resources.NO_RECIPE_SIZE);
        }

        return new Metadata(version, recipeSize.intValue());
    }

    private static Map<String, Apfloat> parseFractionConstants(String text) throws ProtocolParseException,
            NumberFormatException {

        Collection<Statement> statements = extractStatements(text);

        Map<String, Apfloat> constantMap = new HashMap<>();
        for (Statement statement : statements) {

            if (statement.getCommand().toLowerCase().startsWith(Commands.FRACTION)) {
                constantMap.put(getName(statement.getCommand()),
                        ApfloatFactory.fromString(statement.getValue().trim()));
            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        ProcessFactory.initActionBuilder(constantMap);

        return constantMap;
    }

    private static List<Sort> parseSorts(Collection<Statement> statements) {

        List<Sort> sorts = new ArrayList<>();

        for (Statement statement : statements) {

            if (statement.getCommand().toLowerCase().startsWith(Commands.SORT)) {
                for (String sortName : statement.getValue().split(",")) {
                    sorts.add(new Sort(sortName.trim()));
                }
            }
        }

        SortFactory.setSorts(sorts);
        return sorts;
    }

    private static List<SortOrder> parseSubsorts(Collection<Statement> statements) throws ProtocolParseException {

        List<SortOrder> sortOrders = new ArrayList<>();

        for (Statement statement : statements) {

            if (statement.getCommand().toLowerCase().startsWith(Commands.SUBSORT)) {

                String[] pieces = statement.getValue().split("<");

                if (pieces.length != 2) {
                    throw new ProtocolParseException("Invalid format for subsort statement: " + statement.getValue());
                } else {

                    Sort supersort = SortFactory.fromString(pieces[1].trim());
                    for (String subsortString : pieces[0].split(",")) {

                        Sort subsort = SortFactory.fromString(subsortString.trim());
                        sortOrders.add(new SortOrder(subsort, supersort));
                    }
                }
            }
        }

        SortFactory.setSortOrders(sortOrders);
        return sortOrders;
    }

    private static Signature parseSignature(String text) throws ProtocolParseException {

        Collection<Statement> statements = extractStatements(text);

        List<NameTerm> publicNames = new ArrayList<>();
        List<NameTerm> privateNames = new ArrayList<>();
        List<VariableTerm> variables = new ArrayList<>();
        List<FunctionSymbol> functions = new ArrayList<>();

        // NOTE: sorts must be parsed before the rest of the signature
        List<Sort> sorts = parseSorts(statements);
        List<SortOrder> sortOrders = parseSubsorts(statements);

        for (Statement statement : statements) {

            if (statement.getCommand().toLowerCase().startsWith(Commands.FUNCTION)) {

                String[] pieces = statement.getValue().split("\\|");
                String[] typePieces = pieces[1].split("->");

                if (pieces.length != 2 || typePieces.length != 2) {
                    throw new ProtocolParseException(Resources.INVALID_FUNCTION_SYMBOL
                            .evaluate(Collections.singletonList(statement.getValue())));
                }

                String[] sortPieces = typePieces[0].trim().split(" ");
                List<Sort> sortParameters = new ArrayList<>();

                for (String sortString : sortPieces) {
                    sortParameters.add(SortFactory.fromString(sortString.trim()));
                }

                Sort sortResult = SortFactory.fromString(typePieces[1].trim());

                functions.add(new FunctionSymbol(pieces[0].trim(), sortParameters, sortResult));

            } else if (statement.getCommand().toLowerCase().startsWith(Commands.PRIVATE_NAMES)) {

                String[] pieces = statement.getValue().split("\\|");

                if (pieces.length != 2) {
                    throw new ProtocolParseException("Invalid private name declaration: " + statement.getValue());
                }

                Sort sort = SortFactory.fromString(pieces[1].trim());

                for (String name : pieces[0].split(",")) {
                    privateNames.add(new NameTerm(name.trim(), true, sort));
                }

            } else if (statement.getCommand().toLowerCase().startsWith(Commands.VARIABLES)) {

                String[] pieces = statement.getValue().split("\\|");

                if (pieces.length != 2) {
                    throw new ProtocolParseException("Invalid variable declaration: " + statement.getValue());
                }

                Sort sort = SortFactory.fromString(pieces[1].trim());

                for (String var : pieces[0].split(",")) {
                    variables.add(new VariableTerm(var.trim(), sort));
                }

            } else if (statement.getCommand().toLowerCase().startsWith(Commands.PUBLIC_NAMES)) {

                String[] pieces = statement.getValue().split("\\|");

                if (pieces.length != 2) {
                    throw new ProtocolParseException("Invalid public name declaration: " + statement.getValue());
                }

                Sort sort = SortFactory.fromString(pieces[1].trim());

                for (String name : pieces[0].split(",")) {
                    publicNames.add(new NameTerm(name.trim(), false, sort));
                }

            } else if (statement.getCommand().toLowerCase().startsWith(Commands.SORT) ||
                    statement.getCommand().toLowerCase().startsWith(Commands.SUBSORT)) {
                // Already handled
            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        Signature signature = new Signature(functions, publicNames, privateNames, variables, sorts, sortOrders);
        validateSignature(signature);

        TermFactory.initTermBuilder(signature);
        return signature;
    }

    private static void validateSignature(Signature signature) throws ProtocolParseException {

        for (FunctionSymbol functionSymbol : signature.getFunctions()) {
            if (TermFactory.getReservedStrings().contains(functionSymbol.getSymbol())) {
                throw new ProtocolParseException("Use of a reserved symbol: " + functionSymbol.getSymbol());
            }
        }

        for (NameTerm name : signature.getPublicNames()) {
            if (TermFactory.getReservedStrings().contains(name.getName())) {
                throw new ProtocolParseException("Use of a reserved symbol: " + name.getName());
            }
        }

        for (NameTerm name : signature.getPrivateNames()) {
            if (TermFactory.getReservedStrings().contains(name.getName())) {
                throw new ProtocolParseException("Use of a reserved symbol: " + name.getName());
            }
        }

        for (VariableTerm variable : signature.getVariables()) {
            if (TermFactory.getReservedStrings().contains(variable.getName())) {
                throw new ProtocolParseException("Use of a reserved symbol: " + variable.getName());
            }
        }
    }

    private static Collection<Rewrite> parseRewrites(String text) throws ProtocolParseException, TermParseException {

        Collection<Statement> statements = extractStatements(text);
        Collection<Rewrite> rewrites = new ArrayList<>();

        for (Statement statement : statements) {
            if (statement.getCommand().toLowerCase().startsWith(Commands.REWRITE)) {

                String[] rule = statement.getValue().split("->");

                if (rule.length != 2) {
                    throw new ProtocolParseException(Resources.INVALID_REWRITE_RULE
                            .evaluate(Collections.singletonList(statement.getValue())));
                }

                Term lhs = TermFactory.buildTerm(rule[0].trim());
                Term rhs = TermFactory.buildTerm(rule[1].trim());

                rewrites.add(new Rewrite(lhs, rhs));

            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        return rewrites;
    }

    private static SafetyProperty parseSafetyProperties(String text) throws TermParseException,
            ProtocolParseException {

        Collection<Statement> statements = extractStatements(text);
        List<Term> secretTerms = new ArrayList<>();

        Apfloat probability = null;

        for (Statement statement : statements) {
            if (statement.getCommand().toLowerCase().startsWith(Commands.SECRECY)) {

                String[] pieces = statement.getValue().split(Resources.SECRECY_PROP_DELIMITER);

                if (pieces.length != 2) {
                    throw new ProtocolParseException(Resources.INVALID_SECRECY_PROPERTY
                            .evaluate(Collections.singletonList(statement.getValue())));
                }

                probability = ApfloatFactory.fromString(pieces[1].trim());

                String[] vars = pieces[0].split(",");
                for (int i = 0; i < vars.length; i++) {
                    secretTerms.add(TermFactory.buildTerm(vars[i].trim()));
                }

                return new SafetyProperty(secretTerms, probability);
            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        throw new ProtocolParseException(Resources.NO_SECRECY_PROPERTY);
    }

    private static List<Role> parseRoles(String text) throws ProtocolParseException, TermParseException,
            ActionParseException {

        Collection<Statement> statements = extractStatements(text);
        List<Role> roles = new ArrayList<>();

        for (Statement statement : statements) {
            if (statement.getCommand().toLowerCase().startsWith(Commands.ROLE)) {

                List<AtomicProcess> actions = new ArrayList<>();

                String[] actionStrings = statement.getValue().split("\\.");
                for (int i = 0; i < actionStrings.length; i++) {
                    actions.add(ProcessFactory.buildAction(actionStrings[i].trim()));
                }

                roles.add(new Role(actions));

            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        return roles;
    }

    private static String getName(String command) throws ProtocolParseException {

        String[] commandPieces = command.split(" ");

        if (commandPieces.length != 2) {
            throw new ProtocolParseException(Resources.INVALID_COMMAND.evaluate(Collections.singletonList(command)));
        }

        return commandPieces[1];
    }

    private static Collection<Statement> extractStatements(String text) throws ProtocolParseException {

        Scanner scanner = new Scanner(text);
        scanner.useDelimiter(LINE_DELIMITER);

        Collection<Statement> statements = new ArrayList<>();

        while (scanner.hasNext()) {

            String statement = scanner.next();
            String[] statementPieces = statement.split(STATEMENT_DELIMITER);

            if (statementPieces.length != 2) {
                throw new ProtocolParseException(Resources.INVALID_STATEMENT
                        .evaluate(Collections.singletonList(statement.trim())));
            }

            statements.add(new Statement(statementPieces[0].trim(), statementPieces[1].trim()));
        }

        return statements;
    }
}
