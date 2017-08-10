package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apfloat.Apfloat;
import protocol.Protocol;
import rewriting.terms.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/26/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class GlobalDataCache {

    private static final LoadingCache<Integer, Collection<Term>> recipies;
    private static Protocol protocol;

    static {
        recipies = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                        new CacheLoader<Integer, Collection<Term>>() {
                            // TODO: tighten exception
                            public Collection<Term> load(Integer numFrameVariables) throws Exception {
                                return getRecipes(numFrameVariables);
                            }
                        });
    }

    public static void setProtocol(Protocol protocolInstance) {
        protocol = protocolInstance;
    }

    public static Protocol getProtocol() {
        return protocol;
    }

    public static Collection<Term> getRecipes(int numFrameVariables) {

        Collection<Term> terms = new ArrayList<>();

        for(int i=1; i <= protocol.getMetadata().getRecipeSize(); i++) {
            if(i==1) {
                terms = getBaseRecipes(numFrameVariables);
            } else {
                terms = applyFunctions(terms);
            }
        }

        return terms;
    }

    private static Collection<Term> getBaseRecipes(int numFrameVariables) {
        Collection<Term> baseRecipes = new ArrayList<>();

        for (int i = 0; i < numFrameVariables; i++) {
            baseRecipes.add(new FrameVariableTerm("W", i));
        }

        for (NameTerm name : protocol.getSignature().getPublicNames()) {
            baseRecipes.add(name);
        }

        for (FunctionSymbol functionSymbol : protocol.getSignature().getFunctions()) {
            if (functionSymbol.getArity() == 0) {
                baseRecipes.add(new FunctionTerm(functionSymbol, Collections.emptyList()));
            }
        }

        return baseRecipes;
    }

    private static Collection<Term> applyFunctions(Collection<Term> recipies) {

        Collection<Term> newRecipies = new ArrayList<>();

        Collection<List<Term>> lists = new ArrayList<>();
        for (Term recipie : recipies) {
            lists.add(Collections.singletonList(recipie));
        }

        int currentArity = 1;

        for (FunctionSymbol functionSymbol : orderByArity(protocol.getSignature().getFunctions())) {

            if (functionSymbol.getArity() == 0) {
                // ignore, already handled
            }

            if (functionSymbol.getArity() != currentArity) {

                int diff = functionSymbol.getArity() - currentArity;
                for (int i = 0; i < diff; i++) {
                    lists = extendLists(lists, recipies);
                }

                currentArity = functionSymbol.getArity();
            }

            for (List<Term> list : lists) {
                newRecipies.add(new FunctionTerm(functionSymbol, list));
            }
        }

        return newRecipies;
    }

    private static Collection<List<Term>> extendLists(Collection<List<Term>> lists, Collection<Term> terms) {

        Collection<List<Term>> newLists = new ArrayList<>();

        for (List<Term> list : lists) {
            for (Term term : terms) {

                // TODO: optimize
                List<Term> newList = new ArrayList<>();
                newList.addAll(list);
                newList.add(term);
                newLists.add(newList);
            }
        }

        return newLists;
    }

    private static List<FunctionSymbol> orderByArity(List<FunctionSymbol> functionSymbols) {

        for(int i=0; i< functionSymbols.size(); i++) {

            int length = functionSymbols.size()-2;
            for(int j=0; j< length; j++) {
                if(functionSymbols.get(j).getArity() > functionSymbols.get(j+1).getArity()) {
                    Collections.swap(functionSymbols, j, j+1);
                }
            }
        }

        return functionSymbols;
    }
}
