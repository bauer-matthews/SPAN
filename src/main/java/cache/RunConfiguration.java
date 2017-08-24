package cache;

import process.EquivalenceChecker;

import java.io.File;
import java.util.Optional;

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
    private static boolean printAttack;

    private static File protocolFile;
    private static EquivalenceChecker.EquivalenceMethod equivalenceMethod;

    private static Integer equivalenceCacheSize;
    private static Integer RewritingCacheSize;
    private static Integer SubstitutionCacheSize;
    private static Integer UnificationCacheSize;

    static {
        // Defaults
        debug = false;
        trace = false;
        printAttack = false;
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

    public static void setEquivalenceCacheSize(Integer equivalenceCacheSize) {
        RunConfiguration.equivalenceCacheSize = equivalenceCacheSize;
    }

    static Optional<Integer> getDefaultEquivalenceCacheSize() {
        return Optional.ofNullable(equivalenceCacheSize);
    }

    static Optional<Integer> getRewritingCacheSize() {
        return Optional.ofNullable(RewritingCacheSize);
    }

    public static void setRewritingCacheSize(Integer rewritingCacheSize) {
        RewritingCacheSize = rewritingCacheSize;
    }

    static Optional<Integer> getSubstitutionCacheSize() {
        return Optional.ofNullable(SubstitutionCacheSize);
    }

    public static void setSubstitutionCacheSize(Integer substitutionCacheSize) {
        SubstitutionCacheSize = substitutionCacheSize;
    }

    static Optional<Integer> getUnificationCacheSize() {
        return Optional.ofNullable(UnificationCacheSize);
    }

    public static void setUnificationCacheSize(Integer unificationCacheSize) {
        UnificationCacheSize = unificationCacheSize;
    }

    public static void enableAttackPrinting() {
        printAttack = true;
    }

    public static boolean printAttack() {
        return printAttack;
    }
}
