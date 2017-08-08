package rewriting;

import org.junit.Test;
import rewriting.terms.FunctionSymbol;
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
    public void TestUnify1() throws Exception{

        FunctionSymbol pairFunction = new FunctionSymbol("Pair", 2);

        ArrayList<Term> subtermsYK = new ArrayList<>();
        subtermsYK.add(new VariableTerm("y"));
        subtermsYK.add(new VariableTerm("k"));
        Term pairYK = new FunctionTerm(pairFunction, subtermsYK);

        ArrayList<Term> lhsSubterms = new ArrayList<>();
        lhsSubterms.add(new VariableTerm("x"));
        lhsSubterms.add(pairYK);

        Term lhs = new FunctionTerm(pairFunction, lhsSubterms);

        ArrayList<Term> rhsSubterms = new ArrayList<>();
        rhsSubterms.add(new VariableTerm("n"));
        rhsSubterms.add(new VariableTerm("z"));

        Term rhs = new FunctionTerm(pairFunction, rhsSubterms);

        Optional<Collection<Equality>> solution =  Unify.unify(new Equality(lhs, rhs));

        assert(solution.isPresent());
        assert(solution.get().size() == 2);

        Term newLhs = RewriteUtils.applySubstitution(lhs, solution.get());
        Term newRhs = RewriteUtils.applySubstitution(rhs, solution.get());

        assert(newLhs.equals(newRhs));
    }
}
