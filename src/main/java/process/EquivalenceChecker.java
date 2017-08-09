package process;

import cache.GlobalDataCache;
import configuration.RunConfiguration;
import kiss.DeductionResult;
import kiss.Kiss;
import kiss.KissEncoder;
import log.Console;
import log.Severity;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Created by mbauer on 8/8/2017.
 */
public class EquivalenceChecker {

    // TODO: Take from input
    private static String KISS_COMMAND = "/home/matt/Desktop/kiss/kiss";

    private static final EquivalenceMethod method;

    public enum EquivalenceMethod {
        KISS;
    }

    static {
        method = RunConfiguration.getEquivalenceMethod();
    }

    static EquivalenceCheckResult check(State state1, State state2) throws IOException, InterruptedException {

        switch (method) {
            case KISS:
                return checkKISS(state1, state2);
            default:
                Console.printMessage(Severity.INFO, "Reverting to default equivalence method");
                return checkKISS(state1, state2);
        }
    }

    private static EquivalenceCheckResult checkKISS(State state1, State state2) throws IOException, InterruptedException {

        String resultString = Kiss.invokeKiss(KISS_COMMAND,
                KissEncoder.encode(GlobalDataCache.getProtocol().getSignature(),
                        GlobalDataCache.getProtocol().getRewrites(), state1, state2,
                        Collections.emptyList(), Collections.emptyList()));

        boolean equivalent = Kiss.getEquivalenceResults(resultString).get(0).isEquivalent()
                && enabledActionsMatch(state1, state2);

        Stream<DeductionResult> deductionResultStream = Kiss.getDeductionResults(resultString).stream()
                .filter(deductionResult -> deductionResult.isDeducible());

        boolean phi1Attack = deductionResultStream.anyMatch(deductionResult ->
                deductionResult.getFrame().equalsIgnoreCase("phi1"));
        boolean phi2Attack = deductionResultStream.anyMatch(deductionResult ->
                deductionResult.getFrame().equalsIgnoreCase("phi2"));

        return new EquivalenceCheckResult(equivalent, phi1Attack, phi2Attack);
    }

    private static boolean enabledActionsMatch(State state1, State state2) {
        // TODO: Make this more efficient!!!
        return state1.getEnabledActions().equals(state2.getEnabledActions());
    }
}
