package kiss;

import sun.misc.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/25/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class Kiss {

    public static String invokeKiss(String command, String input) throws InterruptedException, IOException {

        ProcessBuilder pb = new ProcessBuilder(command);

        Process process = pb.start();
        OutputStream stdin = process.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        writer.write(input);
        writer.newLine();
        writer.flush();
        writer.close();

        InputStream is = process.getInputStream();
        String theString = org.apache.commons.io.IOUtils.toString(is,"ASCII");
        is.close();

        return theString;
    }

    public static List<DeductionResult> getDeductionResults(String output) {
        List<DeductionResult> results = new ArrayList<>();

        String[] pieces = output.split("\n");

        for(int i=0; i < pieces.length; i++) {

            String out = pieces[i];

            if(out.contains(Resources.DEDUCTION_ID)) {
                if(out.startsWith(Resources.TRUE)) {
                    String values = out.substring(out.indexOf(Resources.TRUE) + 3,

                            out.indexOf(Resources.RECIPE_ID)).trim();
                    String[] valuePieces = values.split(Resources.DEDUCTION_ID_REGEX);

                    results.add(new DeductionResult(valuePieces[1].trim(), true, valuePieces[0].trim(),
                            Optional.of(out.substring(out.indexOf(Resources.RECIPE_ID)+2))));

                } else {
                    String values = out.substring(out.indexOf(Resources.FALSE) + 3).trim();

                    String[] valuePieces = values.split(Resources.DEDUCTION_ID_REGEX);

                    results.add(new DeductionResult(valuePieces[1].trim(), false, valuePieces[0].trim(),
                            Optional.empty()));
                }
            }
        }

        return results;
    }

    public static List<EquivalenceResult> getEquivalenceResults(String output) {
        List<EquivalenceResult> results = new ArrayList<>();

        String[] pieces = output.split("\n");

        for(int i=0; i < pieces.length; i++) {

            String out = pieces[i];
            if(out.contains(Resources.EQUIVALENCE_ID)) {
                if(out.startsWith(Resources.TRUE)) {

                    String values = out.substring(out.indexOf(Resources.TRUE) + 3).trim();
                    String[] valuePieces = values.split(Resources.EQUIVALENCE_ID);
                    results.add(new EquivalenceResult(valuePieces[0].trim(), valuePieces[1].trim(), true));

                } else {

                    String values = out.substring(out.indexOf(Resources.FALSE) + 3).trim();
                    String[] valuePieces = values.split(Resources.EQUIVALENCE_ID);
                    results.add(new EquivalenceResult(valuePieces[0].trim(), valuePieces[1].trim(), false));
                }
            }

        }

        return results;
    }

}
