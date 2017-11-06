package rewriting.maude;

import cache.RunConfiguration;
import org.apache.commons.lang3.StringUtils;
import rewriting.RewriteEngine;
import rewriting.terms.Term;
import rewriting.terms.TermFactory;
import rewriting.terms.TermParseException;

import java.io.*;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 10/31/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class MaudeEngine implements RewriteEngine {

    private static final String MAUDE_COMMAND = RunConfiguration.getMaudePath();

    private Process process;
    private BufferedWriter writer;
    private ThreadReader reader;
    private Thread threadReader;
    private File theory;
    private String readLine;

    public MaudeEngine() {
        try {
            ProcessBuilder pb = new ProcessBuilder(MAUDE_COMMAND);
            process = pb.start();

            OutputStream stdin = process.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(stdin));

            reader = new ThreadReader(process.getInputStream());
            threadReader = new Thread(reader);

            for (int i = 0; i < 6; i++) {
                threadReader.run();
            }

            theory = MaudeCodec.encodeTheory();

            writer.write(MaudeCodec.encodeLoadTheory(theory));
            writer.flush();

        } catch (IOException ex) {
            // TODO: Shut down
        }
    }

    @Override
    public Term reduce(Term term, boolean useCache) throws ExecutionException, IOException, TermParseException {

        String reduceCommand = MaudeCodec.encodeReduce(term);
        writer.write(reduceCommand);
        writer.flush();

        threadReader.run();
        threadReader.run();
        threadReader.run();
        threadReader.run();

        // TODO: Does the string line differ on different machines??

        // NOTE: if the reduce command is longer that 75 characters
        // it gets split another another output line
        for (int i = 0; i < (reduceCommand.length() / 78); i++) {
            threadReader.run();
        }

        String resultTerm = readLine.substring(readLine.indexOf(":") + 1).trim();

        // NOTE: Another hack to handle when result string is
        // split onto multiple lines
        while (StringUtils.countMatches(resultTerm, "(") != StringUtils.countMatches(resultTerm, ")")) {
            threadReader.run();
            resultTerm = resultTerm + readLine.trim();
        }

        return TermFactory.buildTerm(resultTerm);
    }

    @Override
    public void shutdown() throws IOException {
        writer.close();
        reader.close();
        process.destroy();
    }

    class ThreadReader implements Runnable {

        private BufferedReader reader;

        ThreadReader(InputStream inputStream) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }

        void close() throws IOException {
            reader.close();
        }

        @Override
        public void run() {
            try {
                readLine = reader.readLine();
            } catch (IOException ex) {
                // TODO
            }
        }
    }
}
