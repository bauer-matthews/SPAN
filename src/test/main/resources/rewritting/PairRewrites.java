package resources.rewritting;

import resources.signature.Pair;
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
public class PairRewrites {

    public static final Collection<Rewrite> REWRITE_RULES = new ArrayList<>();

    static {

        List<Term> pairSubterms = new ArrayList<>();
        pairSubterms.add(Pair.VAR_X);
        pairSubterms.add(Pair.VAR_Y);
        FunctionTerm pair = new FunctionTerm(Pair.PAIR_SYMBOL, pairSubterms);

        List<Term> fstSubterms = new ArrayList<>();
        fstSubterms.add(pair);
        FunctionTerm fst = new FunctionTerm(Pair.FST_SYMBOL, fstSubterms);

        List<Term> sndSubterms = new ArrayList<>();
        sndSubterms.add(pair);
        FunctionTerm snd = new FunctionTerm(Pair.SND_SYMBOL, sndSubterms);

        Rewrite fstRewrite = new Rewrite(fst, Pair.VAR_X);
        Rewrite sndRewrite = new Rewrite(snd, Pair.VAR_Y);

        REWRITE_RULES.add(fstRewrite);
        REWRITE_RULES.add(sndRewrite);
    }
}
