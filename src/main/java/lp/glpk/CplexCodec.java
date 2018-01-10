package lp.glpk;

import lp.Constraint;
import lp.PfaEquivLp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/8/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class CplexCodec {

    private static final int LINE_LENGTH = 240;
    private static String sumLeftVars;
    private static String sumRightVars;

    public static File encodeLinearProg(PfaEquivLp lp) throws IOException {

        if (sumLeftVars == null || sumRightVars == null) {
            generateSumVars(true, lp.getNumLeftVars());
            generateSumVars(false, lp.getNumRightVars());
        }

        File lpProblem = File.createTempFile("LP-", "");
        lpProblem.deleteOnExit();

        PrintWriter writer = new PrintWriter(lpProblem, "UTF-8");

        writer.println("Maximize");
        writer.println();
        printLines(writer, "objective: " + lp.getMaximize().toString());
        writer.println();

        writer.println("Subject To");
        writer.println();

        for (Constraint constraint : lp.getConstraints()) {
            printLines(writer, constraint.toString());
        }

        printLines(writer, sumLeftVars);
        writer.println("cc0: x0 = 1");
        printLines(writer, sumRightVars);
        writer.println("cc1: y0 = 1");

        writer.println();
        writer.println("Bounds");
        writer.println();
        printBounds(writer, lp.getNumLeftVars(), lp.getNumRightVars());

        writer.println();
        writer.println("End");

        writer.flush();
        writer.close();

        return lpProblem;
    }

    private static void generateSumVars(boolean leftVars, long num) {

        StringBuilder sb = new StringBuilder();

        String type;
        if (leftVars) {
            type = "x";
            sb.append("cc00: ");
        } else {
            type = "y";
            sb.append("cc11: ");
        }

        sb.append(type);
        sb.append("0");
        for (int i = 1; i < num; i++) {
            sb.append(" + ");
            sb.append(type);
            sb.append(i);
        }
        sb.append(" = 1");

        if (leftVars) {
            CplexCodec.sumLeftVars = sb.toString();
        } else {
            CplexCodec.sumRightVars = sb.toString();
        }
    }

    private static void printBounds(PrintWriter writer, long numLeft, long numRight) {

        for (int i = 0; i < numLeft; i++) {
            writer.println("0 <= x" + i + " <= 1");
        }

        for (int i = 0; i < numRight; i++) {
            writer.println("0 <= y" + i + " <= 1");
        }
    }

    private static void printLines(PrintWriter writer, String string) throws IOException {

        int printed = 0;
        int length = string.length();

        while (printed < length) {

            if (printed + LINE_LENGTH > length) {
                writer.println(string.substring(printed, Math.min(printed + LINE_LENGTH, length)));
                printed = length;
            } else {

                int offset = string.substring(printed, printed + LINE_LENGTH).lastIndexOf(' ');

                if (offset == -1) {
                    throw new IOException("Unable to encode cplex file");
                }
                writer.println(string.substring(printed, printed + offset));
                printed = printed + offset;
            }
        }
    }

    public static boolean decode(String output) throws IOException {

        if (output.contains("CPLEX LP file processing error")) {
            throw new IOException("CPLEX parsing error in GLPK");
        }

        return output.contains("OPTIMAL SOLUTION FOUND");
    }
}
