package util.rewrite;

import rewriting.Equality;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.Collection;

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

        Term subLhs = equality.getLhs();
        Term subRhs = equality.getRhs();

        for (Equality subEquality : equalities) {
            subLhs = subLhs.substitute((VariableTerm) subEquality.getLhs(), subEquality.getRhs());
            subRhs = subRhs.substitute((VariableTerm) subEquality.getLhs(), subEquality.getRhs());
        }

        return new Equality(subLhs, subRhs);
    }
}
