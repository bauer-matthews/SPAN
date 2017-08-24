package process;

import cache.*;
import rewriting.Equality;
import rewriting.terms.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/23/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ActionFactory2 {

    public static List<Action> getAllRecipes(List<Equality> frame, Optional<Term> guard, int role)
            throws ExecutionException {


        List<Action> actions = new ArrayList<>();

        List<FrameVariableTerm> frameVariableTerms = new ArrayList<>();
        Map<FrameVariableTerm, Term> frameVariableTermMap = new HashMap<>();

        for (Equality equality : frame) {

            frameVariableTerms.add((FrameVariableTerm) equality.getLhs());
            frameVariableTermMap.put((FrameVariableTerm) equality.getLhs(),equality.getRhs());
        }

        Sort guardSort = SortFactory.KIND;
        if (guard.isPresent()) {
            guardSort = guard.get().getSort();
        }


        List<Term> terms = new ArrayList<>();
        for (Term recipe : getAllRecipes(frameVariableTerms, frameVariableTermMap,
                guardSort, GlobalDataCache.getProtocol().getMetadata().getRecipeDepth(), guard)) {

            terms.add(recipe);
        }

        Set<Term> termSet = new HashSet<>();
        Map<Term, Term> termRecipeMap = new HashMap<>();

        for (Term recipe: terms) {

            Term groundTerm = recipe;
            for (FrameVariableTerm frameVariableTerm : frameVariableTerms) {
                groundTerm = groundTerm.substitute(frameVariableTerm, frameVariableTermMap.get(frameVariableTerm));
            }

            groundTerm = RewritingCache.reduce(groundTerm);

            if(termRecipeMap.get(groundTerm) == null ) {
                termRecipeMap.put(groundTerm, recipe);
            } else {
                if(termRecipeMap.get(groundTerm).getSize() > recipe.getSize()) {
                    termRecipeMap.put(groundTerm, recipe);
                }
            }

            termSet.add(groundTerm);
        }

        for(Term term : termSet) {
            actions.add(new Action(termRecipeMap.get(term), role));
        }

        return actions;
    }

    public static Collection<Term> getAllRecipes(List<FrameVariableTerm> frameVariables,
                                                 Map<FrameVariableTerm, Term> frameValues, Sort sort, int depth,
                                                 Optional<Term> guard) throws ExecutionException {

        Collection<Term> terms = new ArrayList<>();

        terms.addAll(getBaseRecipes(frameVariables, frameValues, sort, guard));

        if (guard.isPresent() && (!guard.get().isCompoundTerm())) {

            if(guard.get().isNameTerm()) {
                return terms;
            } else {
                for (FunctionSymbol functionSymbol : GlobalDataCache.getProtocol().getSignature().getFunctions()) {

                    if (functionSymbol.getReturnType().equals(sort)) {

                        Collection<List<Term>> parameterLists = getParameterLists(frameVariables, frameValues,
                                functionSymbol.getParameterType(), depth - 1, Optional.empty());

                        for (List<Term> parameterList : parameterLists) {
                            terms.add(new FunctionTerm(functionSymbol, parameterList));
                        }
                    }
                }

                return terms;
            }
        }

        if (depth == 1) {
            return terms;
        }

        Optional<List<Term>> guardList = Optional.empty();

        if (guard.isPresent()) {

            // NOTE: if we are here we know the guard is a compound term
            FunctionTerm guardFunctionTerm = (FunctionTerm) guard.get();

            List<Term> subGuards = new ArrayList<>();
            for (int i = 0; i < guardFunctionTerm.getSubterms().size(); i++) {
                subGuards.add(i, guardFunctionTerm.getSubterms().get(i));
            }

            guardList = Optional.of(subGuards);
            // NOTE: for 0-arity compound terms Option.empty() is retained
        }

        for (FunctionSymbol functionSymbol : GlobalDataCache.getProtocol().getSignature().getFunctions()) {

            if (functionSymbol.getReturnType().equals(sort)) {

                Collection<List<Term>> parameterLists = getParameterLists(frameVariables, frameValues,
                        functionSymbol.getParameterType(), depth - 1, guardList);

                for (List<Term> parameterList : parameterLists) {
                    Term newTerm = new FunctionTerm(functionSymbol, parameterList);
                    Term newTermSub = new FunctionTerm(functionSymbol, parameterList);

                    for (FrameVariableTerm frameVariableTerm : frameVariables) {
                        newTermSub = newTermSub.substitute(frameVariableTerm, frameValues.get(frameVariableTerm));
                    }

                    if (matchesGuard(guard, RewritingCache.reduce(newTermSub))) {
                        terms.add(newTerm);
                    }
                }
            }
        }

        return terms;
    }

    private static Collection<Term> getBaseRecipes(List<FrameVariableTerm> frameVariables,
                                                   Map<FrameVariableTerm, Term> frameValues, Sort sort,
                                                   Optional<Term> guard) throws ExecutionException {

        Collection<Term> baseRecipes = new ArrayList<>();

        for (FrameVariableTerm frameVariable : frameVariables) {
            if (frameValues.get(frameVariable).hasSort(sort) && matchesGuard(guard,
                    RewritingCache.reduce(frameValues.get(frameVariable)))) {

                baseRecipes.add(frameVariable);
            }
        }

        for (NameTerm name : GlobalDataCache.getProtocol().getSignature().getPublicNames()) {

            if (name.hasSort(sort) && matchesGuard(guard, name)) {
                baseRecipes.add(name);
            }
        }

        for (FunctionSymbol functionSymbol : GlobalDataCache.getProtocol().getSignature().getFunctions()) {
            if (functionSymbol.getArity() == 0) {

                if (functionSymbol.getReturnType().equals(sort) &&
                        matchesGuard(guard, new FunctionTerm(functionSymbol, Collections.emptyList()))) {

                    baseRecipes.add(new FunctionTerm(functionSymbol, Collections.emptyList()));
                }

            }
        }

        return baseRecipes;
    }

    private static Collection<List<Term>> getParameterLists(List<FrameVariableTerm> frameVariables,
                                                            Map<FrameVariableTerm, Term> frameValues,
                                                            List<Sort> parameterTypes, int depth,
                                                            Optional<List<Term>> guardList) throws ExecutionException {

        Map<Integer, Collection<Term>> parameterListMap = new HashMap<>();

        for (int i = 0; i < parameterTypes.size(); i++) {

            Optional<Term> guard = Optional.empty();
            if (guardList.isPresent()) {
                guard = Optional.ofNullable(guardList.get().get(i));
            }


            Set<Term> recipes = new HashSet<>();

            for (int j = depth; j > 0; j--) {
                recipes.addAll(getAllRecipes(frameVariables, frameValues, parameterTypes.get(i), j, guard));
            }


            parameterListMap.put(i, recipes);
        }

        return permuteListMap(parameterListMap);
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

    private static Collection<List<Term>> permuteListMap(Map<Integer, Collection<Term>> parameterListMap) {

        Collection<List<Term>> newLists = new ArrayList<>();
        for (Term term : parameterListMap.get(0)) {
            newLists.add(Collections.singletonList(term));
        }


        for (int i = 1; i < parameterListMap.size(); i++) {

            newLists = extendLists(newLists, parameterListMap.get(i));
        }

        return newLists;
    }

    /**
     * @param guard
     * @param term  in normal form / must also be ground
     * @return
     * @throws ExecutionException
     */
    private static boolean matchesGuard(Optional<Term> guard, Term term)
            throws ExecutionException {

        if (!guard.isPresent()) {
            return true;
        }


        if (guard.get().isVariableTerm()) {
            return true;
        }

        if (guard.get().isGroundTerm()) {
            return guard.get().equals(term);
        }

        //Otherwise we have a compound term with variables
        FunctionTerm guardFunctionTerm = (FunctionTerm) guard.get();

        if (!term.isCompoundTerm()) {
            return false;
        }

        FunctionTerm termFunctionTerm = (FunctionTerm) term;

        if (!guardFunctionTerm.getRootSymbol().equals(((FunctionTerm) term).getRootSymbol())) {
            return false;
        }

        for (int i = 0; i < guardFunctionTerm.getSubterms().size(); i++) {
            if (!(matchesGuard(Optional.ofNullable(guardFunctionTerm.getSubterms().get(i)),
                    termFunctionTerm.getSubterms().get(i)))) {
                return false;
            }
        }

        return true;
    }
}
