package configuration;

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
    private static File protocolFile;

    static {
        debug = false;
    }

    public static void setDebug(boolean debugValue) {
        debug = debugValue;
    }

    public static boolean getDebug() {
        return debug;
    }

    public static void setProtocolFile(File protocolFileValue) {
        protocolFile = protocolFileValue;
    }

    public static File getProtocolFile() {
        return protocolFile;
    }

    public static EquivalenceChecker.EquivalenceMethod getEquivalenceMethod() {
        return EquivalenceChecker.EquivalenceMethod.KISS;
    }
}
