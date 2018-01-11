package cache;

import mc.indistinguishability.IndistinguishabilityMethod;
import mc.reachability.ReachabilityMethod;
import org.apfloat.Aprational;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import parser.protocol.ProtocolType;
import protocol.Metadata;
import protocol.ReachabilityProtocol;
import protocol.ProtocolBuilder;
import protocol.SafetyProperty;
import resources.signature.Simple;
import rewriting.terms.FrameVariableTerm;
import rewriting.terms.Term;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/27/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class TestGlobalDataCache {

    private static ReachabilityProtocol protocol;

    @BeforeClass
    public static void setUp() {
        initializeProtocol();
        GlobalDataCache.setReachabilityProtocol(protocol);
    }

    @AfterClass
    public static void tearDown() {
        GlobalDataCache.setReachabilityProtocol(null);
    }

    @Test
    public void generateRecipes_Depth1() throws Exception {

        Collection<Term> recipes = ActionFactoryCache.getRecipes(1);

        assert(recipes.size() == 4);

        assert(recipes.contains(Simple.PUB_NAME_A));
        assert(recipes.contains(Simple.PUB_NAME_B));
        assert(recipes.contains(Simple.PUB_NAME_C));

        assert(recipes.contains(new FrameVariableTerm("W", 0)));
    }

    @Test
    public void generateRecipes_Depth2() throws Exception {

        protocol.getMetadata().setRecipeDepth(2);
        GlobalDataCache.setReachabilityProtocol(protocol);

        Collection<Term> recipes = ActionFactoryCache.getRecipes(1);

        // TODO: verfiy the list is correct
    }

    private static void initializeProtocol() {

        protocol = new ProtocolBuilder()
                .metadata(new Metadata("1", 1, false, ProtocolType.REACHABILITY,
                        ReachabilityMethod.OTF, IndistinguishabilityMethod.OTF))
                .signature(Simple.SIGNATURE)
                .safetyProperty(new SafetyProperty(Collections.emptyList(), Aprational.ONE))
                .fractionConstants(new HashMap<>())
                .rewrites(Collections.emptyList())
                .roles(Collections.emptyList())
                .buildReachabilityProtocol();
    }
}
