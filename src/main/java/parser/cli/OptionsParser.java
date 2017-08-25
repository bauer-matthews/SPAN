package parser.cli;

import org.apache.commons.cli.*;
import util.ExitCode;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class OptionsParser {

    public static void parse(String[] args) {

        CommandLineParser parser = new DefaultParser();

        // First parse the option list of help/empty to avoid conflicts with required options
        try {

            CommandLine cmd = parser.parse(CLIOptions.getOptions(), args, false);
            List<Option> optionsList = Arrays.asList(cmd.getOptions());

            if (optionsList.isEmpty()) {
                CLIOptions.printUsage();
                System.exit(ExitCode.OPTION_ERROR.getValue());
            }

            for (Option option : CLIOptions.getHelpOptions().getOptions()) {
                if (optionsList.contains(option)) {
                    CLIOptions.printUsage();
                    System.exit(ExitCode.OPTION_ERROR.getValue());
                }
            }
        } catch (ParseException ex) {
            // Bury exception, parse next options group
        }

        try {
            CommandLine cmd = parser.parse(CLIOptions.getOptions(), args);
            initializeRunConfiguration(cmd);
        } catch (ParseException ex) {
            CLIOptions.printParseError(ex);
            CLIOptions.printUsage();
            System.exit(ExitCode.OPTION_ERROR.getValue());
        }
    }

    private static void initializeRunConfiguration(CommandLine cmd) {
        List<Option> options = Arrays.asList(cmd.getOptions());
        Consumer optionConsumer = CLIOptions.getOptionConsumer();

        for (Option option : options) {
            optionConsumer.accept(option);
        }
    }
}
