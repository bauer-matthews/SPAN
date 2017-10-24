package parser.cli;

import cache.RunConfiguration;
import org.apache.commons.cli.*;
import util.ExitCode;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class CLIOptions {

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
                CLIOptions.printUsage();
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
        Option debug = new Option("debug", "print debugging information");
        options.addOption(debug);
        optionMap.put(debug, (debugOpt) -> RunConfiguration.enableDebug());

        // Trace Option
        Option trace = new Option("trace", "print path exploration");
        options.addOption(trace);
        optionMap.put(trace, (traceOpt) -> RunConfiguration.enableTrace());

        // Attack Tree Option
        Option attackTree = new Option("attackTree", "print the attack tree");
        options.addOption(attackTree);
        optionMap.put(attackTree, (attackOpt) -> RunConfiguration.enableAttackPrinting());

        // Maximum Attack Prob Option
        Option maxAttack = new Option("maxAttack", "find the maximum attack probability");
        options.addOption(maxAttack);
        optionMap.put(maxAttack, (maxAttackOpt) -> RunConfiguration.enableMaxAttack());

        // KISS engine
        Option kiss = Option.builder("kiss")
                .desc("path to KISS engine")
                .hasArg()
                .argName("location")
                .required(false)
                .build();
        options.addOption(kiss);
        optionMap.put(kiss, (kissOpt) -> RunConfiguration.setKissPath(kissOpt.getValue()));

        // Dot Output Option
        Option dot = Option.builder("dot")
                .desc("output attack tree to dot file")
                .hasArg()
                .argName("file")
                .required(false)
                .build();
        options.addOption(dot);

        optionMap.put(dot, (dotOpt) -> {
            Path path = Paths.get(dotOpt.getValue());
            RunConfiguration.setDotFile(path);
        });

        // Protocol Option
        Option protocol = Option.builder("protocol")
                .desc("user specified protocol file")
                .hasArg()
                .argName("file")
                .required()
                .build();
        options.addOption(protocol);

        optionMap.put(protocol, (protocolOpt) -> {
            File protocolFile = new File(protocolOpt.getValue());
            if (!protocolFile.exists() || protocolFile.isDirectory()) {
                System.err.println("Invalid file path argument for option: " + protocolOpt.getOpt());
                printUsage();
                System.exit(ExitCode.OPTION_ERROR.getValue());
            }
            RunConfiguration.setProtocolFile(protocolFile);
        });
    }
}
