package process;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import cache.SubstitutionCache;
import kiss.DeductionResult;
import kiss.EquivalenceResult;
import kiss.Kiss;
import kiss.KissEncoder;
import log.Console;
import log.Severity;
import util.ExitCode;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public static EquivalenceCheckResult check(State state1, State state2) throws IOException,
            InterruptedException, ExecutionException {

        switch (method) {
            case KISS:
                return checkKISS(state1, state2);
            default:
                Console.printMessage(Severity.INFO, "Reverting to default equivalence method");
                return checkKISS(state1, state2);
        }
    }

    private static EquivalenceCheckResult checkKISS(State state1, State state2)
            throws IOException, InterruptedException, ExecutionException {

        // TODO: Need to handle the cases when one or the other is empty. Can probably just modify the call
        // that invokes kiss. Farm this out in a different method.
        if (state1.getFrame().isEmpty() && state2.getFrame().isEmpty()) {
            return new EquivalenceCheckResult(true, false, false);
        }

        String resultString = Kiss.invokeKiss(KISS_COMMAND,
                KissEncoder.encode(GlobalDataCache.getProtocol().getSignature(),
                        GlobalDataCache.getProtocol().getRewrites(), state1, state2,
                        SubstitutionCache.applySubstitution(GlobalDataCache.getProtocol()
                                .getSafetyProperty().getSecrets(), state1.getSubstitution()),
                        SubstitutionCache.applySubstitution(GlobalDataCache.getProtocol()
                                .getSafetyProperty().getSecrets(), state2.getSubstitution())));

        List<EquivalenceResult> results = Kiss.getEquivalenceResults(resultString);

        if (results.size() == 0) {

            if(RunConfiguration.getDebug()) {

                System.out.println("Here is the problematic KISS encoding:");
                System.out.println();

                System.out.println(KissEncoder.encode(GlobalDataCache.getProtocol().getSignature(),
                        GlobalDataCache.getProtocol().getRewrites(), state1, state2,
                        SubstitutionCache.applySubstitution(GlobalDataCache.getProtocol()
                                .getSafetyProperty().getSecrets(), state1.getSubstitution()),
                        SubstitutionCache.applySubstitution(GlobalDataCache.getProtocol()
                                .getSafetyProperty().getSecrets(), state2.getSubstitution())));
            }

            Console.printError(Severity.ERROR, "Modeling checking failed due to KISS error");
            System.exit(ExitCode.KISS_ERROR.getValue());
        }

        boolean equivalent = results.get(0).isEquivalent()
                && enabledActionsMatch(state1, state2);

        boolean phi1Attack = false;
        boolean phi2Attack = false;

        for (DeductionResult deductionResult : Kiss.getDeductionResults(resultString)) {

            if (deductionResult.isDeducible()) {
                if (deductionResult.getFrame().equalsIgnoreCase("phi1")) {
                    phi1Attack = true;
                }
                if (deductionResult.getFrame().equalsIgnoreCase("phi2")) {
                    phi2Attack = true;
                }
            }

        }

        return new EquivalenceCheckResult(equivalent, phi1Attack, phi2Attack);
    }

    private static boolean enabledActionsMatch(State state1, State state2) throws ExecutionException {
        return state1.getRoleViews().equals(state2.getRoleViews());
    }
}
