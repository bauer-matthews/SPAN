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

        File maudeTheory = maudeTheory = File.createTempFile("SPAN-", "");
        maudeTheory.deleteOnExit();

        PrintWriter writer = new PrintWriter(maudeTheory, "UTF-8");

        writer.println("fmod SPAN is");
        writer.println("sorts "
                + SPACE_JOINER.join(GlobalDataCache.getProtocol().getSignature().getSorts().stream()
                .map(Sort::getName)
                .collect(Collectors.toList()))
                + " .");

        for (Sort sort : GlobalDataCache.getProtocol().getSignature().getSorts()) {

            if (sort.hasSubsorts()) {
                writer.println("subsort "
                        + SPACE_JOINER.join(sort.getSubsorts().stream()
                        .map(Sort::getName).collect(Collectors.toList()))
                        + " < "
                        + sort.getName()
                        + " .");
            }
        }

        for (Sort sort : GlobalDataCache.getProtocol().getSignature().getSorts()) {

            List<VariableTerm> sortVariables = new ArrayList<>();

            for (VariableTerm var : GlobalDataCache.getProtocol().getSignature().getVariables()) {
                if (var.getSort().equals(sort)) {
                    sortVariables.add(var);
                }
            }

            if (!sortVariables.isEmpty()) {
                writer.println("vars "
                        + SPACE_JOINER.join(sortVariables.stream()
                        .map(VariableTerm::getName).collect(Collectors.toList()))
                        + " : "
                        + sort.getName()
                        + " ."
                );
            }
        }

        for (FunctionSymbol function : GlobalDataCache.getProtocol().getSignature().getFunctions()) {
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

        for(NameTerm name : GlobalDataCache.getProtocol().getSignature().getPrivateNames()) {

            writer.println("op "
                + name.getName()
                    + " : -> "
                    + name.getSort().getName()
                    + " ."
            );
        }

        for(NameTerm name : GlobalDataCache.getProtocol().getSignature().getPublicNames()) {

            writer.println("op "
                    + name.getName()
                    + " : -> "
                    + name.getSort().getName()
                    + " ."
            );
        }

        for (Rewrite rewrite : GlobalDataCache.getProtocol().getRewrites()) {
            writer.println("eq "
                    + rewrite.getLhs().toMathString()
                    + " = "
                    + rewrite.getRhs().toMathString()
                    + " [variant] ."
            );
        }

        writer.println("endfm");
        writer.close();

        return maudeTheory;
    }

    static String encodeLoadTheory(File theory) {
        return "load " + theory.getAbsolutePath() + "\n";
    }

    static String encodeReduce(Term term) {
        return "reduce in SPAN : " + term.toMathString() + " .";
    }
}
