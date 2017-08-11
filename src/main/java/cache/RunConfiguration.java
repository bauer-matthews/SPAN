package cache;

import com.google.common.base.MoreObjects;
import kiss.DeductionResult;
import process.EquivalenceChecker;

import java.io.File;
import java.text.ParseException;
import java.util.Objects;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 5/22/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class RunConfiguration {

    private static boolean debug;
    private static boolean trace;

    private static File protocolFile;
    private static EquivalenceChecker.EquivalenceMethod equivalenceMethod;


    static {
        // Defaults
        debug = false;
        trace = false;
        equivalenceMethod = EquivalenceChecker.EquivalenceMethod.KISS;
    }

    public static void enableDebug() {
        debug = true;
    }

    public static void enableTrace() {
        trace = true;
    }

    public static boolean getDebug() {
        return debug;
    }

    public static boolean getTrace() {
        return trace;
    }

    public static void setProtocolFile(File protocolFileValue) {
        protocolFile = protocolFileValue;
    }

    public static File getProtocolFile() {
        return protocolFile;
    }

    public static EquivalenceChecker.EquivalenceMethod getEquivalenceMethod() {
        return equivalenceMethod;
    }
}
