package rewriting.unification;

import rewriting.Equality;
import rewriting.terms.Term;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by mbauer on 7/30/2017.
 */
public class Unify {

    public static Optional<Collection<Equality>> unify(Term term1, Term term2) {
        return unify(new Equality(term1, term2));
    }

    public static Optional<Collection<Equality>> unify(Equality unifyTerms) {

        Collection<Equality> solvedEqualities = computeSolvedForm(unifyTerms);

        if (properSolvedForm(solvedEqualities)) {
            return Optional.of(solvedEqualities);
        } else {
            return Optional.empty();
        }
    }

    private static Collection<Equality> computeSolvedForm(Equality unifyTerms) {
        Collection<Equality> transformedEqualities = new ArrayList<>();
        transformedEqualities.add(unifyTerms);

        boolean applied = false;
        do {
            applied = applyTransformations(transformedEqualities);
        } while (applied);

        return transformedEqualities;
    }

    private static boolean applyTransformations(Collection<Equality> equalities) {

        boolean applied = false;
        if (Transformations.applyFunction(equalities)) {
            applied = true;
        }

        if (Transformations.applyOrient(equalities)) {
            applied = true;
        }

        if (Transformations.applyEliminate(equalities)) {
            applied = true;
        }

        if (Transformations.applyDelete(equalities)) {
            applied = true;
        }

        return applied;
    }

    private static boolean properSolvedForm(Collection<Equality> equalities) {

        for (Equality equality : equalities) {
            if (!equality.getLhs().isVariableTerm()) {
                return false;
            }
        }

        return true;
    }
}
