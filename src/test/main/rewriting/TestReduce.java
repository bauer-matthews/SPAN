package rewriting;

import org.junit.Test;
import resources.rewritting.SymmetricKeyRewrites;
import resources.signature.SymmetricKey;
import rewriting.terms.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbauer on 8/1/2017.
 */
public class TestReduce {

    private static final NameTerm MESSAGE = new NameTerm("mes", true);
    private static final NameTerm KEY = new NameTerm("key", true);

    @Test
    public void TestReduce1() throws Exception {

        List<Term> cipher1Subterms = new ArrayList<>();
        cipher1Subterms.add(MESSAGE);
        cipher1Subterms.add(KEY);
        FunctionTerm cipher1 = new FunctionTerm(SymmetricKey.ENC_SYMBOL, cipher1Subterms);

        List<Term> cipher2Subterms = new ArrayList<>();
        cipher2Subterms.add(cipher1);
        cipher2Subterms.add(KEY);
        FunctionTerm cipher2 = new FunctionTerm(SymmetricKey.DEC_SYMBOL, cipher2Subterms);

        Term reducedTerm = RewriteEngine.reduce(cipher2, SymmetricKeyRewrites.REWRITE_RULES);
        assert (reducedTerm.equals(MESSAGE));

        List<Term> cipher3Subterms = new ArrayList<>();
        cipher3Subterms.add(cipher2);
        Term cipher3 = new FunctionTerm(SymmetricKey.HASH_SYMBOL, cipher3Subterms);

        List<Term> hashMessageTerms = new ArrayList<>();
        hashMessageTerms.add(MESSAGE);
        Term hashMessage = new FunctionTerm(SymmetricKey.HASH_SYMBOL, hashMessageTerms);

        reducedTerm = RewriteEngine.reduce(cipher3, SymmetricKeyRewrites.REWRITE_RULES);
        assert (hashMessage.equals(reducedTerm));

        List<Term> cipher4Subterms = new ArrayList<>();
        cipher4Subterms.add(cipher3);
        Term cipher4 = new FunctionTerm(SymmetricKey.HASH_SYMBOL, cipher4Subterms);

        List<Term> hashHashMessageTerms = new ArrayList<>();
        hashHashMessageTerms.add(hashMessage);
        Term hashHashMessage = new FunctionTerm(SymmetricKey.HASH_SYMBOL, hashHashMessageTerms);

        reducedTerm = RewriteEngine.reduce(cipher4, SymmetricKeyRewrites.REWRITE_RULES);
        assert (hashHashMessage.equals(reducedTerm));
    }
}