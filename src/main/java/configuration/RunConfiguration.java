package configuration;

import java.io.File;
import java.text.ParseException;

/**
 * SPAN - Stochastic Protocol Analyzer
 *
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

}
