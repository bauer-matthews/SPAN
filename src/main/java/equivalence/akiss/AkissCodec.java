package equivalence.akiss;

import cache.GlobalDataCache;
import com.google.common.base.Joiner;
import equivalence.DeductionResult;
import equivalence.EquivalenceResult;
import process.State;
import rewriting.Equality;
import rewriting.Rewrite;
import rewriting.Signature;
import rewriting.terms.NameTerm;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 10/30/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
class AkissCodec {

    private static final Joiner COMMA_JOINER = Joiner.on(", ");
    private static final Joiner DOT_JOINER = Joiner.on(".");

    // Hack because deduction is not supported in AKISS
    private static List<DeductionResult> deductionResults = new ArrayList<>();

    static String encode(Signature signature, Collection<Rewrite> rewrites, State state1, State state2,
                         List<Term> frame1Secrets, List<Term> frame2Secrets) {

        checkforAttacks(state1, state2, frame1Secrets, frame2Secrets);

        StringBuilder sb = new StringBuilder();

        if (GlobalDataCache.getMetadata().isXOR()) {
            sb.append("#set xor;\n\n");
        }

        sb.append("symbols ");
        sb.append(COMMA_JOINER.join(signature.getFunctions().stream()
                .map(functionSymbol -> functionSymbol.getSymbol() + "/" + functionSymbol.getArity())
                .collect(Collectors.toList())));

        if(signature.getFunctions().size() != 0) {
            sb.append(", ");
        }

        sb.append(COMMA_JOINER.join(signature.getPublicNames().stream()
                .map(name -> name.getName() + "/0" )
                .collect(Collectors.toList())));


        if(GlobalDataCache.getMetadata().isXOR()) {
            sb.append(", one/0");
        }

        sb.append(";\n");

        sb.append("private ");
        sb.append(COMMA_JOINER.join(signature.getPrivateNames().stream()
                .map(NameTerm::toMathString)
                .collect(Collectors.toList())));
        sb.append(";\n");

        sb.append("channels c;\n");

        sb.append("var ");
        sb.append(COMMA_JOINER.join(signature.getVariables().stream()
                .map(VariableTerm::toMathString)
                .collect(Collectors.toList())));
        sb.append(";\n");

        for (Rewrite rewrite : rewrites) {
            sb.append("rewrite ");
            sb.append(rewrite.getLhs().toMathString());
            sb.append(" -> ");
            sb.append(rewrite.getRhs().toMathString());
            sb.append(";\n");
        }

        sb.append("P = ");
        sb.append(DOT_JOINER.join(state1.getFrame().stream()
                .map((equality -> "out(c, " + equality.getRhs().toMathString() + ")"))
                .collect(Collectors.toList())));
        sb.append(".0;\n");

        sb.append("Q = ");
        sb.append(DOT_JOINER.join(state2.getFrame().stream()
                .map((equality -> "out(c, " + equality.getRhs().toMathString() + ")"))
                .collect(Collectors.toList())));
        sb.append(".0;\n");
        sb.append("not equivalentft? P and Q;");

        return sb.toString();
    }

    private static void checkforAttacks(State state1, State state2,
                                        List<Term> frame1Secrets, List<Term> frame2Secrets) {

        deductionResults.clear();

        boolean attack = false;
        Term recipe = null;

        for (Term term : frame1Secrets) {

            for (Equality equality : state1.getFrame()) {
                if (term.equals(equality.getRhs())) {
                    attack = true;
                    recipe = equality.getLhs();
                }
            }

            if (attack) {
                deductionResults.add(new DeductionResult(term.toMathString(), true,
                        "phi1", Optional.ofNullable(recipe.toMathString())));
            } else {
                deductionResults.add(new DeductionResult(term.toMathString(), false,
                        "phi", Optional.empty()));
            }
        }

        for (Term term : frame2Secrets) {

            for (Equality equality : state2.getFrame()) {
                if (term.equals(equality.getRhs())) {
                    attack = true;
                    recipe = equality.getLhs();
                }
            }

            if (attack) {
                deductionResults.add(new DeductionResult(term.toMathString(), true,
                        "phi2", Optional.ofNullable(recipe.toMathString())));
            } else {
                deductionResults.add(new DeductionResult(term.toMathString(), false,
                        "ph2", Optional.empty()));
            }
        }
    }

    static List<DeductionResult> decodeDeductionResults(String output) {
        return deductionResults;
    }

    static List<EquivalenceResult> decodeEquivalenceResults(String output) {

        List<EquivalenceResult> results = new ArrayList<>();

        if (output.contains("cannot establish trace equivalence of P and Q")) {
            results.add(new EquivalenceResult("phi1", "phi2", false));
        } else if (output.contains("P and Q are fine-grained trace equivalent")) {
            results.add(new EquivalenceResult("phi1", "phi2", true));
        }

        return results;
    }
}