package process;

import cache.*;
import rewriting.Equality;
import rewriting.terms.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/10/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ActionFactory {

    public static Collection<Term> getAllRecipes(int numFrameVariables) {

        Collection<Term> terms = new ArrayList<>();

        for (int i = 1; i <= GlobalDataCache.getProtocol().getMetadata().getRecipeDepth(); i++) {
            if (i == 1) {
                terms = getBaseRecipes(numFrameVariables);
            } else {
                terms.addAll(applyFunctions(terms));
            }
        }

        if (RunConfiguration.getTrace() ) {
            System.out.println("ALL ACTIONS(" + numFrameVariables + "): ");
            for (Term term : terms) {
                System.out.println(term.toMathString());
            }
            System.out.println();
        }

        terms = filterByTypeCompliance(terms);


        if (RunConfiguration.getTrace()) {
            System.out.println("TYPE COMPLIANT ACTIONS(" + numFrameVariables + "): ");
            for (Term term : terms) {
                System.out.println(term.toMathString());
            }
            System.out.println();
        }

        return terms;
    }

    private static Collection<Term> filterByTypeCompliance(Collection<Term> recipes) {

        ArrayList<Term> terms = new ArrayList<>();

        for (Term term : recipes) {
            if (typeCompliant(term)) {
                terms.add(term);
            }
        }

        return terms;
    }

    private static boolean typeCompliant(Term recipe) {

        if (recipe.isCompoundTerm()) {
            FunctionTerm term = ((FunctionTerm) recipe);

            for (int i = 0; i < term.getRootSymbol().getArity(); i++) {

                if ((!term.getSubterms().get(i).hasSort(term.getRootSymbol().getParameterType().get(i)))
                        && (!(term.getSubterms().get(i) instanceof FrameVariableTerm))) {
                    return false;
                }
            }

            for (Term subterm : term.getSubterms()) {
                if (!typeCompliant(subterm)) {
                    return false;
                }
            }
        }

        return true;
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

    private static Collection<Term> applyFunctions(Collection<Term> recipes) {

        Collection<Term> newRecipies = new ArrayList<>();

        Collection<List<Term>> lists = new ArrayList<>();
        for (Term recipe : recipes) {
            lists.add(Collections.singletonList(recipe));
        }

        int currentArity = 1;

        for (FunctionSymbol functionSymbol : orderByArity(GlobalDataCache.getProtocol().getSignature().getFunctions())) {

            if (functionSymbol.getArity() == 0) {
                // ignore, already handled
            }

            if (functionSymbol.getArity() != currentArity) {

                int diff = functionSymbol.getArity() - currentArity;
                for (int i = 0; i < diff; i++) {
                    lists = extendLists(lists, recipes);
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
            for (int j = 0; j <= length; j++) {
                if (functionSymbols.get(j).getArity() > functionSymbols.get(j + 1).getArity()) {
                    Collections.swap(functionSymbols, j, j + 1);
                }
            }
        }

        return functionSymbols;
    }

    public static Collection<Term> getRecipesWithGuard(int numFrameVariables, Term guard, List<Equality> frame)
            throws ExecutionException {

        Collection<Term> filteredRecipes = new ArrayList<>();

        for (Term recipe : GlobalDataCache.getRecipes(numFrameVariables)) {

            Term groundTerm = SubstitutionCache.applySubstitution(recipe, frame);

            if (typeCompliant(groundTerm)) {

                if (UnificationCache.unify(guard, RewritingCache.reduce(groundTerm)).isPresent()) {
                    filteredRecipes.add(recipe);
                }
            }
        }

        if (RunConfiguration.getTrace() || RunConfiguration.getDebug()) {
            System.out.println("FILTERED ACTIONS(" + numFrameVariables + "): " + guard.toMathString());
            for (Term term : filteredRecipes) {
                System.out.println(term.toMathString());
            }
            System.out.println();
        }

        return filteredRecipes;
    }
}
