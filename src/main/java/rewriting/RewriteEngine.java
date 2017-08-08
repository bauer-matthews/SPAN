package rewriting;

import rewriting.terms.FunctionTerm;
import rewriting.terms.Term;
import rewriting.unification.Unify;
import util.rewrite.RewriteUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by mbauer on 8/1/2017.
 */
public class RewriteEngine {

    static Term reduce(Term term, Collection<Rewrite> rewrites) {

        boolean rewrite = true;
        do{
            Term reducedTerm = rewrite(term, rewrites);
            if(reducedTerm != null) {
                term = reducedTerm;
            } else {
                rewrite = false;
            }

        } while (rewrite);

        return term;
    }

    private static Term rewrite(Term term, Collection<Rewrite> rewrites) {

        for(Rewrite rewrite : rewrites) {
            Optional<Collection<Equality>> unifier = Unify.unify(term, rewrite.getLhs());
            if(unifier.isPresent()) {
                return RewriteUtils.applySubstitution(rewrite.getRhs(), unifier.get());
            }
        }

        if(term.isCompoundTerm()) {
            List<Term> subterms = ((FunctionTerm) term).getSubterms();
            for(int i=0; i< subterms.size(); i++) {
                for(Rewrite rewrite : rewrites) {
                    Optional<Collection<Equality>> unifier = Unify.unify(subterms.get(i), rewrite.getLhs());
                    if(unifier.isPresent()) {
                        subterms.set(i, RewriteUtils.applySubstitution(rewrite.getRhs(), unifier.get()));
                    }
                }
            }
        }

        return null;
    }
}
