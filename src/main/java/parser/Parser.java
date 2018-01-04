package parser;

import log.Console;
import log.Severity;
import parser.cli.OptionsParser;
import parser.protocol.ProtocolParseException;
import parser.protocol.ProtocolParser;
import protocol.role.ActionParseException;
import rewriting.terms.TermParseException;
import util.ExitCode;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Parser {

    public static void parseOptions(String[] args) {
        OptionsParser.parse(args);
    }

    public static void parseProtocol() {

        try {
            ProtocolParser.parse();
        } catch (ProtocolParseException | TermParseException | ActionParseException ex) {
            Console.printError(Severity.ERROR, ex.getMessage());
            System.exit(ExitCode.PROTOCOL_PARSE_ERROR.getValue());
        }
    }
}
