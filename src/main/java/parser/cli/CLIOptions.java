package parser.cli;

import cache.RunConfiguration;
import equivalence.EquivalenceMethod;
import org.apache.commons.cli.Option;
import rewriting.RewriteMethod;
import util.ExitCode;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 10/30/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class CLIOptions {

    // Debug Option
    static Option debug = new Option("debug", "print debugging information");
    static Consumer<Option> debugConsumer = (debugOpt) -> RunConfiguration.enableDebug();

    // Trace Option
    static Option trace = new Option("trace", "print path exploration");
    static Consumer<Option> traceConsumer = (traceOpt) -> RunConfiguration.enableTrace();

    // Attack Tree Option
    static Option attackTree = new Option("attackTree", "print the attack tree");
    static Consumer<Option> attackTreeConsumer = (attackOpt) -> RunConfiguration.enableAttackPrinting();

    // Maximum Attack Prob Option
    static Option maxAttack = new Option("maxAttack", "find the maximum attack probability");
    static Consumer<Option> maxAttackConsumer = (maxAttackOpt) -> RunConfiguration.enableMaxAttack();

    // KISS engine
    static Option kiss = Option.builder("kiss")
            .desc("path to KISS engine")
            .hasArg()
            .argName("location")
            .required(false)
            .build();
    static Consumer<Option> kissConsumer = (kissOpt) -> {
        RunConfiguration.setKissPath(kissOpt.getValue());
        RunConfiguration.setEquivalenceMethod(EquivalenceMethod.KISS);
    };

    // AKISS engine
    static Option akiss = Option.builder("akiss")
            .desc("path to AKISS engine")
            .hasArg()
            .argName("location")
            .required(false)
            .build();
    static Consumer<Option> akissConsumer = (akissOpt) -> {
        RunConfiguration.setAkissPath(akissOpt.getValue());
        RunConfiguration.setEquivalenceMethod(EquivalenceMethod.AKISS);
    };

    // MAUDE engine
    static Option maude = Option.builder("maude")
            .desc("path to maude engine")
            .hasArg()
            .argName("location")
            .required(false)
            .build();
    static Consumer<Option> maudeConsumer = (maudeOpt) -> {
        RunConfiguration.setMaudePath(maudeOpt.getValue());
        RunConfiguration.setRewriteMethod(RewriteMethod.MAUDE);
    };

    // Dot Output Option
    static Option dot = Option.builder("dot")
            .desc("output attack tree to dot file")
            .hasArg()
            .argName("file")
            .required(false)
            .build();
    static Consumer<Option> dotConsumer = (dotOpt) -> {
        Path path = Paths.get(dotOpt.getValue());
        RunConfiguration.setDotFile(path);
    };

    // Protocol Option
    static Option protocol = Option.builder("protocol")
            .desc("user specified protocol file")
            .hasArg()
            .argName("file")
            .required()
            .build();
    static Consumer<Option> protocolConsumer = (protocolOpt) -> {
        File protocolFile = new File(protocolOpt.getValue());
        if (!protocolFile.exists() || protocolFile.isDirectory()) {
            System.err.println("Invalid file path argument for option: " + protocolOpt.getOpt());
            CLIOptionsHelper.printUsage();
            System.exit(ExitCode.OPTION_ERROR.getValue());
        }
        RunConfiguration.setProtocolFile(protocolFile);
    };
}
