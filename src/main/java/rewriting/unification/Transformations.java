package rewriting.unification;

import rewriting.Equality;
import rewriting.terms.FunctionTerm;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mbauer on 7/30/2017.
 */
class Transformations {

    static boolean applyDelete(Collection<Equality> equalities) {

        for (Equality equality : equalities) {
            if (equality.getLhs().equals(equality.getRhs())) {
                equalities.remove(equality);
                return true;
            }
        }
        return false;
    }

    static boolean applyOrient(Collection<Equality> equalities) {

        for (Equality equality : equalities) {
            if ((!(equality.getLhs().isVariableTerm())) && (equality.getRhs().isVariableTerm())) {
                equalities.add(new Equality(equality.getRhs(), equality.getLhs()));
                equalities.remove(equality);
                return true;
            }
        }
        return false;
    }

    static boolean applyEliminate(Collection<Equality> equalities) {

        for (Equality equality : equalities) {

            Collection<VariableTerm> vars = new ArrayList<>();
            for (Equality equality1 : equalities) {
                if (!equality.equals(equality1)) {
                    vars.addAll(equality1.getLhs().getVariables());
                    vars.addAll(equality1.getRhs().getVariables());
                }
            }

            if (vars.contains(equality.getLhs())
                    && (equality.getLhs().isVariableTerm())
                    && (!(equality.getRhs().getVariables().contains(equality.getLhs())))) {

                Collection<Equality> newEqualities = new ArrayList<>();
                for (Equality equality1 : equalities) {

                    if (equality1.equals(equality)) {
                        newEqualities.add(equality);
                    } else {
                        newEqualities.add(new Equality(
                                equality1.getLhs().substitute((VariableTerm) equality.getLhs(), equality.getRhs()),
                                equality1.getRhs().substitute((VariableTerm) equality.getLhs(), equality.getRhs())));
                    }
                }

                equalities.clear();
                equalities.addAll(newEqualities);
                return true;
            }
        }
        return false;
    }

    static boolean applyFunction(Collection<Equality> equalities) {

        for (Equality equality : equalities) {
            if (equality.getLhs().isCompoundTerm() && equality.getRhs().isCompoundTerm()) {

                FunctionTerm lhs = (FunctionTerm) equality.getLhs();
                FunctionTerm rhs = (FunctionTerm) equality.getRhs();

                if (lhs.getRootSymbol().equals(rhs.getRootSymbol())) {

                    for (int i = 0; i < lhs.getRootSymbol().getArity(); i++) {
                        equalities.add(new Equality(
                                lhs.getSubterms().get(i), rhs.getSubterms().get(i)));
                    }

                    equalities.remove(equality);
                    return true;
                }
            }
        }
        return false;
    }
}
