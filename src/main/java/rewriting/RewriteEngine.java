package rewriting;

import cache.SubstitutionCache;
import cache.UnificationCache;
import rewriting.terms.FunctionTerm;
import rewriting.terms.Term;
import rewriting.unification.UnificationEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by mbauer on 8/1/2017.
 */
public class RewriteEngine {

    public static Term reduce(Term term, Collection<Rewrite> rewrites, boolean useCache) throws ExecutionException {

        boolean rewrite = true;
        do {
            Term reducedTerm = rewrite(term, rewrites, useCache);
            if (reducedTerm != null) {
                term = reducedTerm;
            } else {
                rewrite = false;
            }

        } while (rewrite);

        return term;
    }

    private static Term rewrite(Term term, Collection<Rewrite> rewrites, boolean useCache) throws ExecutionException {

        for (Rewrite rewrite : rewrites) {

            if (useCache) {
                Optional<Collection<Equality>> unifier = UnificationCache.unify(term, rewrite.getLhs());
                if (unifier.isPresent()) {
                    return SubstitutionCache.applySubstitution(rewrite.getRhs(), unifier.get());
                }
            } else {
                Optional<Collection<Equality>> unifier = UnificationEngine.unify(term, rewrite.getLhs());
                if (unifier.isPresent()) {
                    return SubstitutionCache.applySubstitution(rewrite.getRhs(), unifier.get());
                }
            }
        }

        if (term.isCompoundTerm()) {

            List<Term> subterms = ((FunctionTerm) term).getSubterms();

            for (int i = 0; i < subterms.size(); i++) {

                for (Rewrite rewrite : rewrites) {
                    Optional<Collection<Equality>> unifier = UnificationCache.unify(subterms.get(i), rewrite.getLhs());

                    if (unifier.isPresent()) {

                        List<Term> newSubterms = new ArrayList<>();
                        for (int j = 0; j < subterms.size(); j++) {

                            if (j == i) {
                                newSubterms.add(i, SubstitutionCache.applySubstitution(rewrite.getRhs(), unifier.get()));
                            } else {
                                newSubterms.add(i, subterms.get(i));
                            }
                        }

                        return new FunctionTerm(((FunctionTerm) term).getRootSymbol(), newSubterms);
                    }
                }
            }
        }

        return null;
    }
}
