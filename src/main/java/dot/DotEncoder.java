package dot;

import attacker.AttackTree;
import log.Console;
import log.Severity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/25/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class DotEncoder {

    public static void printToDotFile(Path file, AttackTree attackTree) throws IOException {

        try {
            List<String> lines = attackTree.getDotLines();
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (ExecutionException ex) {
            Console.printError(Severity.ERROR, "Unable to print attack tree to dot file.");
        }
    }
}
