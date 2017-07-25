package kiss;

import org.junit.Test;

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
public class TestKiss {

    String QUERRY_1 = "signature pair/2, fst/1, snd/1, enc/2, dec/2;\n" +
            "variables x, y, z;\n" +
            "names a, b, c, k, w1, w2, w3;\n" +
            "rewrite\n" +
            "fst(pair(x, y)) -> x,\n" +
            "snd(pair(x, y)) -> y,\n" +
            "dec(enc(x, y), y) -> x;\n" +
            "frames\n" +
            "phi1 = new a, k.{w1 = enc(a, k), w2 = a},\n" +
            "phi2 = new b, k.{w1 = enc(b, k), w2 = c};\n" +
            "questions\n" +
            "deducible a phi1,\n" +
            "deducible b phi2,\n" +
            "equiv phi1 phi2,\n" +
            "equiv phi1 phi1\n" +
            "knowledgebase phi1;\n";

    // TODO: Take from input
    String COMMAND = "/home/matt/Desktop/kiss/kiss";

    @Test
    public void invokeKiss() throws Exception {
        String output = Kiss.invokeKiss(COMMAND, QUERRY_1);
        List<DeductionResult> deductionResults= Kiss.getDeductionResults(output);
        List<EquivalenceResult> equivalenceResults = Kiss.getEquivalenceResults(output);

        assert(deductionResults.size() == 2);
        assert(equivalenceResults.size() == 2);

        DeductionResult result1 = new DeductionResult("a", true, "phi1", Optional.of("w2"));
        assert(result1.equals(deductionResults.get(0)));

        DeductionResult result2 = new DeductionResult("b", false, "phi2", Optional.empty());
        assert(result2.equals(deductionResults.get(1)));

        EquivalenceResult result3 = new EquivalenceResult("phi1", "phi2", false);
        assert(result3.equals(equivalenceResults.get(0)));

        EquivalenceResult result4 = new EquivalenceResult("phi1", "phi1", true);
        assert(result4.equals(equivalenceResults.get(1)));
    }

}
