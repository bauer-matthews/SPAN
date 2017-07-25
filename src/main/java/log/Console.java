package log;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/24/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Console {

    public static void printMessage(Severity severity, String message) {
        System.out.println(severity.toString() + ": " + message);
    }

    public static void printError(Severity severity, String message) {
        System.err.println(severity.toString() + ": " + message);
    }
}
