package cache;

import org.apfloat.Apfloat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import protocol.Metadata;
import protocol.Protocol;
import protocol.ProtocolBuilder;
import protocol.SafetyProperty;
import rewriting.Rewrites;
import rewriting.Signature;
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

        assert(recipes.contains(new NameTerm("a", false)));
        assert(recipes.contains(new NameTerm("b", false)));
        assert(recipes.contains(new NameTerm("c", false)));

        assert(recipes.contains(new FrameVariableTerm(new VariableTerm("W"), 0)));
    }

    @Test
    public void generateRecipes_Depth2() throws Exception {

        protocol.getMetadata().setRecipeSize(2);
        GlobalDataCache.setProtocol(protocol);

        Collection<Term> recipes = GlobalDataCache.getRecipes(1);

        // TODO: verfiy the list is correct
    }

    private static void initializeProtocol() {

        List<FunctionSymbol> functionSymbolList = new ArrayList<>();

        functionSymbolList.add(new FunctionSymbol("one", 1));
        functionSymbolList.add(new FunctionSymbol("two", 2));
        functionSymbolList.add(new FunctionSymbol("three", 2));

        Collection<NameTerm> pubicNames = new ArrayList<>();
        pubicNames.add(new NameTerm("a", false));
        pubicNames.add(new NameTerm("b", false));
        pubicNames.add(new NameTerm("c", false));

        Signature signature = new Signature(functionSymbolList, pubicNames, new ArrayList<>(), new ArrayList<>());

        protocol = new ProtocolBuilder()
                .metadata(new Metadata("1", 1))
                .signature(signature)
                .safetyProperty(new SafetyProperty(Collections.emptyList(), Apfloat.ONE))
                .fractionConstants(new HashMap<>())
                .rewrites(new Rewrites(Collections.emptyList()))
                .roles(Collections.emptyList())
                .build();
    }
}
