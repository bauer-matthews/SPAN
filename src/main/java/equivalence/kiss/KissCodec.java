package equivalence.kiss;

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
 * Created by mbauer on 8/2/2017.
 */
public class KissCodec {

    private static final Joiner COMMA_JOINER = Joiner.on(", ");
    private static final Joiner COMMA_RETURN_JOINER = Joiner.on(",\n");


    public static String encode(Signature signature, Collection<Rewrite> rewrites, State state1, State state2,
                                List<Term> frame1Secrets, List<Term> frame2Secrets) {

        StringBuilder sb = new StringBuilder();

        sb.append("signature ");
        sb.append(COMMA_JOINER.join(signature.getFunctions().stream()
                .map(functionSymbol -> functionSymbol.getSymbol() + "/" + functionSymbol.getArity())
                .collect(Collectors.toList())));
        sb.append(";\n");

        sb.append("variables ");
        sb.append(COMMA_JOINER.join(signature.getVariables().stream()
                .map(VariableTerm::toMathString)
                .collect(Collectors.toList())));
        sb.append(";\n");

        sb.append("names ");
        sb.append(COMMA_JOINER.join(signature.getPublicNames().stream()
                .map(NameTerm::toMathString)
                .collect(Collectors.toList())));
        sb.append(", ");
        sb.append(COMMA_JOINER.join(signature.getPrivateNames().stream()
                .map(NameTerm::toMathString)
                .collect(Collectors.toList())));
        sb.append(", ");

        int frameTerms = Math.max(state1.getFrame().size(), state2.getFrame().size()) - 1;

        ArrayList<String> frameVars = new ArrayList<>();
        for(int i=0; i <= frameTerms; i++) {
            frameVars.add("W" + i);
        }

        sb.append(COMMA_JOINER.join(frameVars.stream()
                .collect(Collectors.toList())));
        sb.append(";\n");
        sb.append("rewrite\n");

        List<String> rewriteStrings = new ArrayList<>();
        for(Rewrite rewrite : rewrites) {
            rewriteStrings.add("\t\t" + rewrite.getLhs().toMathString() + " -> " + rewrite.getRhs().toMathString());
        }

        sb.append(COMMA_RETURN_JOINER.join(rewriteStrings));
        sb.append(";\n");

        sb.append("frames\n");

        sb.append("\t\t");
        sb.append("phi1 = new ");

        sb.append(COMMA_JOINER.join(GlobalDataCache.getProtocol().getSignature().getPrivateNames().stream()
                .map(NameTerm::toMathString)
                .collect(Collectors.toList())));

        sb.append(".{");
        sb.append(COMMA_JOINER.join(state1.getFrame().stream()
                .map(Equality::toMathString)
                .collect(Collectors.toList())));
        sb.append("},\n");

        sb.append("\t\t");
        sb.append("phi2 = new ");

        sb.append(COMMA_JOINER.join(GlobalDataCache.getProtocol().getSignature().getPrivateNames().stream()
                .map(NameTerm::toMathString)
                .collect(Collectors.toList())));

        sb.append(".{");
        sb.append(COMMA_JOINER.join(state2.getFrame().stream()
                .map(Equality::toMathString)
                .collect(Collectors.toList())));
        sb.append("};\n");

        sb.append("questions\n");
        sb.append("\t\t");
        sb.append("equiv phi1 phi2");

        if(frame1Secrets.isEmpty() && frame2Secrets.isEmpty()) {
            sb.append(";");
        } else {
            sb.append(",\n");
            for(Term sec : frame1Secrets) {
                sb.append("\t\tdeducible ");
                sb.append(sec.toMathString());
                sb.append(" phi1,\n");
            }

            for(Term sec : frame2Secrets) {
                sb.append("\t\tdeducible ");
                sb.append(sec.toMathString());
                sb.append(" phi2,\n");
            }

            int lastSemi = sb.lastIndexOf(",");
            sb.replace(lastSemi, lastSemi+1, ";");
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }

        return sb.toString();
    }

    static List<DeductionResult> decodeDeductionResults(String output) {
        List<DeductionResult> results = new ArrayList<>();

        String[] pieces = output.split("\n");

        for (int i = 0; i < pieces.length; i++) {

            String out = pieces[i];

            if (out.contains(Resources.DEDUCTION_ID)) {
                if (out.startsWith(Resources.TRUE)) {
                    String values = out.substring(out.indexOf(Resources.TRUE) + 3,

                            out.indexOf(Resources.RECIPE_ID)).trim();
                    String[] valuePieces = values.split(Resources.DEDUCTION_ID_REGEX);

                    results.add(new DeductionResult(valuePieces[1].trim(), true, valuePieces[0].trim(),
                            Optional.of(out.substring(out.indexOf(Resources.RECIPE_ID) + 2))));

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

    static List<EquivalenceResult> decodeEquivalenceResults(String output) {
        List<EquivalenceResult> results = new ArrayList<>();

        String[] pieces = output.split("\n");

        for (int i = 0; i < pieces.length; i++) {

            String out = pieces[i];
            if (out.contains(Resources.EQUIVALENCE_ID)) {
                if (out.startsWith(Resources.TRUE)) {

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
