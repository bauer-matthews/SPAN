package rewriting;

import cache.SubstitutionCache;
import org.junit.Test;
import resources.rewritting.PairRewrites;
import resources.signature.Pair;
import rewriting.terms.FunctionTerm;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;
import rewriting.unification.Unify;
import util.rewrite.RewriteUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by mbauer on 7/30/2017.
 */
public class TestUnification {

    @Test
    public void TestUnify1() throws Exception {

        ArrayList<Term> subtermsYK = new ArrayList<>();
        subtermsYK.add(new VariableTerm("y"));
        subtermsYK.add(new VariableTerm("k"));
        Term pairYK = new FunctionTerm(Pair.PAIR_SYMBOL, subtermsYK);

        ArrayList<Term> lhsSubterms = new ArrayList<>();
        lhsSubterms.add(new VariableTerm("x"));
        lhsSubterms.add(pairYK);

        Term lhs = new FunctionTerm(Pair.PAIR_SYMBOL, lhsSubterms);

        ArrayList<Term> rhsSubterms = new ArrayList<>();
        rhsSubterms.add(new VariableTerm("n"));
        rhsSubterms.add(new VariableTerm("z"));

        Term rhs = new FunctionTerm(Pair.PAIR_SYMBOL, rhsSubterms);

        Optional<Collection<Equality>> solution = Unify.unify(new Equality(lhs, rhs));

        assert (solution.isPresent());
        assert (solution.get().size() == 2);

        Term newLhs = SubstitutionCache.applySubstitution(lhs, solution.get());
        Term newRhs = SubstitutionCache.applySubstitution(rhs, solution.get());

        assert (newLhs.equals(newRhs));
    }
}
