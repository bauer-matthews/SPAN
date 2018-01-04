package equivalence;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import cache.SubstitutionCache;
import equivalence.akiss.AkissEngine;
import equivalence.kiss.KissEngine;
import log.Console;
import log.Severity;
import parser.protocol.ProtocolType;
import process.State;
import util.ExitCode;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/8/2017.
 */
public class EquivalenceChecker {

    private static final EquivalenceEngine ENGINE;

    static {

        if (RunConfiguration.getEquivalenceMethod().equals(EquivalenceMethod.KISS)) {
            ENGINE = new KissEngine();
        } else if (RunConfiguration.getEquivalenceMethod().equals(EquivalenceMethod.AKISS)) {
            ENGINE = new AkissEngine();
        } else {
            ENGINE = null;
        }
    }

    public static EquivalenceCheckResult check(State state1, State state2)
            throws IOException, InterruptedException, ExecutionException {

        // TODO: Need to handle the cases when one or the other is empty. Can probably just modify the call
        // that invokes kiss. Farm this out in a different method.
        if (state1.getFrame().isEmpty() && state2.getFrame().isEmpty()) {
            return new EquivalenceCheckResult(true, false, false);
        }

        String resultString = ENGINE.invoke(
                ENGINE.encode(GlobalDataCache.getSignature(),
                        GlobalDataCache.getRewrites(), state1, state2,
                        (GlobalDataCache.getProtocolType().equals(ProtocolType.REACHABILITY) ?
                                SubstitutionCache.applySubstitution(GlobalDataCache.getReachabilityProtocol()
                                .getSafetyProperty().getSecrets(), state1.getSubstitution()) : Collections.emptyList()),
                        (GlobalDataCache.getProtocolType().equals(ProtocolType.REACHABILITY) ?
                                SubstitutionCache.applySubstitution(GlobalDataCache.getReachabilityProtocol()
                                .getSafetyProperty().getSecrets(), state2.getSubstitution()) : Collections.emptyList())));


        List<EquivalenceResult> results = ENGINE.decodeEquivalenceResults(resultString);

        if (results.size() == 0) {

            if (RunConfiguration.getDebug()) {

                System.out.println("Here is the problematic encoding:");
                System.out.println();

                System.out.println(ENGINE.encode(GlobalDataCache.getSignature(),
                        GlobalDataCache.getRewrites(), state1, state2,
                        (GlobalDataCache.getProtocolType().equals(ProtocolType.REACHABILITY) ?
                                SubstitutionCache.applySubstitution(GlobalDataCache.getReachabilityProtocol()
                                .getSafetyProperty().getSecrets(), state1.getSubstitution()) : Collections.emptyList()),
                        (GlobalDataCache.getProtocolType().equals(ProtocolType.REACHABILITY) ?
                                SubstitutionCache.applySubstitution(GlobalDataCache.getReachabilityProtocol()
                                .getSafetyProperty().getSecrets(), state2.getSubstitution()) : Collections.emptyList())));
            }

            Console.printError(Severity.ERROR, "Modeling checking failed due to equivalence engine error");
            System.exit(ExitCode.EQUIVALENCE_ERROR.getValue());
        }

        boolean equivalent = results.get(0).isEquivalent()
                && enabledActionsMatch(state1, state2);

        boolean phi1Attack = false;
        boolean phi2Attack = false;

        for (DeductionResult deductionResult : ENGINE.decodeDeductionResults(resultString)) {

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
        return (state1.getRoleViews().equals(state2.getRoleViews())) &&
                (state1.getFrame().size() == state2.getFrame().size());
    }
}
