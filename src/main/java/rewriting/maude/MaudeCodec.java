package rewriting.maude;

import cache.GlobalDataCache;
import com.google.common.base.Joiner;
import rewriting.Rewrite;
import rewriting.terms.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 10/31/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class MaudeCodec {

    private static final Joiner SPACE_JOINER = Joiner.on(" ");

    static File encodeTheory() throws IOException {

        File maudeTheory = File.createTempFile("SPAN-", "");
        maudeTheory.deleteOnExit();

        PrintWriter writer = new PrintWriter(maudeTheory, "UTF-8");

        writer.println("fmod SPAN is");
        writer.println("sorts "
                + SPACE_JOINER.join(GlobalDataCache.getSignature().getSorts().stream()
                .map(Sort::getName)
                .collect(Collectors.toList()))
                + " .");

        for (Sort sort : GlobalDataCache.getSignature().getSorts()) {

            if (sort.hasSubsorts()) {
                writer.println("subsort "
                        + SPACE_JOINER.join(sort.getSubsorts().stream()
                        .map(Sort::getName).collect(Collectors.toList()))
                        + " < "
                        + sort.getName()
                        + " .");
            }
        }

        for (Sort sort : GlobalDataCache.getSignature().getSorts()) {

            List<VariableTerm> sortVariables = new ArrayList<>();

            for (VariableTerm var : GlobalDataCache.getSignature().getVariables()) {
                if (var.getSort().equals(sort)) {
                    sortVariables.add(var);
                }
            }

            if (!sortVariables.isEmpty()) {

                if(sort.equals(new Sort("Bit"))) {

                    writer.println("vars "
                            + SPACE_JOINER.join(sortVariables.stream()
                            .map(VariableTerm::getName).collect(Collectors.toList()))
                            + " b1 b2 : "
                            + sort.getName()
                            + " ."
                    );

                } else {

                    writer.println("vars "
                            + SPACE_JOINER.join(sortVariables.stream()
                            .map(VariableTerm::getName).collect(Collectors.toList()))
                            + " : "
                            + sort.getName()
                            + " ."
                    );
                }
            }
        }

        if (GlobalDataCache.getMetadata().isXOR()) {
            writer.println("op plus : Bit Bit -> Bit [assoc comm frozen] . ");
            writer.println("op zero : -> Bit . ");
            writer.println("op one : -> Bit . ");
        }

        for (FunctionSymbol function : GlobalDataCache.getSignature().getFunctions()) {

            // TODO: frozen option is not valid for 0-ary function symbols
            writer.println("op "
                    + function.getSymbol()
                    + " : "
                    + SPACE_JOINER.join(function.getParameterType().stream()
                    .map(Sort::getName).collect(Collectors.toList()))
                    + " -> "
                    + function.getReturnType().getName()
                    + " [frozen] ."
            );
        }

        for (NameTerm name : GlobalDataCache.getSignature().getPrivateNames()) {

            writer.println("op "
                    + name.getName()
                    + " : -> "
                    + name.getSort().getName()
                    + " ."
            );
        }

        for (NameTerm name : GlobalDataCache.getSignature().getPublicNames()) {

            writer.println("op "
                    + name.getName()
                    + " : -> "
                    + name.getSort().getName()
                    + " ."
            );
        }

        for (Rewrite rewrite : GlobalDataCache.getRewrites()) {
            writer.println("eq "
                    + rewrite.getLhs().toMathString()
                    + " = "
                    + rewrite.getRhs().toMathString()
                    + " [variant] ."
            );
        }

        if (GlobalDataCache.getMetadata().isXOR()) {
            writer.println("eq plus(b1, b1) = zero [variant] .");
            writer.println("eq plus(b2, plus(b1, b1)) = b2 [variant] .");
            writer.println("eq plus(b1, zero) = b1 [variant] .");
        }

        writer.println("endfm");
        writer.close();

        return maudeTheory;
    }

    static String encodeLoadTheory(File theory) {
        return "load " + theory.getAbsolutePath() + "\n";
    }

    static String encodeReduce(Term term) {
        return "reduce in SPAN : " + term.toMathString() + " .\n";
    }
}
