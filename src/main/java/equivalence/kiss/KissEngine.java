package equivalence.kiss;

import cache.RunConfiguration;
import equivalence.DeductionResult;
import equivalence.EquivalenceEngine;
import equivalence.EquivalenceResult;
import process.State;
import rewriting.Rewrite;
import rewriting.Signature;
import rewriting.terms.Term;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/25/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class KissEngine implements EquivalenceEngine {

    private static final String KISS_COMMAND = RunConfiguration.getKissPath();

    public String invoke(String input) throws InterruptedException, IOException {

        ProcessBuilder pb = new ProcessBuilder(KISS_COMMAND);

        Process process = pb.start();
        OutputStream stdin = process.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();

        InputStream is = process.getInputStream();
        String theString = org.apache.commons.io.IOUtils.toString(is, "ASCII");
        is.close();

        return theString;
    }

    @Override
    public String encode(Signature signature, Collection<Rewrite> rewrites, State state1,
                         State state2, List<Term> frame1Secrets, List<Term> frame2Secrets) {
        return KissCodec.encode(signature, rewrites, state1, state2, frame1Secrets, frame2Secrets);
    }

    @Override
    public List<DeductionResult> decodeDeductionResults(String output) {
        return KissCodec.decodeDeductionResults(output);
    }

    @Override
    public List<EquivalenceResult> decodeEquivalenceResults(String output) {
        return KissCodec.decodeEquivalenceResults(output);
    }
}
