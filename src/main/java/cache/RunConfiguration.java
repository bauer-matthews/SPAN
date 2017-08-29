package cache;

import process.EquivalenceChecker;

import java.io.File;
import java.nio.file.Path;
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
    private static boolean findMaxAttack;
    private static boolean outputToDot;
    private static boolean consecutiveOutputReduction;

    private static File protocolFile;
    private static Path dotFile;
    private static EquivalenceChecker.EquivalenceMethod equivalenceMethod;

    private static Integer equivalenceCacheSize;
    private static Integer rewritingCacheSize;
    private static Integer substitutionCacheSize;
    private static Integer unificationCacheSize;
    private static Integer actionFactoryCacheSize;

    static {
        // Defaults
        debug = false;
        trace = false;
        outputToDot = false;
        printAttack = false;
        findMaxAttack = false;
        consecutiveOutputReduction = true;
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

    public static void setDotFile(Path dotFile) {
        outputToDot = true;
        RunConfiguration.dotFile = dotFile;
    }

    public static Path getDotFile() {
        return dotFile;
    }

    public static boolean outputToDot() {
        return outputToDot;
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
        return Optional.ofNullable(rewritingCacheSize);
    }

    public static void setRewritingCacheSize(Integer rewritingCacheSize) {
        RunConfiguration.rewritingCacheSize = rewritingCacheSize;
    }

    static Optional<Integer> getSubstitutionCacheSize() {
        return Optional.ofNullable(substitutionCacheSize);
    }

    public static void setSubstitutionCacheSize(Integer substitutionCacheSize) {
        RunConfiguration.substitutionCacheSize = substitutionCacheSize;
    }

    public static Optional<Integer> getActionFactoryCacheSize() {
        return Optional.ofNullable(actionFactoryCacheSize);
    }

    public static void setActionFactoryCacheSize(Integer actionFactoryCacheSize) {
        RunConfiguration.actionFactoryCacheSize = actionFactoryCacheSize;
    }

    static Optional<Integer> getUnificationCacheSize() {
        return Optional.ofNullable(unificationCacheSize);
    }

    public static void setUnificationCacheSize(Integer unificationCacheSize) {
        RunConfiguration.unificationCacheSize = unificationCacheSize;
    }

    public static void enableAttackPrinting() {
        printAttack = true;
    }

    public static boolean printAttack() {
        return printAttack;
    }

    public static void enableMaxAttack() {
        findMaxAttack = true;
    }

    public static boolean findMaxAttack() {
        return findMaxAttack;
    }

    public static void disableConsecutiveOutputReduction() {
        consecutiveOutputReduction = false;
    }

    public static boolean useConsecutiveOutputReduction() {
        return consecutiveOutputReduction;
    }
}
