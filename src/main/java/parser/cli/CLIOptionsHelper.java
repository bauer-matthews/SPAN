package parser.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import util.ExitCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static parser.cli.CLIOptions.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class CLIOptionsHelper {

    private static final Options options = new Options();
    private static final Options helpOptions = new Options();
    private static final Map<Option, Consumer<Option>> optionMap = new HashMap<>();

    static {
        addHelpOptions(options, helpOptions);
        addMainOptions(options);
    }

    static Options getOptions() {
        return options;
    }

    static Options getHelpOptions() {
        return helpOptions;
    }

    static Consumer<Option> getOptionConsumer() {
        return option -> {

            if (optionMap.get(option) == null) {
                printParseError(new ParseException("Unrecognized option :" + option.getOpt()));
                CLIOptionsHelper.printUsage();
                System.exit(ExitCode.OPTION_ERROR.getValue());
            } else {
                optionMap.get(option).accept(option);
            }
        };
    }

    static void printParseError(ParseException ex) {
        System.err.println(ex.getMessage());
    }

    static void printUsage() {
        new HelpFormatter().printHelp("span", getOptions());
    }

    private static void addHelpOptions(Options options, Options helpOptions) {
        Option help = new Option("help", "print this message");
        options.addOption(help);
        helpOptions.addOption(help);
    }

    private static void addMainOptions(Options options) {

        // Debug Option
        options.addOption(debug);
        optionMap.put(debug, debugConsumer);

        // Trace Option
        options.addOption(trace);
        optionMap.put(trace, traceConsumer);

        // Attack Tree Option
        options.addOption(attackTree);
        optionMap.put(attackTree, attackTreeConsumer);

        // Maximum Attack Prob Option
        options.addOption(maxAttack);
        optionMap.put(maxAttack, maxAttackConsumer);

        // KISS engine
        options.addOption(kiss);
        optionMap.put(kiss, kissConsumer);

        // AKISS engine
        options.addOption(akiss);
        optionMap.put(akiss, akissConsumer);

        // Maude
        options.addOption(maude);
        optionMap.put(maude, maudeConsumer);

        // Dot Output Option
        options.addOption(dot);
        optionMap.put(dot, dotConsumer);

        // Protocol Option
        options.addOption(protocol);
        optionMap.put(protocol, protocolConsumer);
    }

    static void validateOptions(List<Option> options) throws ParseException{
        validateEquivalenceOptions(options);
    }

    private static void validateEquivalenceOptions(List<Option> options) throws ParseException {

        int equivOptCount = 0;
        for(Option option : options) {

            if(option.equals(kiss)) {
                equivOptCount++;
            }

            if(option.equals(akiss)) {
                equivOptCount++;
            }
        }

        if(equivOptCount != 1) {
            throw new ParseException("Exactly one equivalence checker option must be present");
        }
    }
}
