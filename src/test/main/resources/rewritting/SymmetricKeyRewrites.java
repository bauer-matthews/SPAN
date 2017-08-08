package resources.rewritting;

import resources.signature.SymmetricKey;
import rewriting.Rewrite;
import rewriting.terms.FunctionSymbol;
import rewriting.terms.FunctionTerm;
import rewriting.terms.Term;
import rewriting.terms.VariableTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 8/8/17
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class SymmetricKeyRewrites {

    public static final Collection<Rewrite> REWRITE_RULES = new ArrayList<>();

    static {

        List<Term> encSubterms = new ArrayList<>();
        encSubterms.add(SymmetricKey.VAR_M);
        encSubterms.add(SymmetricKey.VAR_K);
        FunctionTerm enc = new FunctionTerm(SymmetricKey.ENC_SYMBOL, encSubterms);

        List<Term> decSubterms = new ArrayList<>();
        decSubterms.add(enc);
        decSubterms.add(SymmetricKey.VAR_K);
        FunctionTerm dec = new FunctionTerm(SymmetricKey.DEC_SYMBOL, decSubterms);

        Rewrite rewrite = new Rewrite(dec, SymmetricKey.VAR_M);
        REWRITE_RULES.add(rewrite);
    }
}
