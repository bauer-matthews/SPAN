package cache;

import org.apfloat.Apfloat;
import protocol.Protocol;
import rewriting.terms.*;

import java.util.*;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 7/26/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class GlobalDataCache {

    private static Protocol protocol;

    public static void setProtocol(Protocol protocolInstance) {
        Objects.requireNonNull(protocolInstance);
        protocol = protocolInstance;
    }

    public static Protocol getProtocol() {
        return protocol;
    }

    public static Collection<Term> getRecipes(int numFrameVariables) {

        Collection<Term> terms = new ArrayList<>();

        //TODO: use a helper funciton to generate all recipes of a given size and then apply the set of fuction symbols
        // on top, cache the computed set using guava cache library. Will return the set with w1 ... wn. This way
        // we only need to compute it once. Just make sure the transition implement it that way

        // TODO: pull the recipe size and other info (function symbols etc.. from protocol).

        return terms;
    }

    private static Collection<Term> getBaseRecipes(int numFrameVariables) {
        Collection<Term> baseRecipes = new ArrayList<>();

        for (int i = 0; i < numFrameVariables; i++) {
            baseRecipes.add(new FrameVariableTerm(new VariableTerm("W"), i));
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
                    extendLists(lists, recipies);
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
                list.add(term);
                newLists.add(list);
                list.remove(term);
            }
        }

        return newLists;
    }

    private static List<FunctionSymbol> orderByArity(Collection<FunctionSymbol> functionSymbols) {

        List<FunctionSymbol> orderedList = new ArrayList<>();

        // TODO

        return orderedList;
    }
}
