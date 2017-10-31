package cache;

import equivalence.EquivalenceMethod;
import rewriting.RewriteMethod;

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
    private static boolean emptyOutputs;

    private static File protocolFile;
    private static Path dotFile;
    private static EquivalenceMethod equivalenceMethod;
    private static RewriteMethod rewriteMethod;

    private static String kissPath;
    private static String akissPath;
    private static String maudePath;

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
        emptyOutputs = false;
        rewriteMethod = RewriteMethod.INTERNAL;
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

    public static EquivalenceMethod getEquivalenceMethod() {
        return equivalenceMethod;
    }

    public static void setEquivalenceMethod(EquivalenceMethod equivalenceMethod) {
        RunConfiguration.equivalenceMethod = equivalenceMethod;
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

    public static void enableEmptyOutputs() {
        emptyOutputs = true;
    }

    public static boolean containsEmptyOutputs() {
        return emptyOutputs;
    }

    public static void setKissPath(String kissPath) {
        RunConfiguration.kissPath = kissPath;
    }

    public static String getKissPath() {
        return RunConfiguration.kissPath;
    }

    public static void setAkissPath(String akissPath) {
        RunConfiguration.akissPath = akissPath;
    }

    public static String getAkissPath() {
        return akissPath;
    }

    public static void setMaudePath(String maudePath) {
        RunConfiguration.maudePath = maudePath;
    }

    public static String getMaudePath() {
        return maudePath;
    }

    public static void setRewriteMethod(RewriteMethod rewriteMethod) {
        RunConfiguration.rewriteMethod = rewriteMethod;
    }

    public static RewriteMethod getRewriteMethod() {
        return rewriteMethod;
    }
}
