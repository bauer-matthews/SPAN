package dot;

import attacker.AttackTree;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
        List<String> lines = attackTree.getDotLines();
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
}
