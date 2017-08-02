package rewriting;

import org.junit.Test;
import rewriting.terms.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mbauer on 8/1/2017.
 */
public class TestReduce {

    private static final VariableTerm VAR_M = new VariableTerm("m");
    private static final VariableTerm VAR_K = new VariableTerm("k");

    private static final NameTerm MESSAGE = new NameTerm("mes", true);
    private static final NameTerm KEY = new NameTerm("key", true);

    private static final FunctionSymbol ENC_SYMBOL = new FunctionSymbol("enc", 2);
    private static final FunctionSymbol DEC_SYMBOL = new FunctionSymbol("dec", 2);
    private static final FunctionSymbol HASH_SYMBOL = new FunctionSymbol("hash", 1);

    @Test
    public void TestReduce1() throws Exception {

        List<Term> encSubterms = new ArrayList<>();
        encSubterms.add(VAR_M);
        encSubterms.add(VAR_K);
        FunctionTerm enc = new FunctionTerm(ENC_SYMBOL, encSubterms);

        List<Term> decSubterms = new ArrayList<>();
        decSubterms.add(enc);
        decSubterms.add(VAR_K);
        FunctionTerm dec = new FunctionTerm(DEC_SYMBOL, decSubterms);

        List<Term> cipher1Subterms = new ArrayList<>();
        cipher1Subterms.add(MESSAGE);
        cipher1Subterms.add(KEY);
        FunctionTerm cipher1 = new FunctionTerm(ENC_SYMBOL, cipher1Subterms);

        List<Term> cipher2Subterms = new ArrayList<>();
        cipher2Subterms.add(cipher1);
        cipher2Subterms.add(KEY);
        FunctionTerm cipher2 = new FunctionTerm(DEC_SYMBOL, cipher2Subterms);

        Rewrite rewrite = new Rewrite(dec, VAR_M);
        Collection rewrites = new ArrayList();
        rewrites.add(rewrite);

        Term reducedTerm = RewriteEngine.reduce(cipher2, rewrites);
        assert (reducedTerm.equals(MESSAGE));

        List<Term> cipher3Subterms = new ArrayList<>();
        cipher3Subterms.add(cipher2);
        Term cipher3 = new FunctionTerm(HASH_SYMBOL, cipher3Subterms);

        List<Term> hashMessageTerms = new ArrayList<>();
        hashMessageTerms.add(MESSAGE);
        Term hashMessage = new FunctionTerm(HASH_SYMBOL, hashMessageTerms);

        reducedTerm = RewriteEngine.reduce(cipher3, rewrites);
        assert(hashMessage.equals(reducedTerm));

        List<Term> cipher4Subterms = new ArrayList<>();
        cipher4Subterms.add(cipher3);
        Term cipher4 = new FunctionTerm(HASH_SYMBOL, cipher4Subterms);

        List<Term> hashHashMessageTerms = new ArrayList<>();
        hashHashMessageTerms.add(hashMessage);
        Term hashHashMessage = new FunctionTerm(HASH_SYMBOL, hashHashMessageTerms);

        reducedTerm = RewriteEngine.reduce(cipher4, rewrites);
        assert(hashHashMessage.equals(reducedTerm));
    }
}