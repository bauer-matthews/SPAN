package lp.glpk;

import cache.RunConfiguration;

import java.io.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/5/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class GlpkEngine {

    private static final String GLPK_COMMAND = RunConfiguration.getGlpkPath();

    public static String invoke(File inputFile) throws InterruptedException, IOException {

        ProcessBuilder pb = new ProcessBuilder(GLPK_COMMAND, "--cpxlp", inputFile.getAbsolutePath());
        Process process = pb.start();

        InputStream is = process.getInputStream();
        String theString = org.apache.commons.io.IOUtils.toString(is, "ASCII");
        is.close();

        process.destroy();

        return theString;
    }
}
