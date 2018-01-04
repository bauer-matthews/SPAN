package parser.protocol;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import equivalence.EquivalenceMethod;
import log.Console;
import log.Severity;
import org.apfloat.Aprational;
import protocol.*;
import protocol.role.ActionParseException;
import protocol.role.AtomicProcess;
import protocol.role.ProcessFactory;
import protocol.role.Role;
import rewriting.Rewrite;
import rewriting.RewriteMethod;
import rewriting.Signature;
import rewriting.terms.*;
import util.CollectionUtils;
import util.ExitCode;
import util.Pair;
import util.aprational.AprationalFactory;

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

    public static void parse() throws ProtocolParseException, TermParseException, ActionParseException {

        File protocolFile = RunConfiguration.getProtocolFile();

        Map<Section, String> sectionText = parseSections(protocolFile);

        Metadata metadata = parseMetadata(sectionText.get(Section.METADATA));

        if (metadata.getProtocolType().equals(ProtocolType.REACHABILITY)) {

            ReachabilityProtocol protocol = new ProtocolBuilder()
                    .metadata(metadata)
                    .fractionConstants(parseFractionConstants(sectionText.get(Section.CONSTANTS)))
                    .signature(parseSignature(sectionText.get(Section.SIGNATURE)))
                    .rewrites(parseRewrites(sectionText.get(Section.REWRITES)))
                    .roles(parseRoles(sectionText.get(Section.ROLES)))
                    .safetyProperty(parseSafetyProperties(sectionText.get(Section.SAFETY)))
                    .buildReachabilityProtocol();

            GlobalDataCache.setReachabilityProtocol(protocol);
            validateReachabilityProtocol();

        } else {

            IndistinguishabilityProtocol protocol = new ProtocolBuilder()
                    .metadata(metadata)
                    .fractionConstants(parseFractionConstants(sectionText.get(Section.CONSTANTS)))
                    .signature(parseSignature(sectionText.get(Section.SIGNATURE)))
                    .rewrites(parseRewrites(sectionText.get(Section.REWRITES)))
                    .roles(parseRoles(sectionText.get(Section.ROLES1)))
                    .roles2(parseRoles(sectionText.get(Section.ROLES2)))
                    .buildIndistinguishabilityProtocol();

            GlobalDataCache.setIndistinguishabilityProtocol(protocol);
            validateIndistinguishabilityProtocol();
        }
    }

    private static void validateReachabilityProtocol() throws ProtocolParseException {

            validateXOR();
    }

    private static void validateIndistinguishabilityProtocol() throws ProtocolParseException {

            validateXOR();
    }

    private static void validateXOR() throws ProtocolParseException {

        if(GlobalDataCache.getMetadata().isXOR()) {

            if (!RunConfiguration.getRewriteMethod().equals(RewriteMethod.MAUDE)) {
                throw new ProtocolParseException("Maude must be enabled to use XOR");
            }

            if (!RunConfiguration.getEquivalenceMethod().equals(EquivalenceMethod.AKISS)) {
                throw new ProtocolParseException("Akiss must be enabled to use XOR");
            }

            for (FunctionSymbol functionSymbol : GlobalDataCache.getSignature().getFunctions()) {
                if (functionSymbol.getSymbol().equalsIgnoreCase("plus")) {
                    throw new ProtocolParseException("The function symbol \"plus\" is " +
                            "reserved when the XOR option is enabled");
                }

                if (functionSymbol.getSymbol().equalsIgnoreCase("one")) {
                    throw new ProtocolParseException("The function symbol \"one\" is " +
                            "reserved when the XOR option is enabled");
                }

                if (functionSymbol.getSymbol().equalsIgnoreCase("zero")) {
                    throw new ProtocolParseException("The function symbol \"zero\" is " +
                            "reserved when the XOR option is enabled");
                }
            }

            for (VariableTerm variableTerm : GlobalDataCache.getSignature().getVariables()) {

                if (variableTerm.getName().equalsIgnoreCase("b1") ||
                        variableTerm.getName().equalsIgnoreCase("b2")) {

                    throw new ProtocolParseException("The variable names \"b1\" and \"b2\" are " +
                            "reserved when the XOR option is enabled");
                }

            }
        }
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

                    if (!line.trim().startsWith("#")) {
                        stringBuilder.append(line);
                    }
                }

                line = reader.readLine();
            }

            if (section != null) {
                sectionText.put(section, stringBuilder.toString());
            }

        } catch (IOException ex) {
            Console.printError(Severity.ERROR, ex.getMessage());
            System.exit(ExitCode.PROTOCOL_PARSE_ERROR.getValue());
        }

        // TODO: Re-enable
        //List<Section> sectionList = Arrays.asList(Section.values());
        //if (!sectionText.keySet().containsAll(sectionList)) {
        //    throw new ProtocolParseException(Resources.MISSING_INVALID_SECTIONS);
        //}

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
            case "roles1":
                return Section.ROLES1;
            case "roles2":
                return Section.ROLES2;
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
        boolean enableXOR = false;
        ProtocolType protocolType = ProtocolType.REACHABILITY;

        for (Statement statement : statements) {

            if (statement.getCommand().trim().equalsIgnoreCase(Commands.XOR)) {
                if (statement.getValue().trim().equalsIgnoreCase("yes")) {
                    enableXOR = true;
                    RunConfiguration.enableXOR();
                }
            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.EQUIV)) {
                if (statement.getValue().trim().equalsIgnoreCase("yes")) {
                    protocolType = ProtocolType.INDISTINGUISHABILITY;
                }
            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.VERSION)) {

                version = statement.getValue();

            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.RECIPE_DEPTH)) {

                try {
                    recipeSize = Integer.parseInt(statement.getValue().trim());
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException(Resources.INVALID_RECIPE_SIZE
                            .evaluate(Collections.singletonList(statement.getValue().trim())));
                }

            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.REWRITING_CACHE)) {

                try {
                    RunConfiguration.setRewritingCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Rewriting Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.EQUIVALENCE_CACHE)) {

                try {
                    RunConfiguration.setEquivalenceCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Equivalence Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.UNIFICATION_CACHE)) {

                try {
                    RunConfiguration.setUnificationCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Unification Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.SUBSTITUTION_CACHE)) {

                try {
                    RunConfiguration.setSubstitutionCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Substitution Cache Size: " + statement.getValue().trim());
                }

            } else if (statement.getCommand().trim().equalsIgnoreCase(Commands.ACTION_FACTORY_CACHE)) {

                try {
                    RunConfiguration.setActionFactoryCacheSize(Integer.parseInt(statement.getValue().trim()));
                } catch (NumberFormatException ex) {
                    throw new ProtocolParseException("Invalid Action Factory Cache Size: " + statement.getValue().trim());
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

        GlobalDataCache.setProtocolType(protocolType);
        return new Metadata(version, recipeSize.intValue(), enableXOR, protocolType);
    }

    private static Map<String, Aprational> parseFractionConstants(String text) throws ProtocolParseException,
            NumberFormatException {

        Collection<Statement> statements = extractStatements(text);

        Map<String, Aprational> constantMap = new HashMap<>();
        for (Statement statement : statements) {

            if (statement.getCommand().toLowerCase().startsWith(Commands.FRACTION)) {
                constantMap.put(getName(statement.getCommand()),
                        AprationalFactory.fromString(statement.getValue().trim()));
            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        ProcessFactory.initFractionConstants(constantMap);

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

                if (sortPieces.length == 1 && sortPieces[0].trim().isEmpty()) {
                    // no parameters
                } else {

                    for (String sortString : sortPieces) {
                        sortParameters.add(SortFactory.fromString(sortString.trim()));
                    }
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

        if (publicNames.size() == 0) {
            throw new ProtocolParseException("At least one public name is required.");
        }

        Signature signature = new Signature(functions, publicNames, privateNames, variables, sorts, sortOrders);
        validateSignature(signature);

        TermFactory.initTermBuilder(signature);
        return signature;
    }

    private static void validateSignature(Signature signature) throws ProtocolParseException {


        List<String> symbols = new ArrayList<>();

        signature.getFunctions().forEach(functionSymbol -> symbols.add(functionSymbol.getSymbol()));
        signature.getPrivateNames().forEach(nameTerm -> symbols.add(nameTerm.getName()));
        signature.getPublicNames().forEach(nameTerm -> symbols.add(nameTerm.getName()));
        signature.getVariables().forEach(variableTerm -> symbols.add(variableTerm.getName()));

        checkForReservedStrings(symbols);
    }

    private static void checkForReservedStrings(List<String> symbols) throws ProtocolParseException {

        for (String symbol : symbols) {
            for (String reservedString : TermFactory.getReservedStrings()) {
                if (symbol.contains(reservedString)) {
                    throw new ProtocolParseException("Use of a reserved symbol: " + symbol);
                }
            }
        }
    }

    private static Collection<Rewrite> parseRewrites(String text) throws ProtocolParseException, TermParseException {

        Collection<Statement> statements = extractStatements(text);
        Collection<Rewrite> rewrites = new ArrayList<>();
        List<VariableTerm> rewriteVariables = new ArrayList<>();

        for (Statement statement : statements) {
            if (statement.getCommand().toLowerCase().startsWith(Commands.REWRITE)) {

                String[] rule = statement.getValue().split("->");

                if (rule.length != 2) {
                    throw new ProtocolParseException(Resources.INVALID_REWRITE_RULE
                            .evaluate(Collections.singletonList(statement.getValue())));
                }

                Term lhs = TermFactory.buildTerm(rule[0].trim());
                Term rhs = TermFactory.buildTerm(rule[1].trim());

                rewriteVariables.addAll(lhs.getVariables());
                rewriteVariables.addAll(rhs.getVariables());

                rewrites.add(new Rewrite(lhs, rhs));

            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        ProcessFactory.initRewriteVariables(rewriteVariables);

        return rewrites;
    }

    private static SafetyProperty parseSafetyProperties(String text) throws TermParseException,
            ProtocolParseException {

        Collection<Statement> statements = extractStatements(text);
        List<Term> secretTerms = new ArrayList<>();

        Aprational probability = null;

        for (Statement statement : statements) {
            if (statement.getCommand().toLowerCase().startsWith(Commands.SECRECY)) {

                String[] pieces = statement.getValue().split(Resources.SECRECY_PROP_DELIMITER);

                if (pieces.length != 2) {
                    throw new ProtocolParseException(Resources.INVALID_SECRECY_PROPERTY
                            .evaluate(Collections.singletonList(statement.getValue())));
                }

                probability = AprationalFactory.fromString(pieces[1].trim());

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

        parseSubRoles(text);

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

            } else if (statement.getCommand().toLowerCase().startsWith(Commands.SUBROLE)) {
                //already handled
            } else {
                Console.printMessage(Severity.WARNING, Resources.UNRECOGNIZED_COMMAND
                        .evaluate(Collections.singletonList(statement.getCommand())));
            }
        }

        return validateRoles(roles);
    }

    private static void parseSubRoles(String text)
            throws ProtocolParseException, TermParseException, ActionParseException {

        Collection<Statement> statements = extractStatements(text);
        Collection<Statement> remainingStatements = new ArrayList<>();

        Map<String, Role> subroleMap = new HashMap<>();
        ProcessFactory.setSubRolesMap(subroleMap);

        for (Statement statement : statements) {

            if (statement.getCommand().toLowerCase().startsWith(Commands.SUBROLE)) {

                if (getSubRoleStrings(statement.getValue()).isEmpty()) {

                    Pair<String, Role> parsedSubRole = parseSubrole(statement);
                    subroleMap.put(parsedSubRole.getKey(), parsedSubRole.getValue());
                } else {
                    remainingStatements.add(statement);
                }
            }
        }

        ProcessFactory.setSubRolesMap(subroleMap);

        boolean progress = true;
        while ((!remainingStatements.isEmpty()) && progress) {

            progress = false;

            for (Statement statement : remainingStatements) {
                List<String> subroleStrings = getSubRoleStrings(statement.getValue());

                boolean canParse = true;
                for (String subroleString : subroleStrings) {
                    if (subroleMap.get(subroleString) == null) {
                        canParse = false;
                    }
                }

                if (canParse) {
                    progress = true;
                    Pair<String, Role> parsedSubRole = parseSubrole(statement);
                    subroleMap.put(parsedSubRole.getKey(), parsedSubRole.getValue());
                    ProcessFactory.setSubRolesMap(subroleMap);
                    remainingStatements.remove(statement);
                    break;
                }
            }
        }

        ProcessFactory.setSubRolesMap(subroleMap);
    }

    private static Pair<String, Role> parseSubrole(Statement statement)
            throws TermParseException, ActionParseException, ProtocolParseException {

        List<AtomicProcess> actions = new ArrayList<>();
        String[] actionStrings = statement.getValue().split("\\.");

        for (String actionString : actionStrings) {
            actions.add(ProcessFactory.buildAction(actionString.trim()));
        }

        if ((!statement.getCommand().contains("(")) || (!statement.getCommand().contains(")"))) {
            throw new ProtocolParseException("Subroles must be labeled with name");
        }

        String roleName = statement.getCommand().substring(statement.getCommand().indexOf("(") + 1,
                statement.getCommand().lastIndexOf(")")).trim();

        return new Pair<>(roleName, new Role(actions));
    }

    private static List<String> getSubRoleStrings(String role) {

        List<String> subroleStrings = new ArrayList<>();
        String[] actionStrings = role.split("\\.");

        for (String actionString : actionStrings) {
            if (!actionString.contains("in")) {

                String[] probOutputs = actionString.split("\\+");

                for (String probOutput : probOutputs) {

                    if (probOutput.contains("#")) {
                        String[] subRoleChunk = probOutput.split("#");
                        String subRole = subRoleChunk[1].replaceAll("\\)", "").trim();
                        subroleStrings.add(subRole);
                    }
                }
            }
        }

        return subroleStrings;
    }

    private static List<Role> validateRoles(List<Role> roles) throws ProtocolParseException {

        validateNoMultipleBindings(roles);
        validateOutputVarsBound(roles);

        if (roles.size() == 1) {
            return roles;
        }

        Collection<VariableTerm> reservedVariables = roles.get(0).getVariables();

        for (int i = 1; i < roles.size(); i++) {

            Collection<VariableTerm> newVariables = roles.get(i).getVariables();

            if (!CollectionUtils.intersection(newVariables, reservedVariables).isEmpty()) {
                throw new ProtocolParseException("Variables cannot be shared across roles");
            }

            reservedVariables.addAll(newVariables);
        }

        return roles;
    }

    private static void validateNoMultipleBindings(List<Role> roles) throws ProtocolParseException {

        for (int i = 0; i < roles.size(); i++) {

            // TODO: Fix - This ensures nothing!
            Role modifiedRole = roles.get(i).appendBranchIndexToVars(GlobalDataCache.getFreshBranchIndex());
            List<VariableTerm> variableTerms = modifiedRole.getInputVariables();
            Set<VariableTerm> uniqueVariableTerms = new HashSet<>(variableTerms);

            if (variableTerms.size() != uniqueVariableTerms.size()) {
                throw new ProtocolParseException("Role " + i + " binds the same variable multiple times");
            }
        }
    }

    private static void validateOutputVarsBound(List<Role> roles) throws ProtocolParseException {

        for (int i = 0; i < roles.size(); i++) {

            List<VariableTerm> inputVariables = roles.get(i).getInputVariables();

            for (VariableTerm outputVariable : roles.get(i).getOutputVariables()) {
                if (!inputVariables.contains(outputVariable)) {
                    throw new ProtocolParseException("Role " + i + " contains an unbound variable: "
                            + outputVariable.toMathString());
                }
            }
        }
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
