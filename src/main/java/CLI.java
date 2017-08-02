import org.apache.commons.cli.*;
import parser.*;
import parser.Parser;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class CLI {

    public static void main(String[] args) {

        Parser.parseOptions(args);
        Parser.parseProtocol();

    }
}
