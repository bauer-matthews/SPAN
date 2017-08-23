package cache;

import org.apfloat.Apfloat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import protocol.Metadata;
import protocol.Protocol;
import protocol.ProtocolBuilder;
import protocol.SafetyProperty;
import resources.signature.Simple;
import rewriting.terms.*;

import java.util.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/27/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class TestGlobalDataCache {

    private static Protocol protocol;

    @BeforeClass
    public static void setUp() {
        initializeProtocol();
        GlobalDataCache.setProtocol(protocol);
    }

    @AfterClass
    public static void tearDown() {
        GlobalDataCache.setProtocol(null);
    }

    @Test
    public void generateRecipes_Depth1() throws Exception {

        Collection<Term> recipes = GlobalDataCache.getRecipes(1);

        assert(recipes.size() == 4);

        assert(recipes.contains(Simple.PUB_NAME_A));
        assert(recipes.contains(Simple.PUB_NAME_B));
        assert(recipes.contains(Simple.PUB_NAME_C));

        assert(recipes.contains(new FrameVariableTerm("W", 0)));
    }

    @Test
    public void generateRecipes_Depth2() throws Exception {

        protocol.getMetadata().setRecipeDepth(2);
        GlobalDataCache.setProtocol(protocol);

        Collection<Term> recipes = GlobalDataCache.getRecipes(1);

        // TODO: verfiy the list is correct
    }

    private static void initializeProtocol() {

        protocol = new ProtocolBuilder()
                .metadata(new Metadata("1", 1))
                .signature(Simple.SIGNATURE)
                .safetyProperty(new SafetyProperty(Collections.emptyList(), Apfloat.ONE))
                .fractionConstants(new HashMap<>())
                .rewrites(Collections.emptyList())
                .roles(Collections.emptyList())
                .build();
    }
}
