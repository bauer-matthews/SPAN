package parser;

import log.Console;
import log.Severity;
import parser.cli.OptionsParser;
import parser.protocol.ProtocolParseException;
import parser.protocol.ProtocolParser;
import protocol.Protocol;
import protocol.role.ActionParseException;
import rewriting.terms.TermParseException;

/**
 * SPAN - Stochastic Protocol Analyzer
 *
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Parser {

    public static void parseOptions(String[] args) {
        OptionsParser.parse(args);
    }

    public static Protocol parseProtocol() {

        try {
            return ProtocolParser.parse();
        } catch (ProtocolParseException | TermParseException | ActionParseException ex) {
            Console.printError(Severity.ERROR, ex.getMessage());
            System.exit(1);
            return null;
        }
    }
}
