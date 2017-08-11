package util.rewrite;

import rewriting.Equality;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mbauer on 7/31/2017.
 */
public class RewriteUtils {

    public static Term applySubstitution(Term term, Collection<Equality> equalities) {

        for (Equality equality : equalities) {
            term = term.substitute((VariableTerm) equality.getLhs(), equality.getRhs());
        }
        return term;
    }

    public static Equality applySubstitution(Equality equality, Collection<Equality> equalities) {

        Term subLhs = applySubstitution(equality.getLhs(), equalities);
        Term subRhs = applySubstitution(equality.getRhs(), equalities);

        return new Equality(subLhs, subRhs);
    }

    public static List<Term> applySubstitution(List<Term> terms, Collection<Equality> equalities) {

        List<Term> newTerms = new ArrayList<>();

        for (Term term : terms) {
            newTerms.add(applySubstitution(term, equalities));
        }

        return newTerms;
    }
}
