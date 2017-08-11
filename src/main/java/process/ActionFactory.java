package process;

import cache.GlobalDataCache;
import cache.RunConfiguration;
import rewriting.terms.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/10/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ActionFactory {

    public static Collection<Term> getRecipes(int numFrameVariables) {

        Collection<Term> terms = new ArrayList<>();

        for (int i = 1; i <= GlobalDataCache.getProtocol().getMetadata().getRecipeSize(); i++) {
            if (i == 1) {
                terms = getBaseRecipes(numFrameVariables);
            } else {
                terms.addAll(applyFunctions(terms));
            }
        }

        if (RunConfiguration.getTrace()) {
            System.out.println("ALL ACTIONS: ");
            for (Term term : terms) {
                System.out.println(term.toMathString());
            }
            System.out.println();
        }

        return terms;
    }

    private static Collection<Term> getBaseRecipes(int numFrameVariables) {
        Collection<Term> baseRecipes = new ArrayList<>();

        for (int i = 0; i < numFrameVariables; i++) {
            baseRecipes.add(new FrameVariableTerm("W", i));
        }

        for (NameTerm name : GlobalDataCache.getProtocol().getSignature().getPublicNames()) {
            baseRecipes.add(name);
        }

        for (FunctionSymbol functionSymbol : GlobalDataCache.getProtocol().getSignature().getFunctions()) {
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

        for (FunctionSymbol functionSymbol : orderByArity(GlobalDataCache.getProtocol().getSignature().getFunctions())) {

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

        for (int i = 0; i < functionSymbols.size(); i++) {

            int length = functionSymbols.size() - 2;
            for (int j = 0; j < length; j++) {
                if (functionSymbols.get(j).getArity() > functionSymbols.get(j + 1).getArity()) {
                    Collections.swap(functionSymbols, j, j + 1);
                }
            }
        }

        return functionSymbols;
    }
}
